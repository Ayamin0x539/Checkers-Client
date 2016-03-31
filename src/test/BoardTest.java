package test;

import model.Board;

public class BoardTest {
	
	public static void assertCanMove(Board b, int i, int j, int dest_i, int dest_j) {
		assert(b.canMove(i, j, dest_i,dest_j)) : 
			"Can move from (" + i + "," + j +") to (" + dest_i + "," + dest_j + ").";
	}
	
	public static void assertCannotMove(Board b, int i, int j, int dest_i, int dest_j) {
		assert(!b.canMove(i, j, dest_i,dest_j)) : 
			"Cannot move from (" + i + "," + j +") to (" + dest_i + "," + dest_j + ").";
	}
	
	public static void movementTest() {
		Board b = new Board();
		assertCannotMove(b, 0, 6, 1, 5); // move black onto another
		assertCannotMove(b, 6, 2, 7, 1); // move red up
		assertCannotMove(b, 0, 2, -1, 3); // move red out of bounds
		assertCannotMove(b, 7, 5, 8, 4); // move black out of bounds
		assertCanMove(b, 3, 5, 4, 4); // move black up right
		assertCanMove(b, 3, 5, 2, 4); // move black up left
		assertCanMove(b, 1, 5, 2, 4); // move black up right
		assertCanMove(b, 0, 2, 1, 3); // move red down right
		assertCanMove(b, 4, 2, 3, 3); // move red down left
		assertCanMove(b, 4, 2, 5, 3); // move red down right
	}
	
	public static void printTest() {
		Board b = new Board();
		b.print();
	}

	public static void main(String[]args) {
		printTest();
		movementTest();
	}
}

