package model;

public class Board {
	private final int BOARD_SIZE = 8;
	private Piece[][] representation;
	private int movesSinceCapture;
	public enum Direction {UP, DOWN, LEFT, RIGHT};

	public Board() {
		representation = new Piece[BOARD_SIZE][BOARD_SIZE];
		movesSinceCapture = 0;
		initBoard();
	}
	
	public boolean isValidSquare(int i, int j) {
		return i < BOARD_SIZE && j < BOARD_SIZE;
	}
	
	/**
	 * Checks if a piece can attack another in a given direction.
	 * @param p The piece.
	 * @param X The direction along the x-coordinate (LEFT or RIGHT).
	 * @param Y The direction along the y-coordinate (UP or DOWN).
	 * @return
	 */
	public boolean checkAttackDirection(Piece p, Direction X, Direction Y) {
		Piece candidate = null;
		int i = p.getX(), j = p.getY();
		int enemy_i = X.equals(Direction.LEFT) ? i-1 : i+1;
		int enemy_j = Y.equals(Direction.UP) ? j+1 : j-1;
		int end_position_i = X.equals(Direction.LEFT) ? i-2 : i+2;
		int end_position_j = Y.equals(Direction.UP) ? j+2: j-2;
		if (isValidSquare(enemy_i, enemy_j)) {
			candidate = representation[enemy_i][enemy_j];
			if (null != candidate && // there exists a piece we can take)
				candidate.color.equals(p.opposite()) && // it is an enemy piece
				isValidSquare(end_position_i, end_position_j) && // there is a free space
				null == representation[end_position_i][end_position_j]) // that we can end up in
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
	 * @param p
	 * @return
	 */
	public boolean hasAttackVector(Piece p) {
		boolean can_attack = false;
		if (p.color.equals(Color.BLACK) || p.getType().equals(Type.KING)) {
			can_attack |= checkAttackDirection(p, Direction.UP, Direction.LEFT);
			can_attack |= checkAttackDirection(p, Direction.UP, Direction.RIGHT);
		}
		if (p.color.equals(Color.RED) || p.getType().equals(Type.KING)) {
			can_attack |= checkAttackDirection(p, Direction.DOWN, Direction.LEFT);
			can_attack |= checkAttackDirection(p, Direction.DOWN, Direction.RIGHT);
		}
		return can_attack;
	}
	
	private void initBoard()
	{
		for(int row = 0; row < 3; row++){
			for (int col = 0; col < 4; col++)
			{
				representation[row][2*col+ (row % 2)] = new Piece(Color.RED, row, 2*col + (row % 2));
				representation[BOARD_SIZE - 1 - row][2*col + (BOARD_SIZE - 1 - row) %2] = new Piece(Color.BLACK, BOARD_SIZE - 1 - row, 2*col + (BOARD_SIZE - 1 - row) %2);
			}
		}
	}
	 
	public void printBoard()
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
	
	public boolean isOccupied(int row, int col)
	{
		return representation[row][col] != null;
	}

}

