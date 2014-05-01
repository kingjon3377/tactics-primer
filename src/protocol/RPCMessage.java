package protocol;

import java.io.Serializable;

/**
 * A base class for messages between server and client.
 * @author Jonathan Lovelace
 *
 */
public class RPCMessage implements Serializable {
	/**
	 * Version UID for serialization.
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * The player being communicated with, or the player number being negotiated.
	 */
	private final int player;

	/**
	 * @return the player being communicated with, or the player number being
	 *         negotiated.
	 */
	public final int getPlayer() {
		return player;
	}

	/**
	 * Do not instantiate directly.
	 *
	 * @param playr
	 *            the player being communicated with, or the player number being
	 *            negotiated.
	 */
	protected RPCMessage(final int playr) {
		player = playr;
	}
}
