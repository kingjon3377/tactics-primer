package protocol;

/**
 * A message indicating the client sent something in violation of our RPC
 * protocol.
 *
 * @author Jonathan Lovelace
 *
 */
public class ProtocolErrorMessage extends RPCMessage {
	/**
	 * An explanation of the error.
	 */
	private final String message;
	/**
	 * @param player the player being communicated with
	 * @param msg an explanation of the error
	 */
	public ProtocolErrorMessage(final int player, final String msg) {
		super(player);
		message = msg;
	}
	/**
	 * @return an explanation of the error
	 */
	public String getMessage() {
		return message;
	}
}
