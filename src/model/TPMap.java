package model;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jdt.annotation.Nullable;
/**
 * The map in which the game takes place.
 * @author Jonathan Lovelace
 *
 */
public class TPMap implements IMap {
	/**
	 * The terrain.
	 */
	private final Map<IPoint, TileType> terrain;
	/**
	 * The non-terrain contents.
	 */
	private final Map<IPoint, ITileFixture> contents;
	/**
	 * No-arg constructor.
	 */
	public TPMap() {
		terrain = new ConcurrentHashMap<>();
		contents = new ConcurrentHashMap<>();
	}
	/**
	 * @param terr the terrain
	 * @param fixtures the fixtures on the map
	 */
	public TPMap(final Map<IPoint, TileType> terr,
			final Map<IPoint, ITileFixture> fixtures) {
		this();
		if (!terr.keySet().containsAll(fixtures.keySet())) {
			throw new IllegalArgumentException("Can't have fixtures on no terrain");
		}
		terrain.putAll(terr);
		contents.putAll(fixtures);
	}
	/**
	 * @return an iteration over the points in the map 
	 */
	@Override
	public Iterator<IPoint> iterator() {
		final Iterator<IPoint> retval = terrain.keySet().iterator();
		assert retval != null;
		return retval;
	}
	/**
	 * Throws IllegalArgumentException if the point is not in the map.
	 * @param point a location in the map
	 * @return the terrain there
	 */
	@Override
	public TileType getTerrain(final IPoint point) {
		if (terrain.containsKey(point)) {
			final TileType retval = terrain.get(point);
			assert retval != null;
			return retval;
		} else {
			throw new IllegalArgumentException("Asked for terrain not in the map");
		}
	}
	/**
	 * @param point a location in the map
	 * @return the fixture there, if any
	 */
	@Nullable
	@Override
	public ITileFixture getContents(final IPoint point) {
		return contents.get(point);
	}
	/**
	 * @param point a location in the map
	 * @param type the new terrain there
	 */
	public void setTerrain(final IPoint point, final TileType type) {
		terrain.put(point, type);
	}
	/**
	 * Note that while getContents() returns null to represent nothing, this
	 * does not take a null argument; use removeFixture().
	 * 
	 * @param point
	 *            a location in the map
	 * @param fix
	 *            a new fixture to put there
	 */
	public void setTileContents(final IPoint point, final ITileFixture fix) {
		contents.put(point, fix);
	}
	/**
	 * Remove the fixture at the specified point.
	 * @param point the location in question
	 */
	public void removeFixture(final IPoint point) {
		contents.remove(point);
	}
}
