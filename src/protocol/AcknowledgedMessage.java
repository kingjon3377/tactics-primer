package protocol;
/**
 * A message to say "ACK".
 * @author kingjon
 *
 */
public class AcknowledgedMessage extends RPCMessage {
	/**
	 * @return "ACK"
	 */
	@Override
	public String toString() {
		return "ACK";
	}
}
