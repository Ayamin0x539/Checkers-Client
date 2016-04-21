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
	
	/**
	 * Generates the frontier for a particular piece.
	 * @param p
	 * @return
	 */
	public ArrayList<Move> generateMoves(Piece p) {
		ArrayList<Move> avail_moves = new ArrayList<Move>();
		int row = p.getLocation().row;
		int col = p.getLocation().column;
		boolean up, down;
		up = p.getColor() == Color.BLACK || p.getType() == Type.KING;
		down = p.getColor() == Color.RED || p.getType() == Type.KING;
		if (up) {
			Move upLeft = new Move(p.getLocation(), new Location(row - 1, col - 1));
			if (isValidMove(upLeft)) {
				avail_moves.add(new Move(p.getLocation(), new Location(row - 1, col - 1)));
			}
			
			// up right	
			Move upRight = new Move(p.getLocation(), new Location(row - 1, col + 1));
			if (isValidMove(upRight)) {
				avail_moves.add(new Move(p.getLocation(), new Location(row - 1, col + 1)));
			}
		}
		if (down) {
			// down left
			Move downLeft = new Move(p.getLocation(), new Location(row + 1, col - 1));
			if (isValidMove(downLeft)) {
				avail_moves.add(new Move(p.getLocation(), new Location(row + 1, col - 1)));
			}
			
			// down right
			Move downRight = new Move(p.getLocation(), new Location(row + 1, col + 1));
			if (isValidMove(downRight)) {
				avail_moves.add(new Move(p.getLocation(), new Location(row + 1, col + 1)));
			}
		}
		return avail_moves;
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
					ArrayList<Move> jump_moves = generateMoves(this.representation[i][j]);
					for (Move move : jump_moves) {
						Board board = new Board(this);
						board.move(move);
						frontier.add(board);
					}
				}
			}
		}
		return frontier;
	}
	
	public void move(Move move) {
		representation[move.destination.row][move.destination.column]
				= representation[move.source.row][move.source.column];
		representation[move.source.row][move.source.column] = null;
		representation[move.destination.row][move.destination.column]
				.setLocation(new Location(move.destination.row, move.destination.column));
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
	
	public boolean isValidMove(Move move) {
		return isValidSquare(move.destination) && !isOccupied(move.destination);
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

