package gamenet;

import java.net.UnknownHostException;
import java.util.Scanner;

import org.eclipse.jdt.annotation.NonNullByDefault;

/**
 * An abstract class for setting up a game?
 *
 * @author Clem Hasselbach (original)
 * @author Jonathan Lovelace (cleanups, docs)
 *
 */
@NonNullByDefault
public abstract class GameCreator {
	/**
	 * @return a new initialized "game" object.
	 */
	public abstract GameNetCoreGame createGame();

	/**
	 * Set up a game, using the logic from the subclass.
	 *
	 * @param ui
	 *            the "user interface" object (?)
	 * @throws UnknownHostException
	 *             if the local host cannot be resolved to an address
	 */
	public void enterGame(final GameNetUserInterface ui)
			throws UnknownHostException {

		try (final Scanner keyboard = new Scanner(System.in)) {
			System.out.print("Enter your name:\t");
			final String playerName = keyboard.next();
			assert playerName != null;
			System.out.print("Server side of game? (y/n)\t");
			String str = keyboard.next();

			final GameControl gameControl;
			if (str.charAt(0) == 'y') {
				gameControl = new GameControl(this);
			} else {
				System.out.print("Enter IP address:\t");
				final String ipaddr = keyboard.next();
				System.out.println("Enter port number:");
				final int port = keyboard.nextInt();
				gameControl = new GameControl(this, ipaddr, port);
			}

			// Connect ourselves to the GameControl
			final GamePlayer gamePlayer =
					new GamePlayer(playerName, gameControl, ui);

			ui.startUserInterface(gamePlayer);
		}
	}

}
