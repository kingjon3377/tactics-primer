package controller;

import gamenet.GameCreator;
import gamenet.GameNetCoreGame;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import model.IPoint;
import model.ITileFixture;
import model.Point;
import model.TileType;

public class ServerStarter extends GameCreator {
	public static void main(final String[] args) {
		try {
			// XXX: The server SHOULD NOT know about the GUI!
			new ServerStarter().enterGame(new GUIStarter());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public GameNetCoreGame createGame() {
		Map<IPoint, TileType> terrain = new HashMap<>();
		Map<IPoint, ITileFixture> fixtures = new HashMap<>();
		final int maxType = TileType.values().length;
		final Random random = new Random();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				terrain.put(Point.of(i, j),
						TileType.values()[random.nextInt(maxType)]);
			}
		}
		return new ServerAPIAdapter(new TPServer(terrain, fixtures));
	}

}
