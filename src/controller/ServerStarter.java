package controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import model.IPoint;
import model.ITileFixture;
import model.Point;
import model.TileType;

public class ServerStarter {
	public void startServer(final int port) {
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
		final TPServer server = new TPServer(terrain, fixtures);
		new ServerThread(server, port).start();
	}
	public static void main(final String[] args) {
		new ServerStarter().startServer(Integer.parseInt(args[0]));
	}

}
