package protocol;
/**
 * A message indicating that the proposed player number is already in use.
 * @author Jonathan Lovelace
 *
 */
public class PlayerPresentMessage extends RPCMessage {
	/**
	 * The player number in question.
	 */
	private final int number;
	/**
	 * @param player the player number in question
	 */
	public PlayerPresentMessage(final int player) {
		number = player;
	}
	/**
	 * @return the player number in question
	 */
	public int getNumber() {
		return number;
	}
}
