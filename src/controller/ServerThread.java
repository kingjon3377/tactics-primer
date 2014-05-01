package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A thread to listen for connections and handle the server.
 * @author Jonathan Lovelace
 *
 */
public class ServerThread extends Thread {
	/**
	 * The server.
	 */
	private final TPServer server;
	/**
	 * The port number.
	 */
	private final int port;
	/**
	 * @param serv the server
	 * @param portNum the port number
	 */
	public ServerThread(final TPServer serv, final int portNum) {
		server = serv;
		port = portNum;
	}
	@Override
	public void run() {
		try (ServerSocket socket = new ServerSocket(port)) {
			socket.setReuseAddress(true);
			Socket client;
			while ((client = socket.accept()) != null) {
				System.out.println("Accepting client");
				new ServerClientManager(client, new ServerAPIAdapter(server))
						.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
