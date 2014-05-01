package view;

import org.eclipse.jdt.annotation.Nullable;

/**
 * We need a TPUI to pass to the client, and the TPUI may need a reference to
 * the client. So we use a delayed-initialization proxy.
 *
 * @author Jonathan Lovelace
 *
 */
public class TPUIProxy implements TPUI {
	@Nullable
	private TPUI proxied;
	public void setProxied(final TPUI ui) {
		proxied = ui;
	}
	@Override
	public void showError(final String message, @Nullable final Throwable error) {
		if (proxied != null) {
			proxied.showError(message, error);
		}
	}

}
