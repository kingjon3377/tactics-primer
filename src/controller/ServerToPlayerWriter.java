package controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.concurrent.ConcurrentLinkedQueue;

import model.IPoint;
import model.ITileFixture;
import model.ProxyUnit;
import model.TileType;

import org.eclipse.jdt.annotation.Nullable;

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
	private final int player;
	/**
	 * Whether we should continue to run.
	 */
	private boolean continueFlag = true;
	/**
	 * Messages to send.
	 */
	private final ConcurrentLinkedQueue<String> messages =
			new ConcurrentLinkedQueue<>();
	/**
	 * Constructor.
	 * @param sock the socket connecting us to the client
	 * @param index the number of the player we're handling
	 */
	public ServerToPlayerWriter(final Socket sock, final int index) {
		socket = sock;
		player = index;
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
	public void queue(@Nullable final String message) {
		if (message != null) {
			messages.add(message);
			notify();
		}
	}
	/**
	 * Either immediately return the next message in the queue (popping it) or,
	 * if the queue is empty, wait for the next message to be placed in it and
	 * then pop and return that.
	 * @return the message, or none if we should stop
	 */
	@Nullable
	private synchronized String get() {
		while (continueFlag) {
			if (messages.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException except) {
					continue;
				}
			} else {
				final String retval = messages.remove();
				return retval;
			}
		}
		return null;
	}

	/**
	 * The main loop of the thread. Reads messages from the queue (blocking if
	 * empty) and writes them to the socket.
	 *
	 * FIXME: The class that starts us and the reader thread should manage the
	 * socket, not us.
	 */
	@Override
	public void run() {
		try (ObjectOutputStream out =
				new ObjectOutputStream(socket.getOutputStream())) {
			String message;
			while (continueFlag && (message = get()) != null) {
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
	 * @param point the point where the terrain changed
	 * @param type the new terrain there
	 */
	@Override
	public void terrainChanged(final IPoint point, final TileType type) {
		queue(String.format("TERRAIN %d %d %d", point.getRow(),
				point.getColumn(), type.ordinal()));
	}
	/**
	 * @param point the location of the new fixture
	 * @param fix the fixture added
	 */
	@Override
	public void fixtureAdded(final IPoint point, final ITileFixture fix) {
		if (fix instanceof ProxyUnit) {
			try {
				queue(String.format("OPPUNIT %d %d %d %s %d %c %s %d %d %d",
						point.getRow(), point.getColumn(), fix.getID(),
						URLEncoder.encode(fix.getDescription(), "C"),
						fix.getOwner(), fix.getCharacter(),
						URLEncoder.encode(fix.getImage(), "C"),
						((ProxyUnit) fix).getHealthTier().ordinal(),
						((ProxyUnit) fix).getTotalAttackDice(),
						((ProxyUnit) fix).getTotalRangedAttackDice()));
			} catch (UnsupportedEncodingException e) {
				// FIXME: Not yet implemented
				throw new IllegalStateException("FIXME: Not yet implemented");
			}
		} else {
			// FIXME: Not yet implemented
			throw new IllegalStateException("FIXME: Not yet implemented");
		}
	}
	/**
	 * @param point the location of the fixture to remove
	 * @param fix the fixture to remove
	 */
	@Override
	public void fixtureRemoved(final IPoint point, final ITileFixture fix) {
		queue(String.format("REMOVE %d %d %d", point.getRow(),
				point.getColumn(), fix.getID()));
	}
	/**
	 * @param source the location the fixture moves from
	 * @param dest the location the fixture moves to
	 * @param fix the fixture that is moving
	 */
	@Override
	public void fixtureMoved(final IPoint source, final IPoint dest,
			final ITileFixture fix) {
		queue(String.format("MOVE %d %d %d %d %d", fix.getID(),
				source.getRow(), source.getColumn(), dest.getRow(),
				dest.getColumn()));
	}
	/**
	 * @param playr the player whose turn it is now
	 */
	@Override
	public void endTurn(final int playr) {
		queue(String.format("TURN %d", playr));
	}
}
