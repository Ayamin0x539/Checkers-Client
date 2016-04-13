package view;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements MouseListener {
	private JLabel messageBar;
	private CheckersCanvas canvas;
	
	private Square moveDestination;
	private Square moveSource;
	
	public GamePanel() {
		this.messageBar = new JLabel("Select a piece to move.");
		this.canvas = new CheckersCanvas(this);
		
		this.add(this.messageBar);
		this.add(this.canvas);
	}
	
	public void displayMessage(String message) {
		this.messageBar.setText(message);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {


	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		Square square = (Square) e.getComponent();
		if(square.hasPiece()) {
			if (!square.isSelected()) {
				square.setSelected(true);
				if (moveSource != null)
					moveSource.setSelected(false);
				if (moveDestination != null)
					moveDestination.setSelected(false);
				moveDestination = null;
				moveSource = square;
				displayMessage("Select a destination.");
			} else {
				if (square == moveSource)
					moveSource = null;
				square.setSelected(false);
				displayMessage("Select a piece to move.");
			}
		} else {
			
			if (!square.isSelected()) {
				square.setSelected(true);
				if (moveDestination != null)
					moveDestination.setSelected(false);
				moveDestination = square;
				if (moveSource != null)
					displayMessage("Select \"Move\" to move the piece.");
				else
					displayMessage("Select a piece to move.");
			} else {
				if (square == moveDestination)
					moveDestination = null;
				square.setSelected(false);
				if (moveSource != null)
					displayMessage("Select a destination.");
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}
}
