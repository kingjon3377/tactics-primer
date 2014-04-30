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
	 * Do not instantiate directly.
	 */
	protected RPCMessage() {
		// Do not instantiate.
	}
}
