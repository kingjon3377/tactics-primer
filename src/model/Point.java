package model;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jdt.annotation.Nullable;

/**
 * The location of a tile.
 * @author Jonathan Lovelace
 */
public final class Point implements IPoint {
	/**
	 * The row.
	 */
	private final int row;
	/**
	 * The column.
	 */
	private final int column;
	/**
	 * The Point cache.
	 */
	private static final Map<Integer, Point> CACHE = new ConcurrentHashMap<>(100);
	/**
	 * @param theRow the row
	 * @param theCol the column
	 */
	public Point(final int theRow, final int theCol) {
		row = theRow;
		column = theCol;
	}
	/**
	 * @return the row of the tile
	 */
	@Override
	public int getRow() {
		return row;
	}
	/**
	 * @return the column of the tile
	 */
	@Override
	public int getColumn() {
		return column;
	}
	/**
	 * Because we want to guarantee no hash collisions, we use the Cantor
	 * pairing function, which I found on Wikipedia.
	 * @return the hash value for the point
	 */
	@Override
	public int hashCode() {
		return (row + column) * (row + column + 1) / 2 + column;
	}
	/**
	 * @param obj an object
	 * @return whether it's an equal point
	 */
	@Override
	public boolean equals(@Nullable final Object obj) {
		return this == obj || obj instanceof IPoint
				&& row == ((IPoint) obj).getRow()
				&& column == ((IPoint) obj).getColumn();
	}

	/**
	 * Because there are going to be a <em>lot</em> of Points requested from
	 * coordinates, we want to cache them. It doesn't matter much if race
	 * conditons mean a few extra Points get created.
	 *
	 * @param row
	 *            a row
	 * @param col
	 *            a column
	 * @return a Point containing those coordinates
	 */
	public static Point of(final int row, final int col) {
		// We use the same algorithm as hashCode to store them in the cache.
		final Integer hash =
				Integer.valueOf((row + col) * (row + col + 1) / 2 + col);
		if (CACHE.containsKey(hash)) {
			final Point retval = CACHE.get(hash);
			assert retval != null;
			return retval;
		} else {
			final Point retval = new Point(row, col);
			CACHE.put(hash, retval);
			return retval;
		}
	}
}
