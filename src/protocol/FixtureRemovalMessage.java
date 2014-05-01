package protocol;

import model.IPoint;

/**
 * A message indicating that a fixture needs to be removed.
 * @author Jonathan Lovelace
 *
 */
public class FixtureRemovalMessage extends RPCMessage {
	/**
	 * Where the fixture is located.
	 */
	private final IPoint point;
	/**
	 * The ID # of the fixture to remove.
	 */
	private final int fixID;
	/**
	 * @param player the player being communicated with
	 * @param loc where the fixture is located
	 * @param id its ID #
	 */
	public FixtureRemovalMessage(final int player, final IPoint loc,
			final int id) {
		super(player);
		point = loc;
		fixID = id;
	}
	/**
	 * @return where the fixture is located
	 */
	public IPoint getPoint() {
		return point;
	}
	/**
	 * @return its ID
	 */
	public int getID() {
		return fixID;
	}
}
