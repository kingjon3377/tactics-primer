package common;

import model.IPoint;
import model.ITileFixture;
import model.TileType;

/**
 * An interface for objects that want to be notified when a map is updated. This
 * includes both server threads communicating with clients, so they can notify
 * their clients, and client UIs.
 *
 * @author Jonathan Lovelace
 *
 */
public interface MapUpdateListener {
	/**
	 * If the map being listened to knows unit details, those will be sent for
	 * this player's units and only proxy fixtures for other players'.
	 *
	 * @return what player, if any, this listener is interested in.
	 */
	int getPlayer();

	/**
	 * Handle a change in terrain.
	 *
	 * @param point
	 *            the location of the change
	 * @param type
	 *            the new tile type there
	 */
	void terrainChanged(IPoint point, TileType type);

	/**
	 * Handle the addition of a fixture.
	 *
	 * @param point
	 *            the location of the new fixture
	 * @param fix
	 *            the fixture to add.
	 */
	void fixtureAdded(IPoint point, ITileFixture fix);
	/**
	 * Handle the removal of a fixture.
	 * @param point
	 *            the location of the fixture
	 * @param fix
	 *            the fixture to remove.
	 */
	void fixtureRemoved(IPoint point, ITileFixture fix);
	/**
	 * Handle the movement of a fixture from one location to another.
	 * @param source the starting point
	 * @param dest the destination
	 * @param fix the fixture that is moving
	 */
	void fixtureMoved(IPoint source, IPoint dest, ITileFixture fix);
	/**
	 * Handle the end of a turn.
	 * @param player the number of the player whose turn it is.
	 */
	void endTurn(int player);
}
