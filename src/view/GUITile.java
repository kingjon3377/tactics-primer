package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.swing.JComponent;

import model.IPoint;
import model.ITileFixture;
import model.TileType;

import org.eclipse.jdt.annotation.Nullable;

import common.ImageLoader;
/**
 * A GUI for a single tile.
 * @author Jonathan Lovelace
 *
 */
public class GUITile extends JComponent {
	/**
	 * The tile type we represent.
	 */
	private TileType type = TileType.Impassible;
	/**
	 * The fixture on the tile, if any.
	 */
	@Nullable
	private ITileFixture fixture;
	/**
	 * The point we represent.
	 */
	private final IPoint point;
	/**
	 * @param loc which location we represent
	 */
	public GUITile(final IPoint loc) {
		point = loc;
		setMinimumSize(new Dimension(32, 32));
		setPreferredSize(new Dimension(32, 32));
	}
	/**
	 * @return the location we represent
	 */
	public IPoint getPoint() {
		return point;
	}
	/**
	 * @return the tile type we are
	 */
	public TileType getType() {
		return type;
	}
	/**
	 * @return the fixture here
	 */
	@Nullable
	public ITileFixture getFixture() {
		return fixture;
	}
	/**
	 * @param ttype the new type
	 */
	public void setType(final TileType ttype) {
		type = ttype;
	}
	/**
	 * @param fix the new fixture here
	 */
	public void setFixture(final ITileFixture fix) {
		fixture = fix;
	}
	/**
	 * Remove the fixture.
	 */
	public void removeFixture() {
		fixture = null;
	}
	@Override
	public void paint(@Nullable final Graphics g) {
		if (g == null) {
			return;
		}
		super.paint(g);
		switch (type) {
		case Brush:
			g.setColor(Color.green);
			break;
		case Impassible:
			g.setColor(Color.blue);
			break;
		case Lawn:
			g.setColor(Color.cyan);
			break;
		case Road:
			g.setColor(Color.DARK_GRAY);
			break;
		default:
			break;
		}
		g.fillRect(0, 0, getWidth(), getHeight());
		ITileFixture localFixture = fixture;
		if (localFixture != null) {
			try {
				Image image =
						ImageLoader.getLoader().loadImage(
								localFixture.getImage());
				g.drawImage(image, getWidth() / 4, getHeight() / 4,
						getWidth() / 2, getHeight() / 2, null);
			} catch (IOException e) {
				return;
			}
		}
	}
}
