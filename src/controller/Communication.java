package controller;

import java.util.ArrayList;

import model.Location;
import model.Move;

/**
 * Responsible for mapping our representation and moves to the server's representation
 * and expected input for moves.
 * @author Ayamin
 *
 */
public class Communication {

	/**
	 * Converts a string representation of a move sequence to a
	 * Move type object usable by this client's representation.
	 * 
	 * @param stringMove The string representation of a move sequence, sent by the server.
	 * @return An arraylist of Move objects constructed from the string representation.
	 */
	public static ArrayList<Move> stringToMove(String stringMoves) {
		String[] splitMoves = stringMoves.split(":");
		
		ArrayList<Integer> moveIndexes = new ArrayList<Integer>(); 
		
		for (String move : splitMoves) {
			String index = move.replaceAll("[^0-9]", "");
			if (!index.equals("")) {
				moveIndexes.add(Integer.parseInt(index));
			}
		}
		
		ArrayList<Move> moves = new ArrayList<Move>();
		
		for (int i = 0; i < moveIndexes.size() - 3; i+=2) {
			Location from = new Location(moveIndexes.get(i), moveIndexes.get(i+1));
			Location to = new Location(moveIndexes.get(i+2), moveIndexes.get(i+3));
			Move move = new Move(from, to);
			moves.add(move);
		}
		
		return moves;
		
	}
	
	/**
	 * Converts a arraylist of Move type object to the
	 * string representation understood by the server.
	 * 
	 * @param moves The list of Move objects, representing a move sequence.
	 * @return A string representation of the Move object.
	 */
	public static String moveToString(ArrayList<Move> moves) {
		StringBuilder sb = new StringBuilder();
		
		Move firstmove = moves.get(0);
		sb.append("(");
		sb.append(firstmove.source.row);
		sb.append(":");
		sb.append(firstmove.source.column);
		sb.append("):(");
		sb.append(firstmove.destination.row);
		sb.append(":");
		sb.append(firstmove.destination.column);
		sb.append(")");
		for (int i = 1; i < moves.size(); ++i) {
			Move move = moves.get(i);
			sb.append(":(");
			sb.append(move.destination.row);
			sb.append(":");
			sb.append(move.destination.column);
			sb.append(")");
		}
		return sb.toString();
	}
	
	public Communication() {
		// TODO Auto-generated constructor stub
	}

}
