package protocol;
/**
 * A message to request the full map.
 * @author Jonathan Lovelace
 *
 */
public class FullMapRequestMessage extends RPCMessage {
	/**
	 * @param playr the player requesting the map.
	 */
	public FullMapRequestMessage(final int playr) {
		super(playr);
	}
}
