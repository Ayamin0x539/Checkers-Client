package view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import model.Board;

public class CheckersCanvas extends JPanel implements MouseListener {

	public static final int BOARD_DIM = 8;

	private ArrayList<Square> blackSquares;
	private ArrayList<Square> redSquares;

	private Square moveDestination;
	private Square moveSource;

	/* Checker board representation */
	Board board;
	GridBagConstraints layoutConstraints;

	public CheckersCanvas() {
		super(new GridLayout(BOARD_DIM, BOARD_DIM));
		this.blackSquares = new ArrayList<Square>();
		this.redSquares = new ArrayList<Square>();
		initSquares();
	}

	public void initSquares() {
		for (int i = 0; i < BOARD_DIM; ++i) {
			if (i % 2 == 0) {
				for (int j = 0; j < BOARD_DIM/2; ++j) {
					Square black = new Square(Color.BLACK, i, j*2);
					black.addMouseListener(this);
					GUIPiece p = new GUIPiece(Color.WHITE);
					black.setPiece(new GUIPiece(Color.WHITE));
					Square red = new Square(Color.RED, i, j*2 + 1);
					red.addMouseListener(this);
					this.add(black);
					blackSquares.add(black);
					this.add(red);
					redSquares.add(red);
				}
			} else {
				for (int j = 0; j < BOARD_DIM/2; ++j) {
					Square black = new Square(Color.BLACK, i, j*2 + 1);
					black.addMouseListener(this);
					Square red = new Square(Color.RED, i, j*2);
					red.addMouseListener(this);
					this.add(red);
					blackSquares.add(red);
					this.add(black);
					redSquares.add(black);
				}
			}
		}
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
			} else {
				if (square == moveSource)
					moveSource = null;
				square.setSelected(false);
			}
		} else {
			
			if (!square.isSelected()) {
				square.setSelected(true);
				if (moveDestination != null)
					moveDestination.setSelected(false);
				moveDestination = square;
			} else {
				if (square == moveDestination)
					moveDestination = null;
				square.setSelected(false);
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}
}
