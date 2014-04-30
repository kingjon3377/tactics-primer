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
	 * @param loc where the fixture is locate
	 * @param id its ID #
	 */
	public FixtureRemovalMessage(final IPoint loc, final int id) {
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
