package controller;

import java.io.IOException;
import java.net.Socket;

/**
 * A class for the server to handle communications with a client. It delegates
 * reading from and writing to the client.
 *
 * @author Jonathan Lovelace
 */
public class ServerClientManager extends Thread {
	/**
	 * The socket connecting us to the client.
	 */
	private final Socket socket;
	/**
	 * The player number.
	 */
	private final int player;
	/**
	 * The API adapter to turn RPC calls into API calls.
	 */
	private final ServerAPIAdapter api;
	/**
	 * Constructor.
	 * @param sock the socket connecting to the client
	 * @param index the player number
	 * @param adapter the API adapter
	 */
	public ServerClientManager(final Socket sock, final int index,
			final ServerAPIAdapter adapter) {
		socket = sock;
		player = index;
		api = adapter;
	}
	/**
	 * The main loop of this thread.
	 */
	@Override
	public void run() {
		final ServerToPlayerWriter writer =
				new ServerToPlayerWriter(socket, player);
		final ServerFromPlayerReader reader =
				new ServerFromPlayerReader(socket, api, player, writer);
		writer.start();
		reader.start();
		while (writer.isAlive() && reader.isAlive()) {
			try {
				writer.join();
			} catch (InterruptedException e) {
				continue;
			}
		}
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
