package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Represents the panel which will hold all of the graphical
 * components of the game.
 * @author john
 *
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	private JLabel messageBar;
	private CheckersCanvas canvas;
	private GridBagConstraints layoutConstraints;

	private Square moveDestination;
	private Square moveSource;

	public GamePanel(GameEventListener gameListener) {
		super(new GridBagLayout());
		
		/* Initialize the layout manager */
		this.layoutConstraints = new GridBagConstraints();
		this.layoutConstraints.gridy = 0;
		
		/* Add the game listener as a listener */
		this.addKeyListener(gameListener);
		this.setFocusable(true);
		
		/* Initialize the components */
		this.initMessageBar();
		this.initCanvas(gameListener);
	}

	private void initMessageBar() {
		this.messageBar = new JLabel("Select a piece to move.");
		this.layoutConstraints.ipady = 10;
		this.add(this.messageBar, layoutConstraints);
		this.layoutConstraints.gridy++;
	}
	
	private void initCanvas(GameEventListener gameListener) {
		this.canvas = new CheckersCanvas(gameListener);
		this.add(this.canvas, layoutConstraints);
		this.layoutConstraints.gridy++;
	}

	public void displayMessage(String message) {
		this.messageBar.setText(message);
	}

	public void updateMoveMessage() {
		if (moveSource == null) {
			displayMessage("Select a piece to move.");
		} else if (moveDestination == null) {
			displayMessage("Select a destination.");
		} else {
			displayMessage("Select \"Move\" to move the piece.");
		}
	}

	public void setMoveSource(Square square) {
		if (moveSource != null) moveSource.setSelected(false);
		if (moveDestination != null) moveDestination.setSelected(false);

		this.moveDestination = null;
		
		if (square == moveSource) {
			this.moveSource = null;
		} else {
			this.moveSource = square;
			square.setSelected(true);
		}
		
	}

	public void setMoveDestination(Square square) {
		if (moveDestination != null) moveDestination.setSelected(false);
		
		if (square == moveDestination) {
			this.moveDestination = null;
		} else {
			this.moveDestination = square;
			square.setSelected(true);
		}
	}

	public boolean moveReady() {
		return moveSource != null && moveDestination != null;
	}

}
