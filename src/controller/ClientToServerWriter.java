package controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jdt.annotation.Nullable;

import protocol.FullMapRequestMessage;
import protocol.PlayerRequestMessage;
import protocol.RPCMessage;

/**
 * A thread for the client to send requests to the server.
 * @author Jonathan Lovelace
 *
 */
public class ClientToServerWriter extends Thread {
	/**
	 * The socket connecting us to the server.
	 */
	private final Socket socket;
	/**
	 * Whether we should continue to write.
	 */
	private boolean continueFlag = true;
	/**
	 * Messages to send.
	 */
	private final ConcurrentLinkedQueue<RPCMessage> messages =
			new ConcurrentLinkedQueue<>();
	/**
	 * @param sock the connection to the server.
	 */
	public ClientToServerWriter(final Socket sock) {
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
		if (message != null) {
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
	 * Propose that we use the specified player number.
	 * @param player the proposed player number
	 */
	public void proposePlayer(final int player) {
		queue(new PlayerRequestMessage(player));
	}
	/**
	 * Ask the server to send us information about everything in the map.
	 * @param player our player number
	 */
	public void requestFullMap(final int player) {
		queue(new FullMapRequestMessage(player));
	}
}
