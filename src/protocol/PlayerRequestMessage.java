package protocol;
/**
 * A message asking to reserve a player number.
 * @author Jonathan Lovelace
 *
 */
public class PlayerRequestMessage extends RPCMessage {
	/**
	 * @param num the number being requested
	 */
	public PlayerRequestMessage(final int num) {
		super(num);
	}
}
