package protocol;

import model.IPoint;
import model.ProxyUnit;
import model.Unit.HealthTier;

/**
 * A message to transmit an opponent's unit being added.
 * @author Jonathan Lovelace
 *
 */
public class OpposingUnitMessage extends RPCMessage {
	/**
	 * Version UID for serialization.
	 */
	private static final long serialVersionUID = 2L;
	/**
	 * The location of the unit.
	 */
	private final IPoint point;
	/**
	 * The ID # of the unit.
	 */
	private final int unitID;
	/**
	 * The description of the unit.
	 */
	private final String unitDesc;
	/**
	 * The owner of the unit.
	 */
	private final int unitOwner;
	/**
	 * The character to represent the unit.
	 */
	private final char unitChar;
	/**
	 * The name of an image to represent the unit.
	 */
	private final String unitImage;
	/**
	 * The unit's health tier.
	 */
	private final HealthTier unitHealth;
	/**
	 * The unit's melee dice.
	 */
	private final int unitMDice;
	/**
	 * The unit's ranged dice.
	 */
	private final int unitRDice;
	/**
	 * @return the unit's location
	 */
	public IPoint getPoint() {
		return point;
	}
	/**
	 * @return the unit
	 */
	public ProxyUnit getUnit() {
		return new ProxyUnit(unitID, unitDesc, unitOwner, unitChar, unitImage,
				unitHealth, unitMDice, unitRDice);
	}
	/**
	 * Constructor.
	 * @param player the player being communicated with
	 * @param unit the unit being transmitted
	 * @param loc where the unit is located
	 */
	public OpposingUnitMessage(final int player, final IPoint loc,
			final ProxyUnit unit) {
		super(player);
		point = loc;
		unitID = unit.getID();
		unitDesc = unit.getDescription();
		unitOwner = unit.getOwner();
		unitChar = unit.getCharacter();
		unitImage = unit.getImage();
		unitHealth = unit.getHealthTier();
		unitMDice = unit.getTotalAttackDice();
		unitRDice = unit.getTotalRangedAttackDice();
	}
}
