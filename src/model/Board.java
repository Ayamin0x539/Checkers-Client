package model;

import java.util.ArrayList;

import controller.GameConstants;

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
	
	public int pieceDifferentialHeuristic(Color color) {
		int blackHeuristic = blackPieces + blackKings;
		int whiteHeuristic = whitePieces + whiteKings;
		return - (color == Color.BLACK ? 
				(blackHeuristic - whiteHeuristic) : 
					(whiteHeuristic - blackHeuristic));
	}
	
	public Location samuelMapping(int k) {
		int augmented = (32 - k) * 2;
		int row = augmented / GameConstants.BOARD_SIZE;
		int col = augmented % GameConstants.BOARD_SIZE;
		if (row % 2 == 0) ++col;
		return new Location(row, col);
	}
	
	public int emptySquaresSurrounding(Piece piece) {
		int num = 0;
		int row = piece.getLocation().row;
		int col = piece.getLocation().column;
		boolean up = row > 0,
				down = row < GameConstants.BOARD_SIZE,
				left = col > 0,
				right = row < GameConstants.BOARD_SIZE;
		if (up && left && this.representation[row-1][col-1] == null) ++num;
		if (up && right && this.representation[row-1][col+1] == null) ++num;
		if (down && left && this.representation[row+1][col-1] == null) ++num;
		if (down && right && this.representation[row+1][col+1] == null) ++num;
		
		return num;
	}
	
	public boolean isActive(Piece piece) {
		if (piece == null) return false;
		ArrayList<Move> jumpFrontier = this.generateJumpMovesForPiece(piece);
		return jumpFrontier.size() != 0;
	}
	
	/**
	 * "The parameter is debited with 1 if there are no kings
	 *  on the board, if either square 7 or 26 is occupied by
	 *  an active man, and if neither of these squares is
	 *  occupied by a passive man.
	 * @param color
	 * @return
	 */
	public int apexHeuristic(Color color) {
		boolean noKings = false,
				eitherSquaresOccupiedByActiveMan = false,
				neitherSquaresOccupiedByPassiveMan = true;
		Location square7 = this.samuelMapping(7);
		Location square26 = this.samuelMapping(26);
		Piece piece7 = this.getPiece(square7);
		Piece piece26 = this.getPiece(square26);
		
		boolean active7 = isActive(piece7);
		boolean active26 = isActive(piece26);
		
		noKings = (color == Color.BLACK && this.blackKings == 0) ||
					(color == Color.WHITE && this.whiteKings == 0);
		if (piece7 != null && piece7.getColor() == color) {
			eitherSquaresOccupiedByActiveMan |= (active7 && piece7.getType() == Type.NORMAL);
			neitherSquaresOccupiedByPassiveMan &= (active7 && piece7.getType() == Type.NORMAL);
		}
		if (piece26 != null && piece26.getColor() == color) {
			eitherSquaresOccupiedByActiveMan |= (active26 && piece26.getType() == Type.NORMAL);
			neitherSquaresOccupiedByPassiveMan &= (active26 && piece26.getType() == Type.NORMAL);
		}
		
		if (noKings && eitherSquaresOccupiedByActiveMan && neitherSquaresOccupiedByPassiveMan)
			return -1;
		return 0;
	}
	
	public int backHeuristic(Color color) {
		if (whiteKings + blackKings == 0){
			if (color == Color.BLACK){
				Location thirty = samuelMapping(30);
				Location thirty_two = samuelMapping(32);
				
				if ((isActive(getPiece(thirty)) == false) && (isActive(getPiece(thirty_two))==false)){
					return 1;
				}
			}
			else { // White color
				Location one = samuelMapping(1);
				Location three = samuelMapping(3);
				
				if ((isActive(getPiece(one)) == false) && (isActive(getPiece(three))==false)){
					return 1;
				}
			}
		}
		return 0;
	}
	
	public int centHeuristic(Color color) {
		int heuristicval = 0;
		int[] coordinates = {11,12,15,16,20,21,24,25};
		
		for (int value : coordinates){
			Location piece_loc = samuelMapping(value);
			if (isOccupied(piece_loc)==true){
				
				Piece chosen_piece = getPiece(piece_loc);
				if (isActive(chosen_piece)==false && chosen_piece.getType()==Type.NORMAL
						&& chosen_piece.getColor() == color){
					heuristicval +=1;
				}
			}
		}
		
		return heuristicval;
	}
	
	/**
	 * "The parameter is credited with 1 for each square
	 * to which the active side could move one or more pieces in
	 * the normal fashion, disregarding the fact that jump moves
	 * may or may not be available.
	 * @param color
	 * @return
	 */
	public int mobHeuristic(Color color) {
		ArrayList<Move> moves = this.generateAllMoves(color);
		return moves.size();
	}
	
	/**
	 * "The parameter is credited with 1 for each of the
	 * following squares: 11, 12, 15, 16, 20, 21, 24, and 25
	 * which is occupied by a passive king."
	 * @param color
	 * @return
	 */
	public int kcentHeuristic(Color color) {
		int sum = 0;
		int[] locations = {11, 12, 15, 16, 20, 21, 24, 25};
		for (int k : locations) {
			Piece p = this.getPiece(this.samuelMapping(k));
			if (p != null && p.getType() == Type.KING && !isActive(p))
				++sum;
		}
		return sum;
	}
	
	public int getHeuristic(Color color) {
		/* Kings are weighted more, so we count for them twice */
		int heuristic = 0;
		if (GameConstants.PIECE_DIFFERENTIAL) {
			heuristic += pieceDifferentialHeuristic(color)*GameConstants.PIECE_DIFFERENTIAL_WEIGHT;
		}
		if (GameConstants.APEX) {
			heuristic += apexHeuristic(color)*GameConstants.APEX_WEIGHT;
		}
		if (GameConstants.BACK) {
			heuristic += backHeuristic(color)*GameConstants.BACK_WEIGHT;
		}
		if (GameConstants.CENT) {
			heuristic += centHeuristic(color)*GameConstants.CENT_WEIGHT;
		}
		if (GameConstants.MOB) {
			heuristic += mobHeuristic(color)*GameConstants.MOB_WEIGHT;
		}
		if (GameConstants.KCENT) {
			heuristic += kcentHeuristic(color)*GameConstants.KCENT_WEIGHT;
		}
		
		return heuristic;
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

