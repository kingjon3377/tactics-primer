package protocol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * A message multiplexing other messages. It should not actually be sent over the wire.
 * @author Jonathan Lovelace
 *
 */
public class MultiMessageMessage extends RPCMessage implements Iterable<RPCMessage> {
	/**
	 * The messages.
	 */
	private final List<RPCMessage> list = new ArrayList<>();
	/**
	 * Constructor.
	 * @param messages the messages to transmit
	 */
	public MultiMessageMessage(final Collection<RPCMessage> messages) {
		list.addAll(messages);
	}
	/**
	 * @return The messages.
	 */
	@Override
	public Iterator<RPCMessage> iterator() {
		final Iterator<RPCMessage> retval = list.iterator();
		assert retval != null;
		return retval;
	}
}
