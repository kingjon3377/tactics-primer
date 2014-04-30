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
	private final int player;
	/**
	 * @param playr whose turn it is next, or whose turn is ending
	 */
	public TurnEndMessage(final int playr) {
		player = playr;
	}
	/**
	 * @return whose turn it is next or is ending
	 */
	public int getPlayer() {
		return player;
	}
}
