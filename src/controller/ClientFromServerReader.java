package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;

import model.Point;
import model.ProxyUnit;
import model.Unit.HealthTier;
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
	 private final ITPClient client;
	/**
	 * The UI. Used (directly) only for handling errors.
	 */
	 private final TPUI ui;
	/**
	 * Constructor.
	 * @param sock a socket connecting to the server.
	 * @param userInterface the UI to show any errors to the user
	 * @param cli the non-UI client/data model to update in response to messages
	 */
	public ClientFromServerReader(final Socket sock, final TPUI userInterface,
			final ITPClient cli) {
		socket = sock;
		ui = userInterface;
		client = cli;
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
				if (obj instanceof String) {
					handleMessage((String) obj);
				}
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
	private void handleMessage(final String message) {
		try {
			if (message.toLowerCase().startsWith("turn ")) {
				client.endTurn(Integer.parseInt(message.split(" ")[1]));
			} else if (message.toLowerCase().startsWith("oppunit ")) {
				String[] msg = message.split(" ");
				assert msg != null;
				client.addOpposingUnit(
						Point.of(Integer.parseInt(msg[1]),
								Integer.parseInt(msg[2])),
						new ProxyUnit(Integer.parseInt(msg[3]), URLDecoder
								.decode(msg[4], "C"), Integer.parseInt(msg[5]),
								msg[6].charAt(0), URLDecoder
										.decode(msg[7], "C"), HealthTier
										.values()[Integer.parseInt(msg[8])],
								Integer.parseInt(msg[9]), Integer
										.parseInt(msg[10])));
			} else if (message.toLowerCase().startsWith("ownunit ")) {
				String[] msg = message.split(" ");
				assert msg != null;
//				client.
			}
		} catch (NumberFormatException e) {
			ui.showError(
					"Server failed to send a number where we expected one", e);
		} catch (UnsupportedEncodingException e) {
			ui.showError(
					"This computer doesn't support the text encoding we use", e);
		}
	}
}
