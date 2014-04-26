package gamenet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * A thread for the server.
 * 
 * @author Clem Hasselbach (original)
 * @author Jonathan Lovelace (cleanups, docs)
 */
class GameServer extends Thread {
	/**
	 * The address of the server.
	 */
	private String inetAddress = null;
	/**
	 * This should really use one of Java's built-in classes that represents an IP address ...
	 * @return the address of the server
	 */
	public String getInetAddress() {
		return inetAddress;
	}
	/**
	 * Connections to clients?
	 */
	private final LinkedList<GamePlayerProcess1> gamePlayers =
			new LinkedList<>();
	/**
	 * The port number of the server
	 */
	private int portNum;
	/**
	 * Whether the server has started.
	 */
	boolean serverStarted = false;
	/**
	 * Whether the server is listening. (?)
	 */
	boolean listening = true;
	/**
	 * The socket to listen on for new connections.
	 */
	private ServerSocket serverSocket = null;
	/**
	 * The "core Game" (?).
	 */
	GameNet_CoreGame coreGame = null;
	/**
	 * @return the port number, but only after the server has started
	 */
	public synchronized int getPortNum() {
		try {
			if (!serverStarted) {
				wait();
			}
		} catch (InterruptedException e) {
			// Continue
		}
		System.out.println(" getPortNum = " + portNum);
		return portNum;
	}
	/**
	 * Mark that the server has started, and wake up every thread that is waiting on us.
	 */
	private synchronized void markServerStarted() {
		serverStarted = true;
		System.out.println(" markServerStarted");
		notifyAll();
	}
	/**
	 * @param port the port to listen on
	 * @param gi the game-logic part of the server
	 */
	public GameServer(final int port, final GameNet_CoreGame gi) {
		portNum = port;
		coreGame = gi;
	}

	/**
	 * Utility method to take objects received from one GamePlayer conversation,
	 * Process it, and put it into the Message Queue of all GamePlayer
	 * Conversations
	 * @param ob the message
	 */
	public synchronized void putInputMsgs(final Object ob) {
		Object ob2 = coreGame.process(ob);
		if (ob2 != null) {
			putOutputMsgs(ob2);
		}
	}
	/**
	 * @param ob a message to send to all "players".
	 */
	public synchronized void putOutputMsgs(final Object ob) {
		for (final GamePlayerProcess1 p : gamePlayers) {
			p.put(ob);
		}
	}
	/**
	 * @param index the index of a "player" to remove.
	 */
	public synchronized void removeMe(final int index) {
		for (final GamePlayerProcess1 c : gamePlayers) {
			if (index == c.getIndex()) {
				c.stopGamePlayer();
				gamePlayers.remove(c);
				break;
			}
		}
	}
	/**
	 * The main loop of this thread.
	 */
	@Override
	public void run() {
		int nThreadCount = 0;

		try {
			for (int i = 0; i <= 20; i++) {
				if (i == 20) {
					throw new RuntimeException(
							"Exhausted possible port numbers");
				}
				try {
					serverSocket = new ServerSocket(portNum);
					break;
				} catch (IOException e) {
					System.out.println("GameServer.run Exception :" + e);
					portNum += 1;
					System.out
							.println("Assuming port collision, retry with port "
									+ portNum);
				}
			}
			final InetAddress iaddr = InetAddress.getLocalHost();

			// Make the server available to callers
			inetAddress = iaddr.getHostAddress();
			// Make port available sooner even if it is being timed out.
			serverSocket.setReuseAddress(true);

			markServerStarted();

			while (listening) {
				@SuppressWarnings("resource")
				final Socket nextSock = serverSocket.accept();
				System.out.println(nThreadCount + " Another Thread Created");

				// Create a thread to process incoming connection
				GamePlayerProcess1 gamePlayerChild =
						new GamePlayerProcess1(nextSock, this, nThreadCount++);
				gamePlayers.add(gamePlayerChild);
				gamePlayerChild.start();
			}
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("GameServer.run Exception:" + e);
		}

	}
	/**
	 * Stop the server
	 */
	public void stopServer() {
		listening = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
			// Ignore; this is cleanup code anyway.
		}

		// Go in the inverse order because we are removing entries from
		// the list.
		for (int i = gamePlayers.size() - 1; i >= 0; i--) {
			GamePlayerProcess1 p = gamePlayers.remove(i);
			p.stopGamePlayer();
		}
	}
}
