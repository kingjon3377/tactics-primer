package model;

/**
 * A Unit implementation to expose only these details to the server's clients.
 * Most fields are mutable, to allow for changes, but the ID # and owner are
 * not.
 *
 * @author Jonathan Lovelace
 */
public class ProxyUnit implements Unit {
	/**
	 * How many "dice" of damage the unit does in melee.
	 */
	private int attackDice;
	/**
	 * How many "dice" of damage the unit does in ranged combat.
	 */
	private int rangedDice;
	/**
	 * The tier the unit's health falls into.
	 */
	private HealthTier health;
	/**
	 * The unit's ID #.
	 */
	private final int id;
	/**
	 * A description of the unit.
	 */
	private String desc;
	/**
	 * A character to present the unit in a text UI.
	 */
	private char character;
	/**
	 * The name of an image a GUI should use to represent the unit.
	 */
	private String image;
	/**
	 * The number of the player that owns the unit.
	 */
	private final int owner;

	/**
	 * @param proxied
	 *            The unit being proxied.
	 */
	public ProxyUnit(final Unit proxied) {
		attackDice = proxied.getTotalAttackDice();
		rangedDice = proxied.getTotalRangedAttackDice();
		health = proxied.getHealthTier();
		id = proxied.getID();
		desc = proxied.getDescription();
		character = proxied.getCharacter();
		image = proxied.getImage();
		owner = proxied.getOwner();
	}

	/**
	 * Constructor taking parameters rather than the unit being proxied---useful
	 * on the client side.
	 *
	 * @param idNum
	 *            the ID # of the unit
	 * @param description
	 *            the description of the unit
	 * @param ownerNum
	 *            the owner of the unit
	 * @param charRepr
	 *            the character to represent the unit in a TUI.
	 * @param img
	 *            the name of the image to represent the unit in a GUI.
	 * @param htier
	 *            the health of the unit
	 * @param mDice
	 *            how many dice of damage it will deal in melee combat
	 * @param rDice
	 *            how many dice of damage it will deal in ranged combat
	 */
	public ProxyUnit(final int idNum, final String description,
			final int ownerNum, final char charRepr, final String img,
			final HealthTier htier, final int mDice, final int rDice) {
		id = idNum;
		desc = description;
		owner = ownerNum;
		character = charRepr;
		image = img;
		health = htier;
		attackDice = mDice;
		rangedDice = rDice;
	}

	/**
	 * @return how many "dice" of damage the unit can do in melee combat.
	 */
	@Override
	public int getTotalAttackDice() {
		return attackDice;
	}

	/**
	 * @param dice
	 *            a new value for the number of "dice" of damage the unit can do
	 *            in melee combat.
	 */
	public void setTotalAttackDice(final int dice) {
		attackDice = dice;
	}

	/**
	 * @return how many "dice" of damage the unit can do in ranged combat
	 */
	@Override
	public int getTotalRangedAttackDice() {
		return rangedDice;
	}

	/**
	 * @param dice
	 *            a new value for the number of "dice" of damage the unit can do
	 *            in ranged combat.
	 */
	public void setTotalRangedAttackDice(final int dice) {
		rangedDice = dice;
	}

	/**
	 * @return what "tier" the unit's health falls in
	 */
	@Override
	public HealthTier getHealthTier() {
		return health;
	}

	/**
	 * @param tier
	 *            what "tier" the unit's health now falls in
	 */
	public void setHealthTier(final HealthTier tier) {
		health = tier;
	}

	/**
	 * @return the ID # of the unit
	 */
	@Override
	public int getID() {
		return id;
	}

	/**
	 * @return a description of the unit
	 */
	@Override
	public String getDescription() {
		return desc;
	}

	/**
	 * @param description
	 *            a new description of the unit
	 */
	public void setDescription(final String description) {
		desc = description;
	}

	/**
	 * @return a character to use to represent the unit in a text UI.
	 */
	@Override
	public char getCharacter() {
		return character;
	}

	/**
	 * @param charRepr
	 *            a character to now use to represent the unit in a text UI
	 */
	public void setCharacter(final char charRepr) {
		character = charRepr;
	}

	/**
	 * @return the name of an image to use to represent the unit in a GUI
	 */
	@Override
	public String getImage() {
		return image;
	}

	/**
	 * @param img
	 *            the name of an image to now use to represent the unit in a GUI
	 */
	public void setImage(final String img) {
		image = img;
	}

	/**
	 * @return the number of the player whose unit this is
	 */
	@Override
	public int getOwner() {
		return owner;
	}
}
