package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Queue;

import model.Board;
import model.Color;
import model.Location;
import model.Move;
import model.Type;
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

		/* If this is the first jump of the jump sequence */
		if (move.isJump() && !inJumpSequence) {
			inJumpSequence = true;
		}
		
		if (movePromotesPiece(move)) {
			inJumpSequence = false;
		}
		
		board.movePiece(move);
		gamePanel.movePiece(move);
		
		if (board.getMovesSinceCapture() > GameConstants.MAX_PASSIVE_MOVES) {
			gamePanel.displayMessage("It's a tie! Be more aggressive next time.");
			// Disable UI here
		}
		/*
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		/* If the piece that jumped has no more jumps */
		if (move.isJump() && getAvailableMoves(move.destination).isEmpty())
			inJumpSequence = false;
		
		/* If the game is not in the middle of a jump sequence, move the thunk */
		if (!inJumpSequence) {

			Move thunkMove = getMinimaxMove(GameConstants.MAX_SEARCH_DEPTH);
			if (thunkMove != null) {
				if (thunkMove.isJump()) inJumpSequence = true;
				
				if (movePromotesPiece(thunkMove)) {
					inJumpSequence = false;
				}
				
				board.movePiece(thunkMove);
				gamePanel.movePiece(thunkMove);

				while (inJumpSequence) {
					thunkMove = getThunkMove();
					
					if (thunkMove != null) {
						if (movePromotesPiece(thunkMove)) {
							inJumpSequence = false;
						}
						board.movePiece(thunkMove);
						gamePanel.movePiece(thunkMove);
					} else {
						inJumpSequence = false;
					}
				}
			} else {
				gamePanel.displayMessage("Game over. User has won!");
			}
		}
	}
	
	public boolean movePromotesPiece(Move move) {
		return board.getPiece(move.source).getType() != Type.KING &&
				board.isPromotionLocation(move.destination);

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
	
	public Move getMinimaxMove(int depth) {
		ArrayList<Board> boardFrontier = board.generateFrontier(GameConstants.THUNK_COLOR);
		ArrayList<Move> moveFrontier = board.generateAllMoves(GameConstants.THUNK_COLOR);
		ArrayList<Integer> moveScores = new ArrayList<Integer>();
		
		Color otherColor = GameConstants.THUNK_COLOR == Color.BLACK ? 
				Color.WHITE : Color.BLACK;
		// Recurse for each one here
		for (Board board : boardFrontier) {
			moveScores.add(this.getMinimaxScore(otherColor, board, depth));
		}
		
		int maxScore = Integer.MIN_VALUE;
		Move bestMove = null;
		
		for (int i = 0; i < moveScores.size(); ++i) {
			if (moveScores.get(i) > maxScore) {
				bestMove = moveFrontier.get(i);
				maxScore = moveScores.get(i);
			}
			System.out.println("Score[" + i + "] = " + moveScores.get(i));
		}
		System.out.println("---");
		
		return bestMove;
	}
	
	public int getMinimaxScore(Color color, Board b, int depth) {
		if (depth == 0 || b.getBlackPieces() == 0 || b.getWhitePieces() == 0) {
			Color otherColor = color == Color.BLACK ? Color.WHITE : Color.BLACK;
			return b.getHeuristic(otherColor);
		}
		ArrayList<Board> boardFrontier = board.generateFrontier(color);
		
		ArrayList<Integer> moveScores = new ArrayList<Integer>();

		for (Board board : boardFrontier) {
			Color nextColor = color == Color.BLACK ? Color.WHITE : Color.BLACK;
			moveScores.add(getMinimaxScore(nextColor, board, depth - 1));
		}
		for (int score : moveScores) {
			System.out.println(score);
		}
		System.out.println("---");
		if (color == GameConstants.THUNK_COLOR) {
			// Maximize
			return Collections.max(moveScores);
		}
		else {
			// Minimize
			return Collections.min(moveScores);
		}
	}
	
	public void notifyClientWin() {
		gamePanel.displayMessage("Game over. Client has won!");
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
	
	public ArrayList<Move> getAllAvailableMoves(Color player) {
		return board.generateAllMoves(player);
	}
	
	public void setGamePanel(GamePanel panel) {
		this.gamePanel = panel;
	}

	public boolean isInJumpSequence() {
		return inJumpSequence;
	}

}
