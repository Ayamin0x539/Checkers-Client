package player;

import java.util.ArrayList;

import model.Board;
import model.Color;
import model.Location;
import model.Move;

public class Player {
	protected Color color;
	protected Board board;
	protected boolean inJumpSequence;
	
	public Player(Color color, Board board) {
		this.color = color;
		this.board = board;
		this.inJumpSequence = false;
	}
	
	//Sorry that I had to make this, it's needed to make life easier on 
	//The server side
	public void setColor(Color newColor){
		this.color = newColor;
	}
	
	public boolean isInJumpSequence() {
		return inJumpSequence;
	}

	public ArrayList<Move> getAllAvailableMoves() {
		return board.generateAllMoves(color);
	}

	public ArrayList<Move> getAllAvailableJumpMoves() {
		return board.generateAllJumpMoves(color);
	}

	public ArrayList<Move> getAvailableMoves(Location source) {
		if (inJumpSequence) {
			return board.generateJumpMovesForPiece(board.getPiece(source));
		} else {
			return board.generateMovesForPiece(board.getPiece(source));
		}
	}
}
