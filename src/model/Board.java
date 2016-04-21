package model;

import java.util.ArrayList;

/**
 * The representation is a 8x8 grid where
 * A[row][col] marks the position of the checker piece.
 * Smoke starts at the "bottom" and Fire starts at the "top".
 * The top-left-most square is row = 0, column = 0.
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
	 * Copy constructor.
	 * @param other
	 */
	public Board(Board other) {
		this.representation = new Piece[BOARD_SIZE][BOARD_SIZE];
		Piece[][] other_representation = other.getRepresentation();
		for (int i = 0; i < other_representation.length; ++i) {
			for (int j = 0; j < other_representation[0].length; ++j) {
				this.representation[i][j] = new Piece(other_representation[i][j]);
			}
		}
		movesSinceCapture = other.getMovesSinceCapture();
	}

	public boolean isValidSquare(int row, int col) {
		return 	0 <= row && row < BOARD_SIZE && 
				0 <= col && col < BOARD_SIZE;
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
	
	/**
	 * Generates the frontier for a particular piece.
	 * @param p
	 * @return
	 */
	public ArrayList<Board> generateMoveFrontierForPiece(Piece p) {
		ArrayList<Board> frontier = new ArrayList<Board>();
		int row = p.getLocation().row;
		int col = p.getLocation().column;
		boolean up, down;
		up = p.getColor() == Color.BLACK || p.getType() == Type.KING;
		down = p.getColor() == Color.RED || p.getType() == Type.KING;
		if (up) {
			// up left:
			if (isValidSquare(row - 1, col - 1)) {
				if (null == this.representation[row - 1][col - 1]) {
					Board upleft = new Board(this);
					Piece[][] rep = upleft.getRepresentation();
					rep[row - 1][col - 1] = rep[row][col];
					rep[row][col] = null;
					rep[row - 1][col - 1].setLocation(new Location(row - 1, col - 1));
					frontier.add(upleft);
				}
			}
			// up right	
			if (isValidSquare(row - 1, col + 1)) {
				if (null == this.representation[row - 1][col + 1]) {
					Board upright = new Board(this);
					Piece[][] rep = upright.getRepresentation();
					rep[row - 1][col + 1] = rep[row][col];
					rep[row][col] = null;
					rep[row - 1][col + 1].setLocation(new Location(row - 1, col + 1));
					frontier.add(upright);
				}
			}
		}
		if (down) {
			// down left
			if (isValidSquare(row + 1, col - 1)) {
				if (null == this.representation[row + 1][col - 1]) {
					Board downleft = new Board(this);
					Piece[][] rep = downleft.getRepresentation();
					rep[row + 1][col - 1] = rep[row][col];
					rep[row][col] = null;
					rep[row + 1][col - 1].setLocation(new Location(row + 1, col - 1));
					frontier.add(downleft);
				}
			}
			// down right
			if (isValidSquare(row + 1, col + 1)) {
				if (null == this.representation[row + 1][col + 1]) {
					Board downright = new Board(this);
					Piece[][] rep = downright.getRepresentation();
					rep[row + 1][col + 1] = rep[row][col];
					rep[row][col] = null;
					rep[row + 1][col + 1].setLocation(new Location(row + 1, col + 1));
					frontier.add(downright);
				}
			}
		}
		return frontier;
	}
	
	/**
	 * Generates the frontier for movement for all pieces.
	 * @param color
	 * @return
	 */
	public ArrayList<Board> generateMoveFrontier(Color color) {
		ArrayList<Board> frontier = new ArrayList<Board>();
		for (int i = 0; i < BOARD_SIZE; ++i) {
			for (int j = 0; j < BOARD_SIZE; ++j) {
				Piece p = this.representation[i][j];
				if(null != p && p.getColor() == color) {
					ArrayList<Board> sub_frontier = generateMoveFrontierForPiece(this.representation[i][j]);
					frontier.addAll(sub_frontier);
				}
			}
		}
		return frontier;
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
				if (!isOccupied(row, col))
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
	
	/**
	 * return true if square contains a piece
	 * return false otherwise
	 */
	public boolean isOccupied(int row, int col) {
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
	
	public Piece[][] getRepresentation() {
		return this.representation;
	}
	
	public int getMovesSinceCapture() {
		return this.movesSinceCapture;
	}
}

