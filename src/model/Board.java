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

	public boolean isValidSquare(int i, int j) {
		return 	0 <= i && i < BOARD_SIZE && 
				0 <= j && j < BOARD_SIZE;
	}

	/**
	 * Checks if a piece can attack another in a given direction.
	 * Up is negative y direction, down is positive y direction.
	 * Left is negative x direction, right is positive x direction.
	 * @param p The piece.
	 * @param X The direction along the x-coordinate (LEFT or RIGHT).
	 * @param Y The direction along the y-coordinate (UP or DOWN).
	 * @return
	 */
	public boolean checkAttackDirection(Piece p, Piece[][] image, Direction X, Direction Y) {
		Piece candidate = null;
		int i = p.getX(), j = p.getY(); // y is row, x is column
		int enemy_i = X.equals(Direction.LEFT) ? i-1 : i+1;
		int enemy_j = Y.equals(Direction.UP) ? j-1 : j+1;
		int end_position_i = X.equals(Direction.LEFT) ? i-2 : i+2;
		int end_position_j = Y.equals(Direction.UP) ? j-2: j+2;
		if (isValidSquare(enemy_j, enemy_i)) {
			candidate = image[enemy_j][enemy_i];
			if (null != candidate && // there exists a piece we can take)
					candidate.color.equals(p.opposite()) && // it is an enemy piece
					isValidSquare(end_position_j, end_position_i) && // there is a free space
					null == image[end_position_j][end_position_i]) // that we can end up in
				return true;
		}
		return false;
	}

	/**
	 * Used for validation of moves.
	 * If returns true after a player chooses a move,
	 * that move is invalid because the player can keep on attacking.
	 * We go by the convention that black starts out at the "bottom", and 
	 * red starts out at the "top". Smoke moves before fire.
	 * 
	 * Up is negative y direction, down is positive y direction.
	 * Left is negative x direction, right is positive x direction.
	 * 
	 * @param p
	 * @return
	 */
	public boolean hasAttackVector(Piece p, Piece[][] image) {
		boolean can_attack = false;
		if (p.color.equals(Color.BLACK) || p.getType().equals(Type.KING)) {
			can_attack |= checkAttackDirection(p, image, Direction.UP, Direction.LEFT);
			can_attack |= checkAttackDirection(p, image, Direction.UP, Direction.RIGHT);
		}
		if (p.color.equals(Color.RED) || p.getType().equals(Type.KING)) {
			can_attack |= checkAttackDirection(p, image, Direction.DOWN, Direction.LEFT);
			can_attack |= checkAttackDirection(p, image, Direction.DOWN, Direction.RIGHT);
		}
		return can_attack;
	}
	
	/**
	 * Used for validation of moves.
	 * We go by the convention that black starts out at the "bottom", and 
	 * red starts out at the "top". Smoke moves before fire.
	 * Up is negative y direction, down is positive y direction.
	 * Left is negative x direction, right is positive x direction.
	 * A piece can move if the spot is available AND the move matches the color.
	 * (Black can only go up and red can only go down, unless they are kinged.)
	 * @param p
	 * @return true if the piece can move to the specified coordinates.
	 */
	public boolean canMove(Piece p, int x, int y) {
		if(!this.isValidSquare(y, x)) return false;
		boolean spot_available = (null == this.representation[y][x]);
		boolean is_moving_up = (y == p.getY() - 1);
		boolean is_moving_down = (y == p.getY() + 1);
		boolean is_moving_left = (x == p.getX() - 1);
		boolean is_moving_right = (x == p.getX() + 1);
		
		// Spot must be available and must be moving exactly one to the left or right
		if (spot_available && (is_moving_left || is_moving_right)) {
			if (p.color.equals(Color.BLACK) || p.getType().equals(Type.KING)) {
				if (is_moving_up) return true;
			}
			if (p.color.equals(Color.RED) || p.getType().equals(Type.KING)) {
				if (is_moving_down) return true;
			}
		}
		return false;
	}
	
	/**
	 * Tests whether we can move a piece at (src_x, src_y)
	 * to a spot on the board at (dest_x, dest_y).
	 * Delegates to canMove(Piece, int, int).
	 * If a piece does not exist at the source, returns false.
	 * @param src_x
	 * @param src_y
	 * @param dest_x
	 * @param dest_y
	 * @return
	 */
	public boolean canMove(int src_x, int src_y, int dest_x, int dest_y) {
		if (!(this.isValidSquare(src_y, src_x) && this.isValidSquare(dest_y, dest_x))) return false;
		Piece p = this.representation[src_y][src_x];
		if (null == p) return false;
		return this.canMove(p, dest_x, dest_y);
	}
	
	public boolean tryToMove(int src_x, int src_y, int dest_x, int dest_y) {
		if (canMove(src_x, src_y, dest_x, dest_y)) {
			Piece piece = this.representation[src_y][src_x];
			this.representation[src_y][src_x] = null;
			this.representation[dest_y][dest_x] = piece;
			return true;
		}
		else if (canJump)
		
	}
	
	/**
	 * Initialize the board putting checker pieces in their starting locations
	 */
	private void init()
	{
		for(int row = 0; row < 3; row++){
			for (int col = 0; col < 4; col++)
			{
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

