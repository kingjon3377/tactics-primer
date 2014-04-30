package model;

/**
 * A simple, concrete Unit implementation for the server and for the client's
 * own units.
 *
 * @author Jonathan Lovelace
 */
public class SimpleUnit implements Unit {
	/**
	 * The ID # of the unit.
	 */
	private final int id;
	/**
	 * A description of the unit.
	 */
	private final String desc;
	/**
	 * A character to represent the unit.
	 */
	private final char character;
	/**
	 * The name of an image to represent the unit.
	 */
	private final String image;
	/**
	 * The owner of the unit.
	 */
	private final int owner;
	/**
	 * The most HP the unit can have.
	 */
	private final int maxHP;
	/**
	 * How many HP the unit currently has.
	 */
	private int currHP;
	/**
	 * What size die to use for melee combat.
	 */
	private final int meleeDie;
	/**
	 * How many dice to use in melee combat.
	 */
	private final int meleeDice;
	/**
	 * What size die to use for ranged combat.
	 */
	private final int rangedDie;
	/**
	 * How many dice to use for ranged combat.
	 */
	private final int rangedDice;
	/**
	 * @param idNum the ID # for the unit
	 * @param description a description of the unit
	 * @param charRepr a character to represent the unit in a text UI
	 * @param img an image to use to represent the unit
	 * @param hitPoints the unit's starting HP
	 * @param mDie the die to use for melee combat
	 * @param mDice how many dice to use in melee combat
	 * @param rDie the die to use in ranged combat
	 * @param rDice how many dice to use in ranged combat
	 * @param player the owner of the unit
	 */
	public SimpleUnit(final int idNum, final int player, final String description,
			final char charRepr, final String img, final int hitPoints,
			final int mDie, final int mDice, final int rDie, final int rDice) {
		id = idNum;
		owner = player;
		desc = description;
		character = charRepr;
		image = img;
		maxHP = hitPoints;
		currHP = hitPoints;
		meleeDice = mDice;
		meleeDie = mDie;
		rangedDice = rDice;
		rangedDie = rDie;
	}
	@Override
	public int getID() {
		return id;
	}

	@Override
	public String getDescription() {
		return desc;
	}

	@Override
	public char getCharacter() {
		return character;
	}

	@Override
	public String getImage() {
		return image;
	}

	@Override
	public int getOwner() {
		return owner;
	}

	@Override
	public int getTotalAttackDice() {
		return meleeDice;
	}

	@Override
	public int getTotalRangedAttackDice() {
		return rangedDice;
	}

	@Override
	public HealthTier getHealthTier() {
		if (currHP <= 0) {
			return HealthTier.Dead;
		} else if (currHP <= maxHP / 4) {
			return HealthTier.Critical;
		} else if (currHP <= maxHP / 2) {
			return HealthTier.Wounded;
		} else if (currHP <= maxHP * 3 / 4) {
			return HealthTier.Bloodied;
		} else {
			return HealthTier.Hale;
		}
	}

}
