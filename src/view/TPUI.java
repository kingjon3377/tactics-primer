package view;

import org.eclipse.jdt.annotation.Nullable;

/**
 * An interface for GUIs and text UIs.
 * @author Jonathan Lovelace
 */
public interface TPUI {
	/**
	 * Show an error message to the user.
	 * @param message the message to display
	 * @param error the exception giving the details, if any
	 */
	void showError(String message, @Nullable Throwable error);
}
