package protocol;
/**
 * A message to request the full map.
 * @author Jonathan Lovelace
 *
 */
public class FullMapRequestMessage extends RPCMessage {
	/**
	 * The player making the request.
	 */
	private final int player;
	/**
	 * @param playr the player requesting the map.
	 */
	public FullMapRequestMessage(final int playr) {
		player = playr;
	}
	/**
	 * @return the player making the request
	 */
	public int getPlayer() {
		return player;
	}
}
