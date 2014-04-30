package protocol;

import model.IPoint;
import model.SimpleUnit;

/**
 * A message for transmitting a player's own unit.
 * @author kingjon
 *
 */
public class OwnUnitMessage extends RPCMessage {
	/**
	 * Where the unit is.
	 */
	private final IPoint point;
	/**
	 * The ID # of the unit.
	 */
	private final int unitID;
	/**
	 * A description of the unit.
	 */
	private final String unitDesc;
	/**
	 * A character to represent the unit.
	 */
	private final char unitChar;
	/**
	 * The name of an image to represent the unit.
	 */
	private final String unitImage;
	/**
	 * How many HP the unit has. (Both Max and Current.)
	 */
	private final int unitHP;
	/**
	 * The size of die used in melee combat.
	 */
	private final int unitMDie;
	/**
	 * The number of dice used in melee combat.
	 */
	private final int unitMDice;
	/**
	 * The size of die used in ranged combat.
	 */
	private final int unitRDie;
	/**
	 * The number of dice used in ranged combat.
	 */
	private final int unitRDice;
	/**
	 * Who owns the unit.
	 */
	private final int unitOwner;
	/**
	 * Constructor.
	 * @param loc where the unit is
	 * @param unit the unit to transmit
	 */
	public OwnUnitMessage(final IPoint loc, final SimpleUnit unit) {
		point = loc;
		unitID = unit.getID();
		unitDesc = unit.getDescription();
		unitChar = unit.getCharacter();
		unitImage = unit.getImage();
		unitHP = unit.getMaxHP();
		unitMDie = unit.getMeleeDie();
		unitMDice = unit.getTotalAttackDice();
		unitRDie = unit.getRangedDie();
		unitRDice = unit.getTotalRangedAttackDice();
		unitOwner = unit.getOwner();
	}
	/**
	 * @return where the unit is
	 */
	public IPoint getPoint() {
		return point;
	}
	/**
	 * @return the unit transmitted
	 */
	public SimpleUnit getUnit() {
		return new SimpleUnit(unitID, unitOwner, unitDesc, unitChar, unitImage,
				unitHP, unitMDie, unitMDice, unitRDie, unitRDice);
	}
}
