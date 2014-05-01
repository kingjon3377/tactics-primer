package view;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import model.IPoint;
import model.ITileFixture;
import model.Point;
import model.TileType;

import common.MapUpdateListener;
/**
 * A GUI map.
 * @author Jonathan Lovelace
 *
 */
public class GUIMap extends JPanel implements MapUpdateListener {
	/**
	 * The player we are a GUI for.
	 */
	private int player;
	/**
	 * The visible tiles.
	 */
	private Map<IPoint, GUITile> tiles = new HashMap<>();
	/**
	 * @param listener a listener to listen for events on the tiles
	 */
	public GUIMap(final MouseInputListener listener) {
		setLayout(new GridLayout(10, 10));
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				final IPoint point = Point.of(i, j);
				final GUITile tile = new GUITile(point);
				tiles.put(point, tile);
				add(tile);
				tile.addMouseListener(listener);
				tile.addMouseMotionListener(listener);
			}
		}
		setMinimumSize(new Dimension(320, 320));
		setPreferredSize(new Dimension(320, 320));
	}
	/**
	 * @return the player we handle
	 */
	@Override
	public int getPlayer() {
		return player;
	}
	/**
	 * @param point where the terrain changed
	 * @param type what it changed to
	 */
	@Override
	public void terrainChanged(final IPoint point, final TileType type) {
		tiles.get(point).setType(type);
	}
	/**
	 * @param point where a fixture is added
	 * @param fix the fixture to add there
	 */
	@Override
	public void fixtureAdded(final IPoint point, final ITileFixture fix) {
		tiles.get(point).setFixture(fix);
	}

	@Override
	public void fixtureRemoved(final IPoint point, final ITileFixture fix) {
		tiles.get(point).removeFixture();
	}

	@Override
	public void fixtureMoved(final IPoint source, final IPoint dest,
			final ITileFixture fix) {
		fixtureRemoved(source, fix);
		fixtureAdded(dest, fix);
	}
	/**
	 * Handled elsewhere.
	 * @param playr ignored
	 */
	@Override
	public void endTurn(final int playr) {
		// Ignored.
	}
}
