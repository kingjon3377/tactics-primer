package protocol;
/**
 * A message to say that we're closing the connection.
 * @author Jonathan Lovelace
 *
 */
public class QuitMessage extends RPCMessage {
	/**
	 * @param player the player being communicated with
	 */
	public QuitMessage(final int player) {
		super(player);
	}
}
