package protocol;

import model.IPoint;
import model.TileType;

/**
 * A message to change a tile's terrain.
 * @author Jonathan Lovelace
 *
 */
public class TerrainChangeMessage extends RPCMessage {
	/**
	 * Where the terrain changed.
	 */
	private final IPoint point;
	/**
	 * What it changed to.
	 */
	private final TileType type;
	/**
	 * @return where the terrain changed
	 */
	public IPoint getPoint() {
		return point;
	}
	/**
	 * @return what it changed to
	 */
	public TileType getType() {
		return type;
	}
	/**
	 * @param player the player being communicated with
	 * @param loc where the terrain changed
	 * @param ttype what it changed to
	 */
	public TerrainChangeMessage(final int player, final IPoint loc,
			final TileType ttype) {
		super(player);
		point = loc;
		type = ttype;
	}
}
