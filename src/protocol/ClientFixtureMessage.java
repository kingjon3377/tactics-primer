package protocol;

import model.IPoint;

/**
 * A message asking the server to add a fixture.
 * @author Jonathan Lovelace
 *
 */
public class ClientFixtureMessage extends RPCMessage {
	/**
	 * The kinds of fixtures we know how to add.
	 */
	public static enum FixtureType {
		/**
		 * A swordsman. Basic melee unit.
		 */
		Swordsman,
		/**
		 * An archer. Basic ranged unit.
		 */
		Archer;
	}
	/**
	 * What kind of fixture to add.
	 */
	private final FixtureType type;
	/**
	 * Where to add it.
	 */
	private final IPoint point;
	/**
	 * @param fixType what kind of fixture to add.
	 * @param player the player being communicated with
	 * @param loc where to add it
	 */
	public ClientFixtureMessage(final int player, final FixtureType fixType,
			final IPoint loc) {
		super(player);
		type = fixType;
		point = loc;
	}
	/**
	 * @return what kind of fixture to add
	 */
	public FixtureType getType() {
		return type;
	}
	/**
	 * @return where to add it
	 */
	public IPoint getPoint() {
		return point;
	}
}
