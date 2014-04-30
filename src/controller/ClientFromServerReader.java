package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import view.TPUI;

/**
 * A thread for a client to handle messages from the server.
 * @author Jonathan Lovelace
 *
 */
public class ClientFromServerReader extends Thread {
	/**
	 * The socket connecting to the server.
	 */
	private final Socket socket;
	/**
	 * Whether we should continue to read.
	 */
	private boolean continueFlag = true;
	/**
	 * The data model underlying the UI.
	 */
	// private final TPClient client;
	/**
	 * The UI. Used (directly) only for handling errors.
	 */
	 private final TPUI ui;
	/**
	 * Constructor.
	 * @param sock a socket connecting to the server.
	 * @param userInterface the UI to show any errors to the user
	 */
	public ClientFromServerReader(final Socket sock, final TPUI userInterface) {
		socket = sock;
		ui = userInterface;
	}
	/**
	 * Main loop of this thread.
	 */
	@Override
	public void run() {
		try (final ObjectInputStream in =
				new ObjectInputStream(socket.getInputStream())) {
			Object obj;
			while (continueFlag && (obj = in.readObject()) != null) {
				handleMessage(obj);
			}
		} catch (ClassNotFoundException e) {
			ui.showError("\"Class Not Found\" error handling server message", e);
		} catch (IOException e) {
			ui.showError("I/O error listening to server", e);
		}
	}
	/**
	 * Handle a message received from the server.
	 * @param message the message to handle.
	 */
	private void handleMessage(final Object message) {
		if (message instanceof String) {

		}
	}
}
