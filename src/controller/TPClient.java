package controller;

import model.IPoint;
import model.ProxyUnit;
import model.SimpleUnit;
import model.TileType;

/**
 * A client for the game, sans UI.
 * @author Jonathan Lovelace
 *
 */
public class TPClient implements ITPClient {

	@Override
	public void setPlayerNumber() {
		// TODO Auto-generated method stub

	}

	@Override
	public void rejectPlayerNumber() {
		// TODO Auto-generated method stub

	}

	@Override
	public void endTurn(final int player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addOpposingUnit(final IPoint point, final ProxyUnit unit) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addOwnUnit(final IPoint point, final SimpleUnit unit) {
		// TODO Auto-generated method stub

	}

	@Override
	public void moveFixture(final IPoint source, final IPoint dest, final int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeFixture(final IPoint point, final int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeTerrain(final IPoint point, final TileType type) {
		// TODO Auto-generated method stub

	}

}
