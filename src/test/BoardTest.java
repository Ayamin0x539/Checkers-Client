package test;

import model.Board;

public class BoardTest {

	public static void main(String[]args) {
		Board checkers = new Board();
		checkers.initBoard();
		checkers.printBoard();
	}
}

