package view;
/**
 * An interface for GUIs and text UIs.
 * @author Jonathan Lovelace
 */
public interface TPUI {
	/**
	 * Show an error message to the user.
	 * @param message the message to display
	 * @param error the exception givinng the details
	 */
	void showError(String message, Throwable error);
}
