package gamenet;

/**
 * The <b>GameControl</b> class provides the overall framework for gameNet. This
 * class has a constructor for starting up a server to host a game. Another
 * constructor is available for connecting to an existing server.
 */
public class GameControl {
	/**
	 * The starting port number for servers, overwritten with whatever is used.
	 */
	private int serverPortNum = 54321;
	/**
	 * The server that handles client connections.
	 */
	private GameServer gameServer = null;
	/**
	 * The IP address of the server, in human-readable format.
	 */
	private String ipAddr = null;
	/**
	 * The game logic. 
	 */
	private GameNetCoreGame coreGame = null;
	/**
	 * The object to create the server?
	 */
	private GameCreator gameCreator;

	/**
	 * This is method returns the IP address used for this game.
	 * 
	 * @return IP address for this game
	 */
	public String getIpAddress() {
		return ipAddr;
	}

	/**
	 * This is method returns the port number used for this game.
	 * 
	 * @return port number for this game
	 */
	public int getPortNum() {
		return serverPortNum;
	}

	/**
	 * This constructor is used when everything is on the same computer and you
	 * aren't needing to network.
	 * @param gc the "game creator" to set up the game?
	 */
	public GameControl(final GameCreator gc) {
		gameCreator = gc;
	}

	/**
	 * Prepare to connect to a server that lives elsewhere. TODO: turn into a
	 * real constructor.
	 * 
	 * @param addr
	 *            the IP address of the server
	 * @param port
	 *            the port number of the server
	 */

	public void connect_to_server(final String addr, final int port) {
		ipAddr = addr;
		serverPortNum = port;
	}

	/**
	 * If your GameControl requires a thread to return updates that are not
	 * related to user inputs, then this method must be called to send the
	 * updates back to the GUI. For example, a Pong game would need this to send
	 * ball updates that are not directly associated with a user input.
	 * 
	 * @param ob
	 *            an output that is returned to the GUI from the GameControl
	 */

	public void putMsgs(final Object ob) {
		if (gameServer != null) {
			gameServer.putOutputMsgs(ob);
		}
	}

	/**
	 * Use this constructor if the Server for this game lives in this program.
	 * Note that the actual variable createServer is not actually used.
	 * 
	 * TODO: make this a real constructor.
	 */
	void startServer() {
		try {
			coreGame = gameCreator.createGame();
			gameServer = new GameServer(serverPortNum, coreGame);
			gameServer.start();

			coreGame.startGame(this);

			// getPortNum will not return until the server has started. If for
			// some reason we can't start the server after 20 port numbers, an
			// exception will occur.

			serverPortNum = gameServer.getPortNum();
			ipAddr = gameServer.getInetAddress();

			System.out.println("Starting GameControl Server ipAddress("
					+ ipAddr + ")  portNum (" + serverPortNum + ")");
		} catch (RuntimeException e) {
			System.out.println("GameControl: Runtime Exception:" + e);
		}
	}

	/**
	 * Shut down the server.
	 */

	void endGame() {
		if (gameServer != null) {
			System.out.println("endGame ");
			gameServer.stopServer();
		}
	}
}
