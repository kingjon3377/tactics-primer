package gameNet;

import java.util.Scanner;

/**
 * An abstract class for setting up a game?
 * 
 * @author Clem Hasselbach (original)
 * @author Jonathan Lovelace (cleanups, docs)
 * 
 */
public abstract class GameCreator {
	/**
	 * @return a new initialized "game" object.
	 */
	public abstract GameNet_CoreGame createGame();

	/**
	 * Set up a game, using the logic from the subclass.
	 * 
	 * @param ui
	 *            the "user interface" object (?)
	 */
	public void enterGame(final GameNet_UserInterface ui) {
		GameControl gameControl = new GameControl(this);

		try (final Scanner keyboard = new Scanner(System.in)) {
			System.out.print("Enter your name:\t");
			final String playerName = keyboard.next();
			System.out.print("Server side of game? (y/n)\t");
			String str = keyboard.next();

			if (str.charAt(0) == 'y') {
				gameControl.startServer(); // Start a Server GameControl
			} else {
				System.out.print("Enter IP address:\t");
				final String ipaddr = keyboard.next();
				System.out.println("Enter port number:");
				final int port = keyboard.nextInt();
				gameControl.connect_to_server(ipaddr, port);
			}

			// Connect ourselves to the GameControl
			final GamePlayer gamePlayer = new GamePlayer(playerName, gameControl, ui);

			ui.startUserInterface(gamePlayer);
		}
	}

}
