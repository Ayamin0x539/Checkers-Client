package model;

public class Board {
	private final int BOARD_SIZE = 8;
	private Piece[][] representation;
	private int movesSinceCapture;

	public Board() {
		representation = new Piece[BOARD_SIZE][BOARD_SIZE];
	}

}
