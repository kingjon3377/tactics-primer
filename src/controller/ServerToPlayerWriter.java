package controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import model.IPoint;
import model.ITileFixture;
import model.ProxyUnit;
import model.SimpleUnit;
import model.TileType;

import org.eclipse.jdt.annotation.Nullable;

import protocol.FixtureMoveMessage;
import protocol.FixtureRemovalMessage;
import protocol.MultiMessageMessage;
import protocol.OpposingUnitMessage;
import protocol.OwnUnitMessage;
import protocol.PlayerPresentMessage;
import protocol.RPCMessage;
import protocol.TerrainChangeMessage;
import protocol.TurnEndMessage;

import common.MapUpdateListener;

/**
 * A thread to send replies and game-state updates to clients.
 *
 * @author Clem Hasselbach (original of code from which this is adapted)
 * @author Jonathan Lovelace (cleanups, docs, adaptation to fit our needs here)
 */
public class ServerToPlayerWriter extends Thread implements MapUpdateListener {
	/**
	 * The connection to the server.
	 */
	private final Socket socket;
	/**
	 * The number of the current player.
	 */
	private int player = -1;
	/**
	 * Whether we should continue to run.
	 */
	private boolean continueFlag = true;
	/**
	 * Messages to send.
	 */
	private final ConcurrentLinkedQueue<RPCMessage> messages =
			new ConcurrentLinkedQueue<>();

	/**
	 * Constructor.
	 *
	 * @param sock
	 *            the socket connecting us to the client
	 */
	public ServerToPlayerWriter(final Socket sock) {
		socket = sock;
	}

	/**
	 * Stop this thread at the next opportunity.
	 */
	public void stopWriter() {
		continueFlag = false;
		notify();
	}

	/**
	 * Queue a message.
	 *
	 * @param message
	 *            the message to queue. Marked as nullable because Eclipse
	 *            doesn't know that String.format can't return null.
	 */
	public void queue(@Nullable final RPCMessage message) {
		if (message instanceof MultiMessageMessage) {
			for (RPCMessage msg : (MultiMessageMessage) message) {
				queue(msg);
			}
		} else if (message != null) {
			messages.add(message);
			notify();
		}
	}

	/**
	 * Either immediately return the next message in the queue (popping it) or,
	 * if the queue is empty, wait for the next message to be placed in it and
	 * then pop and return that.
	 *
	 * @return the message, or none if we should stop
	 */
	@Nullable
	private synchronized RPCMessage get() {
		while (continueFlag) {
			if (messages.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException except) {
					continue;
				}
			} else {
				final RPCMessage retval = messages.remove();
				return retval;
			}
		}
		return null;
	}

	/**
	 * The main loop of the thread. Reads messages from the queue (blocking if
	 * empty) and writes them to the socket.
	 */
	@Override
	public void run() {
		try (ObjectOutputStream out =
				new ObjectOutputStream(socket.getOutputStream())) {
			RPCMessage message;
			while (continueFlag && (message = get()) != null) {
				// AcknowledgeMessage, used to say "yes" to player number
				// proposals, doesn't say which number. But it's one more than
				// the previous proposal, so we update our guess every time a
				// proposal is rejected.
				if (message instanceof PlayerPresentMessage) {
					player = ((PlayerPresentMessage) message).getNumber() + 1;
				}
				out.writeObject(message);
				out.flush();
				out.reset();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return which player we're connected to
	 */
	@Override
	public int getPlayer() {
		return player;
	}

	/**
	 * @param point
	 *            the point where the terrain changed
	 * @param type
	 *            the new terrain there
	 */
	@Override
	public void terrainChanged(final IPoint point, final TileType type) {
		queue(new TerrainChangeMessage(point, type));
	}

	/**
	 * @param point
	 *            the location of the new fixture
	 * @param fix
	 *            the fixture added
	 */
	@Override
	public void fixtureAdded(final IPoint point, final ITileFixture fix) {
		if (fix instanceof ProxyUnit) {
			queue(new OpposingUnitMessage(point, (ProxyUnit) fix));
		} else if (fix instanceof SimpleUnit) {
			queue(new OwnUnitMessage(point, (SimpleUnit) fix));
		} else {
			// FIXME: Not yet implemented
			throw new IllegalStateException("FIXME: Not yet implemented");
		}
	}

	/**
	 * @param point
	 *            the location of the fixture to remove
	 * @param fix
	 *            the fixture to remove
	 */
	@Override
	public void fixtureRemoved(final IPoint point, final ITileFixture fix) {
		queue(new FixtureRemovalMessage(point, fix.getID()));
	}

	/**
	 * @param source
	 *            the location the fixture moves from
	 * @param dest
	 *            the location the fixture moves to
	 * @param fix
	 *            the fixture that is moving
	 */
	@Override
	public void fixtureMoved(final IPoint source, final IPoint dest,
			final ITileFixture fix) {
		queue(new FixtureMoveMessage(source, dest, fix.getID()));
	}

	/**
	 * @param playr
	 *            the player whose turn it is now
	 */
	@Override
	public void endTurn(final int playr) {
		queue(new TurnEndMessage(playr));
	}
}
