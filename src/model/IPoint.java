package model;

/**
 * An interface for the location of a tile.
 * @author Jonathan Lovelace
 *
 */
public interface IPoint {

	/**
	 * @return the row of the tile
	 */
	int getRow();

	/**
	 * @return the column of the tile
	 */
	int getColumn();

}