package gamenet;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
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
	private final String inetAddress;

	/**
	 * This should really use one of Java's built-in classes that represents an
	 * IP address ...
	 *
	 * @return the address of the server
	 */
	public String getInetAddress() {
		return inetAddress;
	}
	/**
	 * Connections to clients?
	 */
	private final LinkedList<ServerClientHandler> gamePlayers =
			new LinkedList<>();
	/**
	 * The port number of the server.
	 */
	private int portNum;
	/**
	 * Whether the server has started.
	 */
	private boolean serverStarted = false;
	/**
	 * Whether the server is listening. (?)
	 */
	private boolean listening = true;
	/**
	 * The socket to listen on for new connections.
	 */
	private ServerSocket serverSocket;
	/**
	 * The "core Game" (?).
	 */
	private GameNetCoreGame coreGame;
	/**
	 * @return the port number, but only after the server has started
	 */
	public synchronized int getPortNum() {
		while (!serverStarted) {
			try {
				wait();
			} catch (InterruptedException e) {
				continue;
			}
		}
		System.out.println(" getPortNum = " + portNum);
		return portNum;
	}

	/**
	 * Mark that the server has started, and wake up every thread that is
	 * waiting on us.
	 */
	private synchronized void markServerStarted() {
		serverStarted = true;
		System.out.println(" markServerStarted");
		notifyAll();
	}
	/**
	 *  We had set up inetAddress in run(), with the comment
	 * "Make the server available to callers", but code calling getInetAddress
	 * isn't null-safe.
	 *
	 * @throws UnknownHostException
	 *             if the local host can't be resolved to an address
	 * @param port the port to listen on
	 * @param gi the game-logic part of the server
	 */
	public GameServer(final int port, final GameNetCoreGame gi)
			throws UnknownHostException {
		inetAddress = InetAddress.getLocalHost().getHostAddress();
		portNum = port;
		coreGame = gi;
	}

	/**
	 * Utility method to take objects received from one GamePlayer conversation,
	 * process it, and put it into the Message Queue of all GamePlayer
	 * conversations.
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
		for (final ServerClientHandler p : gamePlayers) {
			p.put(ob);
		}
	}
	/**
	 * @param index the index of a "player" to remove.
	 */
	public synchronized void removeMe(final int index) {
		for (final ServerClientHandler c : gamePlayers) {
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
			// Make port available sooner even if it is being timed out.
			serverSocket.setReuseAddress(true);

			markServerStarted();

			while (listening) {
				@SuppressWarnings("resource")
				final Socket nextSock = serverSocket.accept();
				System.out.println(nThreadCount + " Another Thread Created");

				// Create a thread to process incoming connection
				ServerClientHandler gamePlayerChild =
						new ServerClientHandler(nextSock, this, nThreadCount++);
				gamePlayers.add(gamePlayerChild);
				gamePlayerChild.start();
			}
			serverSocket.close();
		} catch (IOException e) {
			System.out.println("GameServer.run Exception:" + e);
		}

	}
	/**
	 * Stop the server.
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
			ServerClientHandler p = gamePlayers.remove(i);
			p.stopGamePlayer();
		}
	}
}
