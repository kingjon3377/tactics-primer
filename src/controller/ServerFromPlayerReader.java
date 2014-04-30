package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import protocol.AcknowledgedMessage;
import protocol.PlayerPresentMessage;
import protocol.PlayerRequestMessage;
import protocol.ProtocolErrorMessage;
import protocol.RPCMessage;

/**
 * A thread to read input from a client.
 *
 * @author Clem Hasselbach (original, from which this was adapted)
 * @author Jonathan Lovelace (cleanups, docs, adapting to fit needs of this
 *         game)
 *
 */
public class ServerFromPlayerReader extends Thread {
	/**
	 * Logger.
	 */
	private static final Logger LOGGER = Logger
			.getLogger(ServerFromPlayerReader.class.getName());
	/**
	 * The API adapter to hand the client's messages to.
	 */
	private final ServerAPIAdapter api;
	/**
	 * The socket connecting to the client.
	 */
	private final Socket socket;
	/**
	 * Which player we are handling.
	 */
	private int player = -2;
	/**
	 * The output hopper into which to put the output of commands.
	 */
	private final ServerToPlayerWriter out;
	/**
	 * Whether we should continue to read.
	 */
	private boolean shouldContinue = true;

	/**
	 * @param sock
	 *            the socket we are handling
	 * @param adapter
	 *            the API adapter to hand messages to
	 * @param ind
	 *            our index in the table of threads
	 * @param outHopper
	 *            The output hopper into which to put the output of commands
	 */
	public ServerFromPlayerReader(final Socket sock,
			final ServerAPIAdapter adapter, final int ind,
			final ServerToPlayerWriter outHopper) {
		socket = sock;
		api = adapter;
		out = outHopper;
	}

	/**
	 * The main loop of this thread.
	 */
	@Override
	public void run() {
		try (final ObjectInputStream in =
				new ObjectInputStream(socket.getInputStream())) {
			Object input;
			while (shouldContinue && (input = in.readObject()) != null) {
				if (input instanceof RPCMessage) {
					if (player == -2) {
						if (input instanceof PlayerRequestMessage) {
							try {
								api.addPlayer(((PlayerRequestMessage) input)
										.getNumber());
							} catch (IllegalArgumentException e) {
								out.queue(new PlayerPresentMessage(
										((PlayerRequestMessage) input)
												.getNumber()));
								continue;
							}
							player = ((PlayerRequestMessage) input).getNumber();
							out.queue(new AcknowledgedMessage());
						} else {
							out.queue(new ProtocolErrorMessage(
									"Negotiate player number first"));
						}
					} else {
						out.queue(api.process((RPCMessage) input, player));
					}
				} else {
					out.queue(new ProtocolErrorMessage(
							"We onlyaccept RPCMessage inputs"));
				}
			}
		} catch (ClassNotFoundException except) {
			out.queue(new ProtocolErrorMessage(
					"We onlyaccept RPCMessage inputs"));
			LOGGER.log(Level.SEVERE, "Class not found in message from player "
					+ player, except);
		} catch (IOException except) {
			LOGGER.log(Level.SEVERE, "I/O error in dealing with player "
					+ player, except);
		} finally {
			api.removePlayer(player);
		}
	}

	/**
	 * Stop reading after the next request.
	 */
	public void stopReading() {
		shouldContinue = false;
	}
}
