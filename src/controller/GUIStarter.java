package controller;

import gamenet.GameNetUserInterface;
import gamenet.GamePlayer;

import org.eclipse.jdt.annotation.Nullable;

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
import view.TPGUI;
import view.TPUI;

public class GUIStarter implements GameNetUserInterface {
	private final ITPClient client = new TPClient();
	@Nullable
	private GamePlayer serverConnection;
	@Nullable
	private TPUI ui;
	@Override
	public void receivedMessage(@Nullable final Object ob) {
		if (ob instanceof RPCMessage) {
			RPCMessage message = (RPCMessage) ob;
			if (message instanceof TurnEndMessage) {
				client.endTurn(((TurnEndMessage) message).getPlayer());
				System.out.println("End turn");
			} else if (message instanceof OpposingUnitMessage) {
				client.addOpposingUnit(((OpposingUnitMessage) message).getPoint(),
						((OpposingUnitMessage) message).getUnit());
				System.out.println("Opposing unit");
			} else if (message instanceof OwnUnitMessage) {
				client.addOwnUnit(((OwnUnitMessage) message).getPoint(),
						((OwnUnitMessage) message).getUnit());
				System.out.println("Own unit");
			} else if (message instanceof AcknowledgedMessage) {
				client.setPlayerNumber();
				System.out.println("ACK");
			} else if (message instanceof FixtureMoveMessage) {
				client.moveFixture(((FixtureMoveMessage) message).getSource(),
						((FixtureMoveMessage) message).getDest(),
						((FixtureMoveMessage) message).getMover());
				System.out.println("Fixture move");
			} else if (message instanceof FixtureRemovalMessage) {
				client.removeFixture(((FixtureRemovalMessage) message).getPoint(),
						((FixtureRemovalMessage) message).getID());
				System.out.println("Fixture remove");
			} else if (message instanceof PlayerPresentMessage) {
				client.rejectPlayerNumber();
				System.out.println("NACK");
			} else if (message instanceof TerrainChangeMessage) {
				client.changeTerrain(((TerrainChangeMessage) message).getPoint(),
						((TerrainChangeMessage) message).getType());
				System.out.println("Terrain change");
			} else if (message instanceof ProtocolErrorMessage) {
				showError("Server sent us an error message", new Throwable(
						((ProtocolErrorMessage) message).getMessage()));
				System.out.println("Error");
			} else {
					showError(
							"Server sent a message we don't know how to handle",
							null);
			}
		}
	}

	private void showError(final String message, @Nullable final Throwable error) {
		final TPUI localUI = ui;
		if (localUI == null) {
			System.err.println(message);
		} else {
			localUI.showError(message, error);
		}
	}

	@Override
	public void startUserInterface(@Nullable final GamePlayer player) {
		serverConnection = player;
		if (player != null) {
			client.setServerConnection(player);
			ui = new TPGUI(client);
			((TPGUI) ui).setVisible(true);
		}
	}

}
