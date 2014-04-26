package model;
/**
 * An interface for things that can be on a tile.
 * @author Jonathan Lovelace
 */
public interface ITileFixture {
	/**
	 * @return a unique ID for the fixture. If negative, indicates a client-side
	 *         fixture that has been sent to but not confirmed from the server.
	 */
	int getID();
	/**
	 * @return a short description of the fixture.
	 */
	String getDescription();
	/**
	 * @return a character to represent the fixture in a terminal-window view.
	 */
	char getCharacter();
	/**
	 * @return the name of an image a GUI should use to represent the fixture.
	 * 
	 * This is model-view mixing, but the best solution I've found for this.
	 */
	String getImage();
	/**
	 * @return the number of the player who owns the fixture. Anything that is
	 *         not owned by a player, such as trees, is "owned" by "player -1"
	 */
	int getOwner();
}
