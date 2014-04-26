package gamenet;

/**
 * Your GUI might want to implement this interface if wants to know when the
 * socket connection is broken. Since this is optional, to register the desire
 * for this information, call the GamePlayer method:
 * setGameConnectionBroken(Interface_ConnectionBroken gcb)
 */
public interface GameNet_ConnectionBrokenInterface {
	/**
	 * Handle broken connection.
	 */
	void gameConnectionBroken();
}