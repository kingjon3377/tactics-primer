package protocol;
/**
 * A message to say that we're closing the connection.
 * @author Jonathan Lovelace
 *
 */
public class QuitMessage extends RPCMessage {
	@Override
	public String toString() {
		return "Quit";
	}
}
