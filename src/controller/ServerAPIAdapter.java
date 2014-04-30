package controller;

import java.util.ArrayList;
import java.util.List;

import model.IMap;
import model.IPoint;
import model.ITileFixture;
import model.ProxyUnit;
import model.SimpleUnit;
import protocol.AcknowledgedMessage;
import protocol.ClientFixtureMessage;
import protocol.FixtureMoveMessage;
import protocol.FixtureRemovalMessage;
import protocol.FullMapRequestMessage;
import protocol.MultiMessageMessage;
import protocol.OpposingUnitMessage;
import protocol.OwnUnitMessage;
import protocol.ProtocolErrorMessage;
import protocol.RPCMessage;
import protocol.TerrainChangeMessage;
import protocol.TurnEndMessage;

import common.MapUpdateListener;

/**
 * An adapter to turn clients' RPC language into API calls to the server object.
 *
 * @author Jonathan Lovelace
 *
 */
public class ServerAPIAdapter {
	/**
	 * The server we wrap.
	 */
	private final TPServer server;

	/**
	 * Constructor.
	 *
	 * @param serv
	 *            the server to wrap
	 */
	public ServerAPIAdapter(final TPServer serv) {
		server = serv;
	}

	/**
	 * Process a command string.
	 *
	 * @param cmd
	 *            the command object received from the client
	 * @param player
	 *            the player who sent the command
	 * @return the result to send back to the client
	 */
	public RPCMessage process(final RPCMessage cmd, final int player) {
		try {
			if (cmd instanceof TurnEndMessage) {
				server.endTurn(player);
				return new AcknowledgedMessage();
			} else if (cmd instanceof FixtureMoveMessage) {
				server.moveUnit(player, ((FixtureMoveMessage) cmd).getMover(),
						((FixtureMoveMessage) cmd).getSource(),
						((FixtureMoveMessage) cmd).getDest());
				return new AcknowledgedMessage();
			} else if (cmd instanceof ClientFixtureMessage) {
				switch (((ClientFixtureMessage) cmd).getType()) {
				case Archer:
					server.addFixture(player, ((ClientFixtureMessage) cmd)
							.getPoint(),
							new SimpleUnit(IDFactory.FACTORY.createID(),
									player, "archer", 'a', "archer.png", 10, 3,
									1, 6, 2));
					return new AcknowledgedMessage();
				case Swordsman:
					server.addFixture(player, ((ClientFixtureMessage) cmd)
							.getPoint(),
							new SimpleUnit(IDFactory.FACTORY.createID(),
									player, "swordsman", '/', "swordsman.png",
									12, 8, 2, 0, 0));
					return new AcknowledgedMessage();
				default:
					return new ProtocolErrorMessage("Unknown fixture type");
				}
			} else if (cmd instanceof FullMapRequestMessage) {
				final IMap map =
						server.getPlayerMap(((FullMapRequestMessage) cmd)
								.getPlayer());
				final List<RPCMessage> messages = new ArrayList<>();
				for (final IPoint point : map) {
					messages.add(new TerrainChangeMessage(point, map
							.getTerrain(point)));
					ITileFixture fix = map.getContents(point);
					if (fix == null) {
						messages.add(new FixtureRemovalMessage(point, -1));
					} else if (fix instanceof ProxyUnit) {
						messages.add(new OpposingUnitMessage(point,
								(ProxyUnit) fix));
					} else if (fix instanceof SimpleUnit) {
						messages.add(new OwnUnitMessage(point, (SimpleUnit) fix));
					} else {
						messages.add(new ProtocolErrorMessage(
								"Map contains untransmitted fixture"));
					}
				}
				return new MultiMessageMessage(messages);
			} else {
				return new ProtocolErrorMessage("Unknown command");
			}
		} catch (IllegalArgumentException except) {
			return new ProtocolErrorMessage(except.getMessage());
		}
	}

	/**
	 * Add a listener to the server.
	 *
	 * @param listener
	 *            the listener to add
	 */
	public void addMapUpdateListener(final MapUpdateListener listener) {
		server.addMapUpdateListener(listener);
	}

	/**
	 * Remove a listener from the server.
	 *
	 * @param listener
	 *            the listener to remove
	 */
	public void removeMapUpdateListener(final MapUpdateListener listener) {
		server.removeMapUpdateListener(listener);
	}

	/**
	 * Add a player to the map.
	 *
	 * @param player
	 *            the player to add
	 */
	public void addPlayer(final int player) {
		server.addPlayer(player);
	}

	/**
	 * Remove a player from the map.
	 *
	 * @param player
	 *            the player to remove
	 */
	public void removePlayer(final int player) {
		server.removePlayer(player);
	}
}
