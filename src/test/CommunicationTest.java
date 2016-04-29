package test;

import java.util.ArrayList;

import model.Location;
import model.Move;
import controller.Communication;

public class CommunicationTest {

	public CommunicationTest() {
		// TODO Auto-generated constructor stub
	}
	
	public boolean moveExistsInSet(Move move, ArrayList<Move> set) {
		for (Move m : set) {
			if (	move.destination.row 	== m.destination.row &&
					move.destination.column == m.destination.column &&
					move.source.row			== m.source.row &&
					move.source.column		== m.source.column) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Input: "(0:0):(2:2):(4:0)"
	 * 
	 * Output: { Move( from=(0,0), to=(2,2) ), 
	 * 			 Move( from=(2,2), to=(4,0) ) }
	 */
	public void stringToMoveTest() {
		String moveString = "(0:0):(2:2):(4:0)";
		
		ArrayList<Move> moves = Communication.stringToMove(moveString);
		
		Move move1 = new Move(new Location(0, 0), new Location(2, 2));
		Move move2 = new Move(new Location(2, 2), new Location(4, 0));
		
		assert(moveExistsInSet(move1, moves) && moveExistsInSet(move2, moves)) : "FAILED: String --> Move";
		
		System.out.println("PASSED: String --> Move");
	}
	
	/**
	 * Input: { Move( from=(0,0), to=(2,2) ), 
	 * 			 Move( from=(2,2), to=(4,0) ) }
	 * 
	 * Output: "(0:0):(2:2):(4:0)"
	 */
	public void moveToStringTest() {
		ArrayList<Move> moves = new ArrayList<Move>();
		Move move1 = new Move(new Location(0, 0), new Location(2, 2));
		Move move2 = new Move(new Location(2, 2), new Location(4, 0));
		moves.add(move1);
		moves.add(move2);
		
		String moveString = Communication.moveToString(moves);
		
		assert(moveString.equals("(0:0):(2:2):(4:0)")) : "PASSED: Move --> String";
		
		System.out.println("PASSED: Move --> String");
	}
	
	/**
	 * Test the inverse property.
	 */
	public void bidirectionalTest() {
		String moveString = "(0:0):(2:2):(4:0)";
		
		ArrayList<Move> moves = Communication.stringToMove(moveString);
		
		assert(Communication.moveToString(moves).equals(moveString)) : "FAILED: String --> Move --> String";
		
		System.out.println("PASSED: String --> Move --> String");
	}

	public static void main(String[] args) {
		CommunicationTest ct = new CommunicationTest();
		ct.stringToMoveTest();
		ct.moveToStringTest();
		ct.bidirectionalTest();
		
	}

}
