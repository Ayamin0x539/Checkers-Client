package model;

import java.util.ArrayList;

/**
 * The representation is a 8x8 grid where
 * A[y][x] denotes the (x, y) indexed piece.
 * It is swapped because we dereference by "row" first,
 * which is "y".
 * @author Ayamin
 *
 */
public class Board {
	// Board properties and representation
	private final int BOARD_SIZE = 8;
	private Piece[][] representation;

	// Pieces available to the Players
	private ArrayList<Piece> red_pieces;
	private ArrayList<Piece> black_pieces;

	// Move properties
	private int movesSinceCapture;
	public enum Direction {UP, DOWN, LEFT, RIGHT};
	

	public Board() {
		representation = new Piece[BOARD_SIZE][BOARD_SIZE];
		movesSinceCapture = 0;
		init();
	}
	
	
	/**
	 * Initialize the board putting checker pieces in their starting locations
	 */
	private void init()
	{
		for(int row = 0; row < 3; row++) {
			for (int col = 0; col < 4; col++) {
				Piece red_piece = new Piece(Color.RED, 2*col + (row % 2), row);
				Piece black_piece = new Piece(Color.BLACK, 2*col + (BOARD_SIZE - 1 - row) %2, BOARD_SIZE - 1 - row);
				representation[row][2*col+ (row % 2)] = red_piece;
				representation[BOARD_SIZE - 1 - row][2*col + (BOARD_SIZE - 1 - row) %2] = black_piece;
			}
		}
	}
	
	/**
	 * Print the current board representation
	 */
	public void print()
	{
		for(int row = 0; row < BOARD_SIZE; row++)
		{
			for (int col = 0; col < BOARD_SIZE; col++)
			{
				if (!isOccupied(row, col))
					System.out.print("| ");
				else if (representation[row][col].getColor() == Color.RED)
				{
					if (representation[row][col].getType() == Type.NORMAL)
						System.out.print("|r");
					else	
						System.out.print("|R");
				}
				else
				{
					if(representation[row][col].getType() == Type.NORMAL)
						System.out.print("|b");
					else
						System.out.print("|B");
				}

			}
			System.out.println("|");
		}
	}
	
	/**
	 * return true if square contains a piece
	 * return false otherwise
	 */
	public boolean isOccupied(int row, int col)
	{
		return representation[row][col] != null;
	}

	public ArrayList<Piece> getRedPieces() {
		return red_pieces;
	}

	public void setRedPieces(ArrayList<Piece> red_pieces) {
		this.red_pieces = red_pieces;
	}

	public ArrayList<Piece> getBlackPieces() {
		return black_pieces;
	}

	public void setBlackPieces(ArrayList<Piece> black_pieces) {
		this.black_pieces = black_pieces;
	}

}

