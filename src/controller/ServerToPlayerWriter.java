package controller;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.eclipse.jdt.annotation.Nullable;

/**
 * A thread to send replies and game-state updates to clients.
 *
 * @author Clem Hasselbach (original of code from which this is adapted)
 * @author Jonathan Lovelace (cleanups, docs, adaptation to fit our needs here)
 */
public class ServerToPlayerWriter extends Thread {
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
	 * @param message the message to queue.
	 */
	public void queue(final String message) {
		messages.add(message);
		notify();
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
}
