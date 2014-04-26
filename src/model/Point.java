package model;
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
}
