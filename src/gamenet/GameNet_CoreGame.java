package gamenet;

/**
 * The abstract class defining the interface for the game server. This really
 * ought to be an interface, not an abstract class ...
 * 
 * @author Clem Hasselbach (original)
 * @author Jonathan Lovelace (cleanups and docs)
 * 
 */
public abstract class GameNet_CoreGame {
	/**
	 * Start the game.
	 * @param control the parameters for the server
	 */
	public void startGame(@SuppressWarnings("unused") final GameControl control) {
		// Empty default implementation.
	}
	/**
	 * Handle input from a client.
	 * @param request An object encapsulating the client request
	 * @return the response to return to the client
	 */
	public abstract Object process(Object request);
}
