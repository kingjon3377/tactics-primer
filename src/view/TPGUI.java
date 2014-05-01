package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import model.IPoint;
import model.ITileFixture;
import model.TileType;

import org.eclipse.jdt.annotation.Nullable;

import common.MapUpdateListener;

import controller.ITPClient;

public class TPGUI extends JFrame implements TPUI, MapUpdateListener,
		MouseInputListener, ActionListener {
	private int player;
	private final JLabel turnCounter = new JLabel("Player N's turn");
	private final JButton moveButton = new JButton("Move Unit");
	private final JButton attackButton = new JButton("Attack");
	private final JButton rangedButton = new JButton("Ranged Attack");
	private final JButton endTurnButton = new JButton("End Turn");
	@Nullable
	private GUITile selection;
	@Nullable
	private GUITile hovered;
	private JLabel selectLabel = new JLabel("");
	private JLabel hoverLabel = new JLabel("");

	public static enum PlayerAction {
		Move, Attack, RangedAttack, Nothing;
	}

	private PlayerAction currentAction = PlayerAction.Nothing;
	private final ITPClient client;

	public TPGUI(final ITPClient cli) {
		setLayout(new BorderLayout());
		final JPanel actionPanel = new JPanel();
		moveButton.addActionListener(this);
		actionPanel.add(moveButton);
		attackButton.addActionListener(this);
		actionPanel.add(attackButton);
		rangedButton.addActionListener(this);
		actionPanel.add(rangedButton);
		endTurnButton.addActionListener(this);
		actionPanel.add(endTurnButton);
		actionPanel.add(turnCounter);
		actionPanel.setMinimumSize(new Dimension(320, 20));
		add(actionPanel, BorderLayout.NORTH);
		add(new GUIMap(this), BorderLayout.CENTER);
		client = cli;
		final JPanel detailPanel = new JPanel(new BorderLayout());
		detailPanel.add(selectLabel, BorderLayout.WEST);
		detailPanel.add(hoverLabel, BorderLayout.EAST);
		detailPanel.setMinimumSize(new Dimension(320, 20));
		add(detailPanel, BorderLayout.SOUTH);
		addWindowStateListener(new WindowAdapter() {
			@Override
			public void windowClosing(@Nullable final WindowEvent e) {
				client.stopThreads();
				dispose();
			}
		});
		pack();
	}

	@Override
	public int getPlayer() {
		return player;
	}

	@Override
	public void terrainChanged(final IPoint point, final TileType type) {
		// handled in GUIMap
	}

	@Override
	public void fixtureAdded(final IPoint point, final ITileFixture fix) {
		// handled in GUIMap
	}

	@Override
	public void fixtureRemoved(final IPoint point, final ITileFixture fix) {
		// handled in GUIMap
	}

	@Override
	public void fixtureMoved(final IPoint source, final IPoint dest,
			final ITileFixture fix) {
		// handled in GUIMap
	}

	@Override
	public void endTurn(final int playr) {
		if (player == playr) {
			turnCounter.setText("Your Turn");
			moveButton.setEnabled(true);
			attackButton.setEnabled(true);
			rangedButton.setEnabled(true);
			endTurnButton.setEnabled(true);
			currentAction = PlayerAction.Nothing;
			JOptionPane.showMessageDialog(this, "It is your turn");
		} else {
			turnCounter.setText("Player " + playr + "'s Turn");
			moveButton.setEnabled(false);
			attackButton.setEnabled(false);
			rangedButton.setEnabled(false);
			endTurnButton.setEnabled(false);
		}
	}

	/**
	 * @param message
	 *            the error message to throw
	 * @param error
	 *            the exception that caused it
	 */
	@Override
	public void
			showError(final String message, @Nullable final Throwable error) {
		if (error == null) {
			JOptionPane.showMessageDialog(this, message, "Error",
					JOptionPane.ERROR_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(this,
					message + ":\n" + error.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void mouseClicked(@Nullable final MouseEvent e) {
		if (e != null && e.getComponent() instanceof GUITile) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				final GUITile source = (GUITile) e.getComponent();
				final GUITile localSelection = selection;
				if (localSelection == null
						|| currentAction == PlayerAction.Nothing) {
					selection = source;
					currentAction = PlayerAction.Nothing;
					return;
				} else {
					ITileFixture actor = localSelection.getFixture();
					switch (currentAction) {
					case Attack:
						JOptionPane.showMessageDialog(this,
								"Not yet implemented");
						break;
					case Move:
						if (actor != null && localSelection != source
								&& actor.getOwner() == player) {
							client.moveFixture(localSelection.getPoint(),
									source.getPoint(), actor.getID());
						}
						break;
					case Nothing:
						// Can't get here except by race condition
						break;
					case RangedAttack:
						JOptionPane.showMessageDialog(this,
								"Not yet implemented");
						break;
					default:
						// Can't get here.
						JOptionPane.showMessageDialog(this,
								"Not yet implemented");
						break;
					}
					currentAction = PlayerAction.Nothing;
				}
			} else {
				currentAction = PlayerAction.Nothing;
			}
		}
		updateLabels();
	}

	private void updateLabels() {
		final GUITile localSelection = selection;
		final GUITile localHover = hovered;
		if (localSelection == null) {
			selectLabel.setText("Click a tile to select it");
		} else {
			final IPoint point = localSelection.getPoint();
			final ITileFixture fix = localSelection.getFixture();
			final StringBuilder builder = new StringBuilder(100);
			builder.append("Selection: (");
			builder.append(point.getRow());
			builder.append(", ");
			builder.append(point.getColumn());
			builder.append(' ');
			builder.append(localSelection.getType());
			if (fix != null) {
				builder.append(", ");
				builder.append(fix.getDescription());
				if (fix.getOwner() == player) {
					builder.append(" (you)");
				} else {
					builder.append(" (player #");
					builder.append(fix.getOwner());
					builder.append(')');
				}
			}
			selectLabel.setText(builder.toString());
		}
		if (localHover == null) {
			hoverLabel.setText("Hover over a tile to see its details");
		} else {
			final IPoint point = localHover.getPoint();
			final ITileFixture fix = localHover.getFixture();
			final StringBuilder builder = new StringBuilder(100);
			builder.append("Under cursor: (");
			builder.append(point.getRow());
			builder.append(", ");
			builder.append(point.getColumn());
			builder.append(' ');
			builder.append(localHover.getType());
			if (fix != null) {
				builder.append(", ");
				builder.append(fix.getDescription());
				if (fix.getOwner() == player) {
					builder.append(" (you)");
				} else {
					builder.append(" (player #");
					builder.append(fix.getOwner());
					builder.append(')');
				}
			}
			selectLabel.setText(builder.toString());
		}
	}

	@Override
	public void mousePressed(@Nullable final MouseEvent e) {
		// Ignored
	}

	@Override
	public void mouseReleased(@Nullable final MouseEvent e) {
		// Ignored
	}

	@Override
	public void mouseEntered(@Nullable final MouseEvent e) {
		if (e != null && e.getComponent() instanceof GUITile) {
			hovered = (GUITile) e.getComponent();
		} else {
			hovered = null;
		}
		updateLabels();
	}

	@Override
	public void mouseExited(@Nullable final MouseEvent e) {
		hovered = null;
		updateLabels();
	}

	@Override
	public void mouseDragged(@Nullable final MouseEvent e) {
		// ignored
	}

	@Override
	public void mouseMoved(@Nullable final MouseEvent e) {
		// ignored
	}

	@Override
	public void actionPerformed(@Nullable final ActionEvent e) {
		JOptionPane.showMessageDialog(this,
				"Not yet implemented");
	}
}
