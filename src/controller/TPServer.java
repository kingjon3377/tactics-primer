package controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import model.IPoint;
import model.ITileFixture;
import model.TPMap;
import model.TileType;

import common.MapUpdateListener;

/**
 * The main server-logic class, keeping track of the map and executing orders,
 * but not dealing directly with network connections.
 *
 * @author Jonathan Lovelace
 *
 */
public class TPServer {
	/**
	 * The map.
	 */
	private final TPMap map;
	/**
	 * Listeners to notify when we change the map.
	 */
	private final List<MapUpdateListener> listeners = new ArrayList<>();
	/**
	 * The list of player numbers. While ArrayDeque is generally more efficient,
	 * our usage pattern will primarily be removing the head element and
	 * immediately adding it at the tail.
	 */
	private final Queue<Integer> players = new LinkedList<>();
	/**
	 * No-arg constructor. Sets up an empty map.
	 */
	public TPServer() {
		map = new TPMap();
	}
	/**
	 * Constructor taking an already-initialized map.
	 * @param theMap the map
	 */
	public TPServer(final TPMap theMap) {
		map = theMap;
	}
	/**
	 * Constructor taking parameters to build a map.
	 * @param terrain the terrain in the map
	 * @param fixtures the contents of the map
	 */
	public TPServer(final Map<IPoint, TileType> terrain,
			final Map<IPoint, ITileFixture> fixtures) {
		map = new TPMap(terrain, fixtures);
	}
	/**
	 * Add an update listener.
	 * @param listener the listener to add
	 */
	public void addMapUpdateListener(final MapUpdateListener listener) {
		listeners.add(listener);
	}
	/**
	 * Remove an update listener.
	 * @param listener the listener to remove
	 */
	public void removeMapUpdateListener(final MapUpdateListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Add a player. Throws IllegalArgumentException if the player is already in
	 * the game.
	 *
	 * @param player
	 *            the player to add
	 */
	public void addPlayer(final int player) {
		Integer val = Integer.valueOf(player);
		synchronized (players) {
			if (players.contains(val)) {
				throw new IllegalArgumentException("Player already joined");
			}
			players.add(val);
		}
	}
	/**
	 * Remove a player.
	 * @param player the player to remove
	 */
	public void removePlayer(final int player) {
		players.remove(Integer.valueOf(player));
	}

	/**
	 * End the turn, advancing to the next player.
	 *
	 * @param requester
	 *            the player requesting the end of the turn. Only the current
	 *            player may end the turn.
	 */
	public void endTurn(final int requester) {
		if (requester != players.peek().intValue()) {
			throw new IllegalArgumentException("Not your turn");
		}
		final Integer old = players.poll();
		players.add(old);
		final int curr = players.peek().intValue();
		for (final MapUpdateListener listener : listeners) {
			listener.endTurn(curr);
		}
	}
	/**
	 * Move a unit. If the destination is occupied, the caller should use some
	 * other method to clear it first.
	 *
	 * @param requester
	 *            the player giving the order. Only the current player may make
	 *            orders.
	 * @param id
	 *            the ID # of the unit moving
	 * @param source
	 *            the unit's current location
	 * @param dest
	 *            the unit's destination. Must be unoccupied.
	 */
	public void moveUnit(final int requester, final int id,
			final IPoint source, final IPoint dest) {
		final ITileFixture mover = map.getFixture(id);
		final IPoint actualSource = map.findFixture(id);
		if (requester != players.peek().intValue()) {
			throw new IllegalArgumentException("Not your turn");
		} else if (mover == null) {
			throw new IllegalArgumentException("No such unit");
		} else if (mover.getOwner() != requester) {
			throw new IllegalArgumentException("You don't own that unit");
		} else if (!source.equals(actualSource)) {
			throw new IllegalArgumentException(
					"Starting point doesn't match unit's location");
		} else if (map.getContents(dest) != null) {
			// FIXME: What about fixtures (e.g. platforms) that can contain units?
			throw new IllegalArgumentException("Destination isn't empty");
		}
		// Check that the fixture is actually a unit.
		map.removeFixture(source);
		map.setTileContents(dest, mover);
		for (final MapUpdateListener listener : listeners) {
			// FIXME: Don't expose unit details to other players
			listener.fixtureMoved(source, dest, mover);
		}
	}
}
