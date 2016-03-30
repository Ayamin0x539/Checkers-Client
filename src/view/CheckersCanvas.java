package view;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;

import model.Board;

public class CheckersCanvas extends JPanel {
	
	public static final int BOARD_DIM = 8;
	
	ArrayList<Square> blackSquares;
	ArrayList<Square> redSquares;
	
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
					black.add(new GUIPiece(Color.WHITE));
					Square red = new Square(Color.RED, i, j*2 + 1);
					this.add(black);
					blackSquares.add(black);
					this.add(red);
					redSquares.add(red);
				}
			} else {
				for (int j = 0; j < BOARD_DIM/2; ++j) {
					Square black = new Square(Color.BLACK, i, j*2 + 1);
					Square red = new Square(Color.RED, i, j*2);
					this.add(red);
					blackSquares.add(red);
					this.add(black);
					redSquares.add(black);
				}
			}
		}
	}
}
