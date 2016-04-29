package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import controller.GameConstants;

/**
 * Represents an object which listens for any events which occur in the UI
 * @author john
 *
 */
public class GameEventListener implements MouseListener, KeyListener, ActionListener {

	/**
	 * An instance of {@link GamePanel} for which this listener is listening.
	 */
	private GamePanel gamePanel;

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {
		Square square = (Square) e.getComponent();
		if (square.hasPiece() && !gamePanel.isInJumpSequence() 
				&& square.getPieceColor() == GameConstants.USER_COLOR
				&& (!gamePanel.isForceJump() || (square.hasPiece()
						&& square.isValid()))) {
			gamePanel.dehighlightValidDestinations();
			gamePanel.setMoveSource(square);
			if (square.isSelected())
				gamePanel.highlightValidDestinations(square.getCellLocation());
			gamePanel.updateMoveMessage();
		} else if (square.isValid()) {
			gamePanel.setMoveDestination(square);
			gamePanel.moveSelectedPiece();
			gamePanel.updateMoveMessage();
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void keyPressed(KeyEvent arg0) {}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent arg0) {}

	public void setGamePanel(GamePanel gamePanel) {
		this.gamePanel = gamePanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("New game")) {
			// TODO: Request the controller for a new game
		} else if (e.getActionCommand().equals("Quit")) {
			System.exit(0);
		} else if (e.getActionCommand().equals("Instructions")) {
			// TODO: Create an instructions dialog box, this is just for testing
			String instructions = ""
					+ "<html>"
						+ "<ol>"
							+ "<li>Click the piece you want to move.</li>"
							+ "<li>Click the place you want to move to.</li>"
							+ "<li>Try not to lose.</li>"
						+ "</ol>"
					+ "</html>";
			JOptionPane.showMessageDialog(null, instructions, "Instructions", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}

