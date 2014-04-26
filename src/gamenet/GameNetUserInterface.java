package gamenet;

/**
 * Your GUI will likely want to implement this interface to ever receive updates
 * from the GameControl class. A call will be made to the receivedMessage
 * whenever a new GameOutputObj is being sent to your GUI.
 * @author Clem Hasselbach (original)
 * @author Jonathan Lovelace (cleanups, method docs)
 */
public interface GameNetUserInterface {
	/**
	 * Handle a message from the server.
	 * @param ob the message
	 */
	void receivedMessage(Object ob);
	/**
	 * Start the UI running?
	 * @param player the socket manager
	 */
	void startUserInterface(GamePlayer player);
}