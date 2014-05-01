package controller;

import gamenet.GamePlayer;
import model.IPoint;
import model.ProxyUnit;
import model.SimpleUnit;
import model.TileType;

/**
 * An interface for the non-UI part of the client.
 * @author Jonathan Lovelace
 *
 */
public interface ITPClient {
	/**
	 * Set the player number to the current candidate.
	 */
	void setPlayerNumber();
	/**
	 * Reject the current player-number candidate. (It's already in use.)
	 */
	void rejectPlayerNumber();
	/**
	 * Handle the end of a turn.
	 * @param player whose turn it is now
	 */
	void endTurn(int player);
	/**
	 * Add an opponent's unit to the map.
	 * @param point where the unit is located
	 * @param unit the unit to add
	 */
	void addOpposingUnit(IPoint point, ProxyUnit unit);
	/**
	 * Add one of our own units to the map.
	 * @param point where the unit is located
	 * @param unit the unit to add
	 */
	void addOwnUnit(IPoint point, SimpleUnit unit);
	/**
	 * Move a fixture.
	 * @param source where the unit was located
	 * @param dest where it is now located
	 * @param id the ID of the unit
	 */
	void moveFixture(IPoint source, IPoint dest, int id);
	/**
	 * Remove a fixture.
	 * @param point where it is located
	 * @param id its ID
	 */
	void removeFixture(IPoint point, int id);
	/**
	 * Change the tile type somewhere.
	 * @param point where to change it
	 * @param type what to change it to
	 */
	void changeTerrain(IPoint point, TileType type);
	public abstract void stopThreads();
	public void setServerConnection(GamePlayer conn);
}