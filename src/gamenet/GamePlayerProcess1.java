package gamenet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.LinkedList;

/**
 * A sample (?) "player" process.
 * @author Clem Hasselbach (original)
 * @author Jonathan Lovelace (cleanups, docs)
 *
 */
public class GamePlayerProcess1 extends Thread {
	/**
	 * The connection to the server.
	 */
	private final Socket socket;
	/**
	 * The current index. (Into what?)
	 */
	private final int myIndex;
	/**
	 * Whether the thread should continue to run.
	 */
	private boolean continueFlag = true;
	/**
	 * The server object that owns this thread (?).
	 */
	private GameServer mom;
	/**
	 * Messages (to send? that we received?).
	 */
	private final LinkedList<Object> msgObjects = new LinkedList<>();
	/**
	 * @return the current index (into what?)
	 */
	public int getIndex() {
		return myIndex;
	}

	/**
	 * Stop this thread, cleaning up the socket.
	 */
	public synchronized void stopGamePlayer() {
		continueFlag = false;
		notify(); // wake up thread waiting for a message
		try {
			socket.close();
		} catch (IOException e) {
			// Ignore; we're stopping anyway.
		}
	}

	/**
	 * Add a message to the queue.
	 * @param obj the message to add.
	 */
	public synchronized void put(final Object obj) {
		msgObjects.add(obj);
		notify(); // wake up Thread waiting for this message
	}

	/**
	 * Either immediately return the next message in the queue (popping it) or,
	 * if the queue is empty, wait for a new message to enter and then pop and
	 * return it.
	 * 
	 * @return the message
	 */
	public synchronized Object get() {
		Object retval;
		while (continueFlag) {
			if (msgObjects.isEmpty()) {
				try {
					wait();
				} catch (InterruptedException e) {
					continue;
				}
			} else {
				retval = msgObjects.removeFirst();
				return retval;
			}
		}
		return null;
	}
	/**
	 * @param s the socket connecting us to the server
	 * @param m the server that owns us
	 * @param me our index (into what?)
	 */
	public GamePlayerProcess1(final Socket s, final GameServer m, final int me) {
		socket = s;
		myIndex = me;
		mom = m; // Mother GameServer Task
	}
	
	/**
	 * The main loop of the thread. Starts up a subthread to read from the
	 * socket, then read messages from the queue (blocking if empty) and write
	 * them to the socket.
	 */
	@Override
	public void run() {
		new ServerFromPlayerReaderThread(socket, mom, myIndex).start();
		try (ObjectOutputStream out =
				new ObjectOutputStream(socket.getOutputStream())) {
			Object outputOb;
			while ((outputOb = get()) != null) {
				out.writeObject(outputOb);
				out.reset();
			}
			socket.close();
		} catch (IOException e) {
			System.out.println("GamePlayerProcess1 Err: " + e);
		} finally {
			System.out.println("GamePlayerProcess1.run Terminating : "
					+ myIndex);
			try { // I'm annoyed that I need try ... catch to keep the compiler
					// happy here
				socket.close();

			} catch (IOException e) {
				System.out
						.println("GamePlayerProcess1.run Exception closing sockets :"
								+ e);
			}
		}
	}
}
