package controller;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

import view.TPGUI;
import view.TPUIProxy;

public class GUIStarter {

	public static void main(final String[] args) {
		final TPUIProxy proxy = new TPUIProxy();
		TPClient client;
		try {
			client = new TPClient(new Socket(args[0], Integer.parseInt(args[1])),
					proxy);
			final TPGUI gui = new TPGUI(client);
			proxy.setProxied(gui);
			client.start();
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					gui.setVisible(true);
				}
			});
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
