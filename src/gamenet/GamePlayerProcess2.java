package gamenet;

import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * A second thread to handle a 'player'.
 * 
 * @author Clem Hasselbach (original)
 * @author Jonathan Lovelace (cleanups, docs)
 */
class GamePlayerProcess2 extends Thread {
	/**
	 * The server that owns this thread.
	 */
	private GameServer mom;
	/**
	 * The socket connecting to the client.
	 */
	private Socket sock;
	/**
	 * Our index in the table of threads.
	 */
	private int myIndex;

	/**
	 * @param s
	 *            the socket we are handling
	 * @param m
	 *            the server that owns this
	 * @param index
	 *            our index in the table of threads
	 */
	public GamePlayerProcess2(final Socket s, final GameServer m,
			final int index) {
		sock = s;
		mom = m;
		myIndex = index;
	}

	/**
	 * The main loop of this thread.
	 */
	@Override
	public void run() {
		// Read from socket and put the string read into all message queues for
		// all conversations.

		try (final ObjectInputStream in =
				new ObjectInputStream(sock.getInputStream())) {
			Object inputObj;

			// Read from socket
			while ((inputObj = in.readObject()) != null) {
				mom.putInputMsgs(inputObj);
			}
		} catch (ClassNotFoundException e) {
			System.out.println("GamePlayerProcess2.run Class Not Found Err: "
					+ e);
			// e.printStackTrace();
		} catch (Exception e) {
			System.out.println("GamePlayerProcess2.run Err: " + e);
			// e.printStackTrace();
		}
		try { // I'm annoyed that I need try/catch to keep the compiler happy
				// here
			sock.close();
			System.out
					.println("GamePlayerProcess2.run terminating: " + myIndex);
			mom.removeMe(myIndex); // just remove me
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
