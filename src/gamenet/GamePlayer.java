package gamenet;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * The <b>GamePlayer</b> class manages the socket connection with the
 * GameControl.
 * 
 * When the GUI wants to send a message to the GameControl, the
 * <b>sendMessage</b> routine is called to write the message object to an
 * ObjectOutputStream.
 * 
 * When the GameControl wants to send a message to the GUI, the GamePlayer
 * Thread will read the object from the ObjectInputStream. The GamePlayer class
 * passes it along to the GUI by calling the <b>receivedMessage</b> method.
 * 
 * Other key methods in GamePlayer are <b>joinGame</b> for creating the
 * connection to the GameControl, and <b>exitGame</b> to terminate the
 * connection.
 * 
 * @author Clem Hasselbach (original, most documentation)
 * @author Jonathan Lovelace (cleanups, further docs)
 */
public class GamePlayer extends Thread {
	/**
	 * Listeners to be notified if a connection is broken.
	 */
	private final List<ConnectionBrokenListener> connectionBrokenListeners =
			new ArrayList<>();
	/**
	 * The name of the player this is connected to.
	 */
	private final String playerName;
	/**
	 * The server or connection to the server.
	 */
	private final GameControl gameControl;
	/**
	 * The socket connecting to the server.
	 */
	private final Socket gameSocket;
	/**
	 * A stream for reading objects from the socket.
	 */
	private final ObjectInputStream socketInput;
	/**
	 * A stream for writing objects to the socket.
	 */
	private final ObjectOutputStream socketOutput;
	/**
	 * The game's UI.
	 */
	private final GameNet_UserInterface userInterface;
	/**
	 * Whether the socket is still alive.
	 */
	private boolean socketAlive = true;

	/**
	 * The GamePlayer constructor needs the name of the player and the
	 * GameControl class to connect to.
	 * 
	 * @param game the server to connect to
	 * @param r the user interface
	 * @param plName the name of the player
	 * 
	 */
	public GamePlayer(final String plName, final GameControl game,
			final GameNet_UserInterface r) {
		playerName = plName;
		if (game == null) {
			throw new RuntimeException("joinGame called on a null gameControl");
		}
		gameControl = game;
		userInterface = r;

		// There could be a little bit of a race condition
		// in the next 2 statements ... can be solved with a little
		// more logic.


		try {
			gameSocket =
					new Socket(gameControl.getIpAddress(),
							gameControl.getPortNum());
			if (gameSocket == null) {
				throw new RuntimeException("joinGame gameSocket null error");
			}

			// Create in/out classes associated with the Open Socket
			ObjectOutputStream tempSocketOutput =
					new ObjectOutputStream(gameSocket.getOutputStream());

			socketInput = new ObjectInputStream(gameSocket.getInputStream());
			if (socketInput == null) {
				throw new RuntimeException("joinGame socketInput null error");
			}

			// Put in a pause to allow some time to get the server
			// side thread up.
			// It turns out that a sendMessage is likely to be sent
			// immediately when this call returns.

			try {
				Thread.sleep(500); // Sleep for 1/2 second
			} catch (InterruptedException e) {
				// continue
			}

			// Start up a Thread to read from the socket and write
			// the contents to the screen

			this.start();
			try {
				Thread.sleep(500); // Sleep for 1/2 second
			} catch (InterruptedException e) {
				// continue
			}
			socketOutput = tempSocketOutput;

		} catch (UnknownHostException e) {
			throw new RuntimeException("Can't find host", e);
		} catch (IOException e) {
			throw new RuntimeException("I/O error joining the game", e);
		}
	}

	/**
	 * @return the name of the player associated with this connection.
	 */
	public String getPlayerName() {
		return playerName;
	}

	/**
	 * If you want your GUI to find out when a GameControl connection is broken,
	 * your GUI must implement the Interface_ConnectionBroken interface. Use
	 * setGameConnectionBroken to let GamePlayer know about this interface.
	 * 
	 * @param gcb a listener to be called when a socket disconnects.
	 */
	public void addConnectionBrokenListener(final ConnectionBrokenListener gcb) {
		connectionBrokenListeners.add(gcb);
	}

	/**
	 * The Thread that is underneath the GamePlayer class is responsible for
	 * reading the ObjectOutputStream from the GameControl. Every GameOutputObj
	 * read from the ObjectOutputStream is passed on by calling the
	 * receivedMessage routine. This routine shouldn't be accessed directly from
	 * gameControl code. It has to be public to properly run as a thread.
	 */
	@Override
	public void run() {
		try {
			Object outputFromSocket;
			// Read from Socket and write to Screen
			while ((outputFromSocket = socketInput.readObject()) != null) {
				receivedMessage(outputFromSocket);
			}
		} catch (ClassNotFoundException e) {
			System.out
					.println("GamePlayer.run Class Not Found Exception: " + e);
		} catch (IOException e) {
			System.out.println("GamePlayer.run Exception: " + e);
		}
		// It's easier for the socket reader to detect that the socket
		// is gone. We need to set a flag so that the socket writer
		// will know that it's time to give up.

		socketAlive = false;
		for (ConnectionBrokenListener listener : connectionBrokenListeners) {
			listener.gameConnectionBroken();
		}
		System.out.println("GamePlayer.run Thread terminating ");

	}

	/**
	 * GameIsAlive can be called to find out if the GamePlayer socket is still
	 * connected.
	 * 
	 * @return true if socket is still alive
	 */
	public boolean isGameAlive() {
		return socketAlive;
	}

	/**
	 * exitGame is called when you want to terminate the connection to the
	 * GameControl.
	 * 
	 */
	void exitGame() {
		System.out.println("GamePlayer.exitGame " + playerName);
		try {
			if (socketOutput != null) {
				socketOutput.close(); // Close output stream side of the socket
			}
			if (socketInput != null) {
				socketInput.close(); // Close input stream side of socket
			}
			if (gameSocket != null) {
				gameSocket.close(); // Close the socket
			}
		} catch (IOException e) {
			// Do nothing; we're exiting
		}
	}

	/**
	 * Call this version of exitGame if you want to pass one last object to the
	 * gameControl and then terminate the connection to the GameControl.
	 * 
	 * @param ob
	 *            Object to pass to the GameControl before terminating
	 */
	void exitGame(final Object ob) {
		sendMessage(ob);
		exitGame();
	}

	/*
	 * Some useful routines contained in GamePlayer:
	 * 
	 * void sendMessage(MyGameInput); // Send to GameControl
	 * 
	 * boolean GameIsAlive(); //Tests if socket is alive
	 */
	/**
	 * Exit and disconnect from the server.
	 */
	public void doneWithGame() {
		exitGame(); // Our GamePlayer object disconnects from the gameControl
		gameControl.endGame(); // If we own the server, it will shutdown
	}

	/**
	 * sendMessage provides a way to send an object to the GameControl.
	 * 
	 * @param obj the message to pass to the server
	 */
	public void sendMessage(final Object obj) {
		try {
			if (socketOutput != null) {
				socketOutput.writeObject(obj);
				socketOutput.reset();
			}
		} catch (IOException e) {
			System.out.println("GamePlayer.sendMessage Exception: " + e);
		}
	}

	/**
	 * To use GamePlayer, you must create a class which extends GamePlayer and
	 * overrides receivedMessage. The receivedMessage is the routine that is
	 * called to receive any messages from the GameControl.
	 * 
	 * @param obj
	 *            message received from the server
	 */

	protected void receivedMessage(final Object obj) {
		userInterface.receivedMessage(obj);
	}

}
