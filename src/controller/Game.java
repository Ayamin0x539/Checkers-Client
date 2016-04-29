package controller;

import java.util.ArrayList;

import model.Board;
import model.Color;
import model.Location;
import model.Move;
import view.GamePanel;

public class Game {
	private Board board;
	private GamePanel gamePanel;
	private boolean inJumpSequence;


	public Game(Board board) {
		this.board = board;
		this.inJumpSequence = false;
	}
	
	/* Either the server or the user will request a move */
	public void requestMove(Move move) {
		board.movePiece(move);
		gamePanel.movePiece(move);
		
		/* If this is the first jump of the jump sequence */
		if (move.isJump() && !inJumpSequence) inJumpSequence = true;
		
		/* If the piece that jumped has no more jumps */
		if (move.isJump() && getAvailableMoves(move.destination).isEmpty())
			inJumpSequence = false;
		
		/* If the game is not in the middle of a jump sequence, move the thunk */
		if (!inJumpSequence) {

			Move thunkMove = getThunkMove();
			board.movePiece(thunkMove);
			gamePanel.movePiece(thunkMove);
			
			if (thunkMove.isJump()) inJumpSequence = true;
			
			while (inJumpSequence) {
				thunkMove = getThunkMove();
				if (thunkMove != null) {
					board.movePiece(thunkMove);
					gamePanel.movePiece(thunkMove);
				} else {
					inJumpSequence = false;
				}
			}
		}
			
	}
	
	public Move getThunkMove() {
		ArrayList<Move> availableMoves;
		
		if (inJumpSequence) {
			availableMoves = 
					board.generateJumpMovesForPiece(board.getLastPieceMoved());
		} else {
			availableMoves = board.generateAllMoves(GameConstants.THUNK_COLOR);
		}
		
		if (!availableMoves.isEmpty()) {
			/* Just take the first move we see */
			Move moveChoice = availableMoves.get(0);
			return moveChoice;
		} else {
			return null;
		}
	}

	
	public ArrayList<Move> getAvailableMoves(Location source) {
		if (inJumpSequence) {
			return board.generateJumpMovesForPiece(board.getPiece(source));
		} else {
			return board.generateMovesForPiece(board.getPiece(source));
		}
	}
	
	public ArrayList<Move> getAllAvailableJumpMoves(Color player) {
		return board.generateAllJumpMoves(player);
	}
	
	public void setGamePanel(GamePanel panel) {
		this.gamePanel = panel;
	}

	public boolean isInJumpSequence() {
		return inJumpSequence;
	}

}
