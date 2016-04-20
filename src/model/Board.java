package model;

import java.util.ArrayList;

/**
 * The representation is a 8x8 grid where
 * A[row][col] marks the position of the checker piece.
 * Smoke starts at lower row and fire starts at higher row.
 * @author Ayamin
 *
 */
public class Board {
	// Board properties and representation
	private final int BOARD_SIZE = 8;
	private Piece[][] representation;

	// Move properties
	private int movesSinceCapture;
	public enum Direction {UP, DOWN, LEFT, RIGHT};
	

	public Board() {
		representation = new Piece[BOARD_SIZE][BOARD_SIZE];
		movesSinceCapture = 0;
		init();

	}
	
	/**
	 * Copy constructor.
	 * @param other
	 */
	public Board(Board other) {
		this.representation = new Piece[BOARD_SIZE][BOARD_SIZE];
		Piece[][] other_representation = other.getRepresentation();
		for (int i = 0; i < other_representation.length; ++i) {
			for (int j = 0; j < other_representation[0].length; ++j) {
				this.representation[i][j] = other_representation[i][j];
			}
		}
		movesSinceCapture = other.getMovesSinceCapture();
	}

	public boolean isValidSquare(Location location) {
		return 	0 <= location.row && location.row < BOARD_SIZE && 
				0 <= location.column && location.column < BOARD_SIZE;
	}

	/**
	 * Initialize the board putting checker pieces in their starting locations
	 */
	private void init() {
		for (int row = 0; row < 3; row++){
			for (int col = 0; col < 4; col++) {
				Piece red_piece = new Piece(Color.RED, 2*col + (row % 2), row);
				Piece black_piece = new Piece(Color.BLACK, 2*col + (BOARD_SIZE - 1 - row) %2, BOARD_SIZE - 1 - row);
				representation[row][2*col+ (row % 2)] = red_piece;
				representation[BOARD_SIZE - 1 - row][2*col + (BOARD_SIZE - 1 - row) %2] = black_piece;
			}
		}
	}
	
	/**
	 * Tests board equality.
	 * @param other The other board.
	 * @return true if the board's pieces are all equal to the other board's pieces.
	 */
	public boolean equals(Board other) {
		for (int i = 0; i < BOARD_SIZE; ++i) {
			for (int j = 0; j < BOARD_SIZE; ++j) {
				if(!(this.representation[i][j]).equals(other.getRepresentation()[i][j])) 
					return false;
			}
		}
		return true;
	}
	
	public ArrayList<Board> generateMoveFrontier(Color color) {
		ArrayList<Board> frontier = new ArrayList<Board>();
		
		
	}
	
	/**
	 * Generates the frontier.
	 * @param color The color of pieces to generate the frontier for.
	 * @return A list of possible "next moves" in the form of boards.
	 */
	public ArrayList<Board> generateFrontier(Color color) {
		ArrayList<Board> from_jumps = generateJumpFrontier(color);
		if(from_jumps.isEmpty()) {
			return generateMoveFrontier(color);
		}
		return from_jumps;
	}
	
	/**
	 * Print the current board representation
	 */
	public void print() {
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				if (!isOccupied(new Location(row, col)))
					System.out.print("| ");
				else if (representation[row][col].getColor() == Color.RED) {
					if (representation[row][col].getType() == Type.NORMAL)
						System.out.print("|r");
					else	
						System.out.print("|R");
				}
				else {
					if (representation[row][col].getType() == Type.NORMAL)
						System.out.print("|b");
					else
						System.out.print("|B");
				}
			}
			System.out.println("|");
		}
	}
	
	
	public boolean isValidJump(Move move) {
		Piece monkey = representation[(move.destination.row + move.source.row)/2][(move.destination.column + move.source.column)/2];
		Piece toMove = representation[move.source.row][move.source.column];
		return isValidSquare(move.destination) && !isOccupied(move.destination)
				&& monkey != null
				&& monkey.getColor() == toMove.opposite();
	}
	
	/**
	 * return true if square contains a piece
	 * return false otherwise
	 */
	public boolean isOccupied(Location location) {
		return representation[location.row][location.column] != null;
	}

	
	public Piece[][] getRepresentation() {
		return this.representation;
	}
	
	public int getMovesSinceCapture() {
		return this.movesSinceCapture;
	}
}

