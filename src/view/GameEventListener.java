package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

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
		if(square.hasPiece()) {
			gamePanel.setMoveSource(square);
			gamePanel.updateMoveMessage();
		} else {
			gamePanel.setMoveDestination(square);
			gamePanel.updateMoveMessage();
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void keyPressed(KeyEvent arg0) {}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER && gamePanel.moveReady()) {
			System.out.println("Moving piece.");
			// TODO: Request a move from the controller.
		}

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
			JOptionPane.showMessageDialog(null, "<html><ol><li>instr 1</li>"
					+ "</ol></html>", "Instructions", JOptionPane.INFORMATION_MESSAGE);
		}
	}
}

