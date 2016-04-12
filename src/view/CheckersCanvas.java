package view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import model.Board;
import model.Location;
import model.Piece;

public class CheckersCanvas extends JPanel implements MouseListener {

	public static final int BOARD_DIM = 8;

	private Square[][] board;

	private Square moveDestination;
	private Square moveSource;


	GridBagConstraints layoutConstraints;

	public CheckersCanvas() {
		super(new GridLayout(BOARD_DIM, BOARD_DIM));
		this.board = new Square[BOARD_DIM][BOARD_DIM];
		initSquares();
		initCheckers();
	}

	public void initSquares() {
		for (int i = 0; i < BOARD_DIM; ++i) {
			if (i % 2 == 0) {
				for (int j = 0; j < BOARD_DIM/2; ++j) {
					/* Create a black square */
					Square blackSquare = new Square(Color.BLACK, new Location(i, j*2));
					board[i][j*2] = blackSquare;
					blackSquare.addMouseListener(this);
					this.add(blackSquare);

					/* Create a red square */
					Square redSquare = new Square(new Color(150, 0, 0), new Location(i, j*2 + 1));
					board[i][j*2 + 1] = redSquare;
					redSquare.addMouseListener(this);
					this.add(redSquare);
				}
			} else {
				for (int j = 0; j < BOARD_DIM/2; ++j) {
					/* Create a red square */
					Square redSquare = new Square(new Color(150, 0, 0), new Location(i, j*2));
					board[i][j*2] = redSquare;
					redSquare.addMouseListener(this);
					this.add(redSquare);
					
					/* Create a black square */
					Square blackSquare = new Square(Color.BLACK, new Location(i, j*2 + 1));
					board[i][j*2 + 1] = blackSquare;
					blackSquare.addMouseListener(this);
					this.add(blackSquare);
				}
			}
		}
	}
	
	private void initCheckers() {
		for (int row = 0; row < BOARD_DIM / 2 - 1; ++row) {
			for (int col = 0; col < BOARD_DIM / 2; ++col) {
				Checker redChecker = new Checker(new Color(255, 51, 51));
				Checker blackChecker = new Checker(new Color(89, 89, 89));
				board[row][2*col+ (row % 2)].setPiece(redChecker);
				board[BOARD_DIM - 1 - row][2*col + (BOARD_DIM - 1 - row) %2].setPiece(blackChecker);
			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {


	}

	@Override
	public void mouseEntered(MouseEvent e) {

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
