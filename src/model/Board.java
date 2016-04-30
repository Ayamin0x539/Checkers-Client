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
	private static final int BOARD_SIZE = 8;
	private Piece[][] representation;
	
	private Piece lastPieceMoved;
	private Move lastMove;
	
	private int blackPieces;
	private int whitePieces;
	private int blackKings;
	private int whiteKings;

	// Move properties
	private int movesSinceCapture;
	public enum Direction {UP, DOWN, LEFT, RIGHT};
	

	public Board() {
		representation = new Piece[BOARD_SIZE][BOARD_SIZE];
		movesSinceCapture = 0;
		init();
		lastPieceMoved = null;
		lastMove = null;
		blackPieces = 12;
		whitePieces = 12;
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
				if (other_representation[i][j] != null) {
					this.representation[i][j] = new Piece(other_representation[i][j]);
				}
			}
		}
		movesSinceCapture = other.getMovesSinceCapture();
		lastPieceMoved = other.getLastPieceMoved();
		lastMove = other.getLastMove();
		blackPieces = other.getBlackPieces();
		whitePieces = other.getWhitePieces();
	}

	/**
	 * Initialize the board putting checker pieces in their starting locations
	 */
	private void init() {
		for (int row = 0; row < 3; row++){
			for (int col = 0; col < 4; col++) {
				Piece white_piece = new Piece(Color.WHITE, BOARD_SIZE - row - 1, 2*col + (row % 2));
				Piece black_piece = new Piece(Color.BLACK, row, 2*col + (BOARD_SIZE - 1 - row) %2);
				representation[BOARD_SIZE - row - 1][2*col+ (row % 2)] = white_piece;
				representation[row][2*col + (BOARD_SIZE - 1 - row) %2] = black_piece;
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
	
	public void movePiece(Move move) {
		int sourceRow = move.source.row;
		int sourceCol = move.source.column;
		int destRow = move.destination.row;
		int destCol = move.destination.column;
	
		if (move.isJump()) {
			int monkeyRow = (destRow + sourceRow)/2;
			int monkeyCol = (destCol + sourceCol)/2;
	
			Piece removed = representation[monkeyRow][monkeyCol];
			
			if (removed.getColor() == Color.BLACK) {
				--blackPieces; 
				if (removed.getType() == Type.KING) --blackKings;
			}
			else {
				--whitePieces;
				if (removed.getType() == Type.KING) --whiteKings;
			}
			
			/* Remove the piece being jumped ("monkey in the middle") */
			representation[monkeyRow][monkeyCol] = null;
			
			this.movesSinceCapture = 0;
		} 
		else {
			++this.movesSinceCapture;
		}
	
		/* Place the piece in the destination cell */
		representation[destRow][destCol] = representation[sourceRow][sourceCol];
		
		/* Remove from the source cell */
		representation[sourceRow][sourceCol] = null;
		
		/* Update the piece's location */
		representation[destRow][destCol].setLocation(new Location(destRow, destCol));
		
		Piece moved = representation[destRow][destCol];
		
		if (canPromote(moved)) {
			moved.promote();
			if (moved.color == Color.BLACK) {
				++blackKings;
			} else {
				++whiteKings;
			}
		}
	
		/* Update the last piece that was moved */
		this.lastPieceMoved = moved;
	}

	/**
	 * Generates the frontier.
	 * @param color The color of pieces to generate the frontier for.
	 * @return A list of possible "next moves" in the form of boards.
	 */
	public ArrayList<Board> generateFrontier(Color color) {
		ArrayList<Board> from_jumps = generateFrontierFromJumps(color);
		if (from_jumps.isEmpty()) {
			return generateFrontierFromRegularMoves(color);
		}
		return from_jumps;
	}

	/**
	 * Generates the frontier for movement for all pieces.
	 * @param color
	 * @return
	 */
	public ArrayList<Board> generateFrontierFromRegularMoves(Color color) {
		ArrayList<Board> frontier = new ArrayList<Board>();
		ArrayList<Move> moves = generateAllRegularMoves(color);
		for (Move move : moves) {
			Board board = new Board(this);
			board.movePiece(move);
			frontier.add(board);
		}
		return frontier;	
	}

	public ArrayList<Board> generateFrontierFromJumps(Color color) {
		ArrayList<Board> frontier = new ArrayList<Board>();
		ArrayList<Move> moves = generateAllJumpMoves(color);
		for (Move move : moves) {
			Board board = new Board(this);
			board.movePiece(move);
			frontier.add(board);
		}
		return frontier;	
	}

	public ArrayList<Move> generateMovesForPiece(Piece p) {
		ArrayList<Move> jumps = generateJumpMovesForPiece(p);
		if (jumps.isEmpty()) {
			return generateRegularMovesForPiece(p);
		}
		return jumps;
	}
	
	/**
	 * Generates the Move set for a particular piece.
	 * @param p
	 * @return
	 */
	public ArrayList<Move> generateRegularMovesForPiece(Piece p) {
		ArrayList<Move> avail_moves = new ArrayList<Move>();
		int row = p.getLocation().row;
		int col = p.getLocation().column;
		boolean up, down;
		up = p.getColor() == Color.WHITE || p.getType() == Type.KING;
		down = p.getColor() == Color.BLACK || p.getType() == Type.KING;
		if (up) {
			// up left
			Move upLeft = new Move(p.getLocation(), new Location(row - 1, col - 1));
			if (isValidMove(upLeft)) {
				avail_moves.add(upLeft);
			}
			
			// up right	
			Move upRight = new Move(p.getLocation(), new Location(row - 1, col + 1));
			if (isValidMove(upRight)) {
				avail_moves.add(upRight);
			}
		}
		if (down) {
			// down left
			Move downLeft = new Move(p.getLocation(), new Location(row + 1, col - 1));
			if (isValidMove(downLeft)) {
				avail_moves.add(downLeft);
			}
			
			// down right
			Move downRight = new Move(p.getLocation(), new Location(row + 1, col + 1));
			if (isValidMove(downRight)) {
				avail_moves.add(downRight);
			}
		}
		return avail_moves;
	}
	
	public ArrayList<Board> generateJumpFrontierForPiece(Piece p) {
		ArrayList<Board> frontier = new ArrayList<Board>();
		ArrayList<Move> moves = generateJumpMovesForPiece(p);
		for (Move move : moves) {
			Board board = new Board(this);
			board.movePiece(move);
			frontier.add(board);
		}
		return frontier;
	}
	
	/**
	 * Returns the possible jumps a piece can take.
	 * @param color
	 * @return
	 */
	public ArrayList<Move> generateJumpMovesForPiece(Piece p) {
		ArrayList<Move> jumps = new ArrayList<Move>();
		int row = p.getLocation().row, 
				col = p.getLocation().column;
		boolean up = p.getColor() == Color.WHITE || p.getType() == Type.KING;
		boolean down = p.getColor() == Color.BLACK || p.getType() == Type.KING;
		if (up) {
			// Up left
			Move upleft = new Move(new Location(row, col), new Location(row - 2, col - 2));
			if (isValidJump(upleft)) {
				jumps.add(upleft);
			}
			// Up right
			Move upright = new Move(new Location(row, col), new Location(row - 2, col + 2));
			if (isValidJump(upright)) {
				jumps.add(upright);
			}
		}
		if (down) {
			// Down left
			Move downleft = new Move(new Location(row, col), new Location(row + 2, col - 2));
			if (isValidJump(downleft)) {
				jumps.add(downleft);
			}
			// Down right
			Move downright = new Move(new Location(row, col), new Location(row + 2, col + 2));
			if (isValidJump(downright)) {
				jumps.add(downright);
			}
		}
		return jumps;
	}

	public ArrayList<Move> generateAllMoves(Color color) {
		ArrayList<Move> jumps = generateAllJumpMoves(color);
		if (jumps.isEmpty()) {
			return generateAllRegularMoves(color);
		}
		return jumps;
	}

	public ArrayList<Move> generateAllJumpMoves(Color color) {
		ArrayList<Move> frontier = new ArrayList<Move>();
		for (int i = 0; i < BOARD_SIZE; ++i) {
			for (int j = 0; j < BOARD_SIZE; ++j) {
				Piece p = this.representation[i][j];
				if (null != p && p.getColor() == color) {
					ArrayList<Move> jump_moves = generateJumpMovesForPiece(this.representation[i][j]);
					frontier.addAll(jump_moves);
				}
			}
		}
		return frontier;		
	}
	
	public ArrayList<Move> generateAllRegularMoves(Color color) {
		ArrayList<Move> frontier = new ArrayList<Move>();
		for (int i = 0; i < BOARD_SIZE; ++i) {
			for (int j = 0; j < BOARD_SIZE; ++j) {
				Piece p = this.representation[i][j];
				if(null != p && p.getColor() == color) {
					ArrayList<Move> moves = generateRegularMovesForPiece(this.representation[i][j]);
					frontier.addAll(moves);
				}
			}
		}
		return frontier;
	}
	
	/**
	 * Print the current board representation
	 */
	public void print() {
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int col = 0; col < BOARD_SIZE; col++) {
				if (!isOccupied(new Location(row, col)))
					System.out.print("| ");
				else if (representation[row][col].getColor() == Color.BLACK) {
					if (representation[row][col].getType() == Type.NORMAL)
						System.out.print("|b");
					else	
						System.out.print("|B");
				}
				else {
					if (representation[row][col].getType() == Type.NORMAL)
						System.out.print("|w");
					else
						System.out.print("|W");
				}
			}
			System.out.println("|");
		}
	}
	
	public boolean isValidSquare(Location location) {
		return 	0 <= location.row && location.row < BOARD_SIZE && 
				0 <= location.column && location.column < BOARD_SIZE;
	}

	/**
	 * Determines whether a move is a valid jump.
	 * @param move
	 * @return
	 */
	public boolean isValidJump(Move move) {
		if (isValidSquare(move.destination)) {
			Piece monkey = representation[(move.destination.row + move.source.row)/2][(move.destination.column + move.source.column)/2];
			Piece toMove = representation[move.source.row][move.source.column];
			return !isOccupied(move.destination)
					&& monkey != null
					&& monkey.getColor() == toMove.opposite();

		} else {
			return false;
		}
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
	
	public boolean canPromote(Piece p) {
		
		return p.getType() != Type.KING && 
				isPromotionLocation(p.getLocation());
	}
	
	public boolean isPromotionLocation(Location location) {
		return (location.row == 0 || 
				location.row == BOARD_SIZE - 1 );
	}
	
	public int getHeuristic(Color color) {
		/* Kings are weighted more, so we count for them twice */
		int blackHeuristic = blackPieces + blackKings;
		int whiteHeuristic = whitePieces + whiteKings;
		return - (color == Color.BLACK ? 
				(blackHeuristic - whiteHeuristic) : 
					(whiteHeuristic - blackHeuristic));
	}
	
	public Piece getPiece(Location location) {
		return representation[location.row][location.column];
	}
	
	public Piece[][] getRepresentation() {
		return this.representation;
	}
	
	public int getMovesSinceCapture() {
		return this.movesSinceCapture;
	}
	
	public Piece getLastPieceMoved() {
		return this.lastPieceMoved;
	}
	public Move getLastMove() {
		return this.lastMove;
	}
	
	public int getWhitePieces() {
		return this.whitePieces;
	}
	
	public int getBlackPieces() {
		return this.blackPieces;
	}
}

