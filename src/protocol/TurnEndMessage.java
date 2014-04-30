package protocol;
/**
 * A message indicating a turn has ended.
 * @author Jonathan Lovelace
 *
 */
public class TurnEndMessage extends RPCMessage {
	/**
	 * Whose turn it is next.
	 */
	private final int next;
	/**
	 * @param player whose turn it is next
	 */
	public TurnEndMessage(final int player) {
		next = player;
	}
	/**
	 * @return whose turn it is next
	 */
	public int getNext() {
		return next;
	}
}
