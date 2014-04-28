package model;

/**
 * An abstract base class for units.
 *
 * @author Jonathan Lovelace
 */
public interface Unit extends ITileFixture {
	/**
	 * How many "dice" of damage the unit's melee attack can do---a simple measure of
	 * strength. But not a complete measure, since a unit might use anything
	 * from a "d2" (coin flip) to a "d20" as its base die.
	 * @return the number of dice
	 */
	int getTotalAttackDice();
	/**
	 * @return How many "dice" of damage the unit's ranged attack can do.
	 */
	int getTotalRangedAttackDice();
	/**
	 * The possible "health tiers" into which a unit's health might fall.
	 */
	public static enum HealthTier {
		/**
		 * Essentially full health.
		 */
		Hale,
		/**
		 * Somewhat wounded, but not too much so.
		 */
		Bloodied,
		/**
		 * More wounded.
		 */
		Wounded,
		/**
		 * Near death.
		 */
		Critical,
		/**
		 * Dead. Once a unit hits this state it should be removed.
		 */
		Dead;
	}
	/**
	 * @return what tier the unit's health falls into
	 */
	HealthTier getHealthTier();
}
