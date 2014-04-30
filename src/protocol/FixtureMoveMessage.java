package protocol;

import model.IPoint;

/**
 * A message indicating a fixture has moved.
 * @author Jonathan Lovelace
 *
 */
public class FixtureMoveMessage extends RPCMessage {
	/**
	 * The source tile.
	 */
	private final IPoint source;
	/**
	 * The destination tile.
	 */
	private final IPoint dest;
	/**
	 * The ID of the fixture that moved.
	 */
	private final int fixID;
	/**
	 * @param src the source location
	 * @param dst the destination location
	 * @param id the mover's ID
	 */
	public FixtureMoveMessage(final IPoint src, final IPoint dst, final int id) {
		source = src;
		dest = dst;
		fixID = id;
	}
	/**
	 * @return the source location
	 */
	public IPoint getSource() {
		return source;
	}
	/**
	 * @return the destination
	 */
	public IPoint getDest() {
		return dest;
	}
	/**
	 * @return the ID of the mover
	 */
	public int getMover() {
		return fixID;
	}
}
