package protocol;
/**
 * A message asking to reserve a player number.
 * @author Jonathan Lovelace
 *
 */
public class PlayerRequestMessage extends RPCMessage {
	/**
	 * The number being requested.
	 */
	private final int number;
	/**
	 * @param num the number being requested
	 */
	public PlayerRequestMessage(final int num) {
		number = num;
	}
	/**
	 * @return the number being requested
	 */
	public int getNumber() {
		return number;
	}
}
