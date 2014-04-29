package controller;

import model.Point;

/**
 * An adapter to turn clients' RPC language into API calls to the server object.
 *
 * @author Jonathan Lovelace
 *
 */
public class ServerClientAdapter {
	/**
	 * The server we wrap.
	 */
	private final TPServer server;

	/**
	 * Constructor.
	 *
	 * @param serv
	 *            the server to wrap
	 */
	public ServerClientAdapter(final TPServer serv) {
		server = serv;
	}

	/**
	 * Process a command string.
	 *
	 * @param cmd
	 *            the command string received from the client
	 * @param player
	 *            the player who sent the command
	 * @return the result to send back to the client
	 */
	public String process(final String cmd, final int player) {
		final String[] command = cmd.split(" ");
		if (command == null || command.length < 1) {
			return "INVALID";
		}
		try {
			switch (command[0].toLowerCase()) {
			case "end":
				server.endTurn(player);
				return "ACK";
			case "move":
				server.moveUnit(
						player,
						Integer.parseInt(command[1]),
						Point.of(Integer.parseInt(command[2]),
								Integer.parseInt(command[3])),
						Point.of(Integer.parseInt(command[4]),
								Integer.parseInt(command[5])));
				return "ACK";
			default:
				return "UNKNOWN";
			}
		} catch (NumberFormatException except) {
			return "MALFORMED " + except.getMessage();
		} catch (IllegalArgumentException except) {
			return "ERROR " + except.getMessage();
		}
	}
}