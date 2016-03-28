package model;

public class Board {
	private final int BOARD_SIZE = 8;
	private Piece[][] representation;
	private int movesSinceCapture;

	public Board() {
		representation = new Piece[BOARD_SIZE][BOARD_SIZE];
	}
	
	public void initBoard()
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
