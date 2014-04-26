package model;
/**
 * What kind of base terrain tiles can have.
 * @author Jonathan Lovelace
 */
public enum TileType {
	/**
	 * Road. Speeds movement.
	 */
	Road,
	/**
	 * Short grass.
	 */
	Lawn,
	/**
	 * Tall grass or small bushes that impede movement.
	 */
	Brush,
	/**
	 * Any terrain that is impassible but not a barrier (that would be
	 * represented by a fixture of some kind).
	 */
	Impassible;
}
