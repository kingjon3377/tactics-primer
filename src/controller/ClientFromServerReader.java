package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import protocol.AcknowledgedMessage;
import protocol.FixtureMoveMessage;
import protocol.FixtureRemovalMessage;
import protocol.OpposingUnitMessage;
import protocol.OwnUnitMessage;
import protocol.PlayerPresentMessage;
import protocol.ProtocolErrorMessage;
import protocol.RPCMessage;
import protocol.TerrainChangeMessage;
import protocol.TurnEndMessage;
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
				if (obj instanceof RPCMessage) {
					handleMessage((RPCMessage) obj);
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
	private void handleMessage(final RPCMessage message) {
		if (message instanceof TurnEndMessage) {
			client.endTurn(((TurnEndMessage) message).getPlayer());
		} else if (message instanceof OpposingUnitMessage) {
			client.addOpposingUnit(((OpposingUnitMessage) message).getPoint(),
					((OpposingUnitMessage) message).getUnit());
		} else if (message instanceof OwnUnitMessage) {
			client.addOwnUnit(((OwnUnitMessage) message).getPoint(),
					((OwnUnitMessage) message).getUnit());
		} else if (message instanceof AcknowledgedMessage) {
			client.setPlayerNumber();
		} else if (message instanceof FixtureMoveMessage) {
			client.moveFixture(((FixtureMoveMessage) message).getSource(),
					((FixtureMoveMessage) message).getDest(),
					((FixtureMoveMessage) message).getMover());
		} else if (message instanceof FixtureRemovalMessage) {
			client.removeFixture(((FixtureRemovalMessage) message).getPoint(),
					((FixtureRemovalMessage) message).getID());
		} else if (message instanceof PlayerPresentMessage) {
			client.rejectPlayerNumber();
		} else if (message instanceof TerrainChangeMessage) {
			client.changeTerrain(((TerrainChangeMessage) message).getPoint(),
					((TerrainChangeMessage) message).getType());
		} else if (message instanceof ProtocolErrorMessage) {
			ui.showError("Server sent us an error message", new Throwable(
					((ProtocolErrorMessage) message).getMessage()));
		} else {
			ui.showError("Server sent a message we don't know how to handle", null);
		}
	}
	/**
	 * Stop reading.
	 */
	public void stopReading() {
		continueFlag = false;
	}
}
