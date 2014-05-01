package view;

import javax.swing.JComponent;

import model.IPoint;
import model.ITileFixture;
import model.TileType;

import org.eclipse.jdt.annotation.Nullable;
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

}
