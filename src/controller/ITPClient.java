package controller;

import model.IPoint;
import model.ProxyUnit;
import model.SimpleUnit;

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
	 * Move
	 */
}