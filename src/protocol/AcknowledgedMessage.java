package protocol;
/**
 * A message to say "ACK".
 * @author kingjon
 *
 */
public class AcknowledgedMessage extends RPCMessage {
	/**
	 * @param player the player being communicated with
	 */
	public AcknowledgedMessage(final int player) {
		super(player);
	}
}
