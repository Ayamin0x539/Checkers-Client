package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import model.Location;

public class CheckersCanvas extends JPanel {

	public static final int BOARD_DIM = 8;
	public static final int CANVAS_SIZE = 800;

	private Square[][] board;


	GridBagConstraints layoutConstraints;

	public CheckersCanvas(GamePanel gamePanel) {
		super(new GridLayout(BOARD_DIM, BOARD_DIM));
		this.board = new Square[BOARD_DIM][BOARD_DIM];
		
		this.setPreferredSize(new Dimension(CANVAS_SIZE, CANVAS_SIZE));
		this.setMaximumSize(new Dimension(CANVAS_SIZE, CANVAS_SIZE));
		this.setMinimumSize(new Dimension(CANVAS_SIZE, CANVAS_SIZE));
		
		initCanvas(gamePanel);
	}
	
	private void initCanvas(GamePanel gamePanel) {
		initSquares(gamePanel);
		initCheckers();
	}

	private void initSquares(GamePanel gamePanel) {
		for (int i = 0; i < BOARD_DIM; ++i) {
			if (i % 2 == 0) {
				for (int j = 0; j < BOARD_DIM/2; ++j) {
					/* Create a black square */
					Square blackSquare = new Square(Color.BLACK, new Location(i, j*2));
					board[i][j*2] = blackSquare;
					blackSquare.addMouseListener(gamePanel);
					this.add(blackSquare);

					/* Create a red square */
					Square redSquare = new Square(new Color(150, 0, 0), new Location(i, j*2 + 1));
					board[i][j*2 + 1] = redSquare;
					redSquare.addMouseListener(gamePanel);
					this.add(redSquare);
				}
			} else {
				for (int j = 0; j < BOARD_DIM/2; ++j) {
					/* Create a red square */
					Square redSquare = new Square(new Color(150, 0, 0), new Location(i, j*2));
					board[i][j*2] = redSquare;
					redSquare.addMouseListener(gamePanel);
					this.add(redSquare);
					
					/* Create a black square */
					Square blackSquare = new Square(Color.BLACK, new Location(i, j*2 + 1));
					board[i][j*2 + 1] = blackSquare;
					blackSquare.addMouseListener(gamePanel);
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
}
