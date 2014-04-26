package model;

import org.eclipse.jdt.annotation.Nullable;

/**
 * An interface for a game-board map.
 * 
 * @author Jonathan Lovelace
 */
public interface IMap extends Iterable<IPoint> {
	/**
	 * @param point a point on the map
	 * @return the terrain there
	 */
	TileType getTerrain(Point point);
	/**
	 * @param point a point on the map
	 * @return the fixture there, if any
	 */
	@Nullable
	ITileFixture getContents(Point point);
}
