package controller;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import model.IPoint;
import model.ITileFixture;
import model.ProxyUnit;
import model.SimpleUnit;
import model.TPMap;
import model.TileType;
import protocol.FixtureMoveMessage;
import view.TPUI;

import common.MapUpdateListener;

/**
 * A client for the game, sans UI.
 * @author Jonathan Lovelace
 *
 */
public class TPClient extends Thread implements ITPClient {
	/**
	 * The player number.
	 */
	private int player = 1;
	/**
	 * Whether we can send non-player-number-negotiation messages.
	 */
	private boolean proceed = false;
	/**
	 * Whether we should continue running at all.
	 */
	private boolean continueFlag = true;
	/**
	 * The socket connecting us to the server.
	 */
	private final Socket socket;
	/**
	 * The reader listening to the server.
	 */
	private final ClientFromServerReader reader;
	/**
	 * The writer writing to the server.
	 */
	private final ClientToServerWriter writer;
	/**
	 * The map.
	 */
	private final TPMap map = new TPMap();
	/**
	 * The set of orders. A map from unit IDs to the location of their target or
	 * destination.
	 */
	private final Map<Integer, IPoint> orders = new ConcurrentHashMap<>();
	/**
	 * Map change listeners.
	 */
	private final List<MapUpdateListener> listeners = new ArrayList<>();
	/**
	 * @param sock the socket connecting us to the server
	 * @param ui the user interface
	 */
	public TPClient(final Socket sock, final TPUI ui) {
		socket = sock;
		reader = new ClientFromServerReader(socket, ui, this);
		writer = new ClientToServerWriter(socket);
	}
	/**
	 * Allow sending any non-player-number-negotiation messages.
	 */
	@Override
	public void setPlayerNumber() {
		proceed = true;
		writer.requestFullMap(player);
	}
	/**
	 * Reject the current proposed player number.
	 */
	@Override
	public void rejectPlayerNumber() {
		if (!proceed) {
			player++;
			writer.proposePlayer(player);
		}
	}
	/**
	 * Mark the end of a turn.
	 * @param playr whose turn it is now
	 */
	@Override
	public void endTurn(final int playr) {
		if (player == playr) {
			orders.clear();
		}
		for (final MapUpdateListener listener : listeners) {
			listener.endTurn(playr);
		}
	}
	/**
	 * Add an opponent's unit.
	 * @param point where to add it
	 * @param unit the unit to add
	 */
	@Override
	public void addOpposingUnit(final IPoint point, final ProxyUnit unit) {
		map.setTileContents(point, unit);
		for (final MapUpdateListener listener : listeners) {
			listener.fixtureAdded(point, unit);
		}
	}
	/**
	 * Add our own unit.
	 * @param point where to add it
	 * @param unit the unit to add
	 */
	@Override
	public void addOwnUnit(final IPoint point, final SimpleUnit unit) {
		map.setTileContents(point, unit);
		for (final MapUpdateListener listener : listeners) {
			listener.fixtureAdded(point, unit);
		}
	}
	/**
	 * @param source where the fixture starts
	 * @param dest where it should end
	 * @param id the fixture's ID
	 */
	@Override
	public void moveFixture(final IPoint source, final IPoint dest, final int id) {
		synchronized (map) {
			ITileFixture mover = map.getContents(source);
			if (mover != null && mover.getID() == id) {
				map.removeFixture(source);
				map.setTileContents(dest, mover);
				for (final MapUpdateListener listener : listeners) {
					listener.fixtureMoved(source, dest, mover);
				}
			} else {
				writer.requestFullMap(player);
			}
		}
	}
	/**
	 * Remove a fixture from the map.
	 * @param point its location
	 * @param id its ID
	 */
	@Override
	public void removeFixture(final IPoint point, final int id) {
		synchronized (map) {
			ITileFixture contents = map.getContents(point);
			if (contents != null && (contents.getID() == id || id == -1)) {
				map.removeFixture(point);
				for (final MapUpdateListener listener : listeners) {
					listener.fixtureRemoved(point, contents);
				}
			} else {
				writer.requestFullMap(player);
			}
		}
	}
	/**
	 * @param point a location
	 * @param type the new tile-type there
	 */
	@Override
	public void changeTerrain(final IPoint point, final TileType type) {
		map.setTerrain(point, type);
		for (final MapUpdateListener listener : listeners) {
			listener.terrainChanged(point, type);
		}
	}
	/**
	 * @param listener a listener to add
	 */
	public void addMapUpdateListener(final MapUpdateListener listener) {
		listeners.add(listener);
	}
	/**
	 * @param listener a listener to remove
	 * @param listener
	 */
	public void removeMapUpdateListener(final MapUpdateListener listener) {
		listeners.remove(listener);
	}
	/**
	 * Set orders for a unit.
	 * @param id the ID of the unit being ordered
	 * @param target where the order affects
	 */
	public void setOrders(final int id, final IPoint target) {
		orders.put(Integer.valueOf(id), target);
	}
	/**
	 * Transmit the orders to the server.
	 */
	public synchronized void transmitOrders() {
		for (final Entry<Integer, IPoint> entry : orders.entrySet()) {
			final IPoint source = map.findFixture(entry.getKey().intValue());
			final ITileFixture actor = map.getFixture(entry.getKey().intValue());
			if (source == null || actor == null) {
				continue;
			}
			final ITileFixture target = map.getContents(entry.getValue());
			if (target == null) {
				writer.queue(new FixtureMoveMessage(source, entry.getValue(),
						entry.getKey().intValue()));
			} else {
				// Attack.
			}
		}
	}

	@Override
	public void run() {
		writer.start();
		reader.start();
		writer.proposePlayer(player);
		while (writer.isAlive() && reader.isAlive()) {
			try {
				writer.join();
			} catch (InterruptedException e) {
				continue;
			}
		}
		try {
			reader.join();
		} catch (InterruptedException e) {
			return;
		}
	}
	@Override
	public void stopThreads() {
		if (writer.isAlive()) {
			writer.stopWriter();
			writer.notify();
		}
		if (reader.isAlive()) {
			reader.stopReading();
			reader.notify();
		}
	}
}
