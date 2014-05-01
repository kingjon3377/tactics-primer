package protocol;
/**
 * A message indicating a turn has ended.
 * @author Jonathan Lovelace
 *
 */
public class TurnEndMessage extends RPCMessage {
	/**
	 * In server-to-client: Whose turn it is next.
	 * In client-to-server: whose turn is ending.
	 */
	private final int next;
	/**
	 * @param player the player being communicated with
	 * @param nextP whose turn it is next, or whose turn is ending
	 */
	public TurnEndMessage(final int player, final int nextP) {
		super(player);
		next = nextP;
	}
	/**
	 * @return whose turn it is next or is ending
	 */
	public int getNext() {
		return next;
	}
}
