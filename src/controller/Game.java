package controller;

import java.util.ArrayList;
import java.util.Collections;

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

		/* If the piece that jumped has no more jumps */
		if (move.isJump() && getAvailableMoves(move.destination).isEmpty())
			inJumpSequence = false;

		/* If the game is not in the middle of a jump sequence, move the thunk */
		if (!inJumpSequence) {

			Move thunkMove = getMinimaxMove(GameConstants.MAX_SEARCH_DEPTH, inJumpSequence);
			if (thunkMove != null) {
				if (thunkMove.isJump()) inJumpSequence = true;

				if (movePromotesPiece(thunkMove)) {
					inJumpSequence = false;
				}

				board.movePiece(thunkMove);
				gamePanel.movePiece(thunkMove);

				while (inJumpSequence) {
					thunkMove = getMinimaxMove(GameConstants.MAX_SEARCH_DEPTH, inJumpSequence);

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

	public Move getMinimaxMove(int depth, boolean inJumpSequence) {
		ArrayList<Board> boardFrontier = null;
		ArrayList<Move> moveFrontier = null;
		ArrayList<Integer> moveScores = new ArrayList<Integer>();

		if (inJumpSequence) {
			/* Generate the frontier only for the piece that just moves */
			boardFrontier = board.generateJumpFrontierForPiece(board.getLastPieceMoved());
			moveFrontier = board.generateJumpMovesForPiece(board.getLastPieceMoved());

			/* If we can't jump anymore, we can't make a move */
			if (boardFrontier.isEmpty()) {
				return null;
			}

		} else {
			/* Generate the frontier for all pieces */
			boardFrontier = board.generateFrontier(GameConstants.THUNK_COLOR);
			moveFrontier = board.generateAllMoves(GameConstants.THUNK_COLOR);
		}

		Color nextColor;
		/* Determine the next color to move */
		if (inJumpSequence) {
			nextColor = GameConstants.THUNK_COLOR;
		} else if (GameConstants.THUNK_COLOR == Color.BLACK) {
			nextColor = Color.WHITE;
		} else {
			nextColor = Color.BLACK;
		}

		/* Calculate the minimax score for each board in the frontier */
		for (Board b : boardFrontier) {
			moveScores.add(this.getMinimaxScore(nextColor, b, depth, inJumpSequence));
		}

		/* Determine the maximum minimax score and which move led to that score */
		int maxScore = Integer.MIN_VALUE;
		Move bestMove = null;

		for (int i = 0; i < moveScores.size(); ++i) {
			//System.out.println("score[" + i + "] = " + moveScores.get(i));
			if (moveScores.get(i) > maxScore) {
				//System.out.println("Best move is " + i);
				bestMove = moveFrontier.get(i);
				maxScore = moveScores.get(i);
			}
			
			System.out.println(moveFrontier.get(i) + " --> " + moveScores.get(i));
		}
		
		System.out.println("Choosing: " + bestMove);

		return bestMove;
	}

	public int getMinimaxScore(Color color, Board b, int depth, boolean inJumpSequence) {
		ArrayList<Board> boardFrontier;
		ArrayList<Integer> moveScores = new ArrayList<Integer>();
		Color otherColor = (color == Color.BLACK ? Color.WHITE : Color.BLACK);

		if (depth == 0) {
			return b.getHeuristic(otherColor);
		}

		if (inJumpSequence) {
			/* Generate the frontier only for the piece that just moved */
			boardFrontier = b.generateJumpFrontierForPiece(b.getLastPieceMoved());

			/* If we can't jump anymore, get out of the jump sequence */
			if (boardFrontier.isEmpty()) {
				return getMinimaxScore(otherColor, b, depth-1, inJumpSequence);
			}
		} else {
			/* Generate the frontier for all pieces */
			boardFrontier = b.generateFrontier(color);
		}

		/* If we have reached the maximum depth or an end state for the game */
		if (b.getBlackPieces() == 0 || b.getWhitePieces() == 0
				|| boardFrontier.size() == 0) {
			return b.getHeuristic(otherColor);
		}

		Color nextColor;
		/* Determine the next color to move */
		if (inJumpSequence) {
			nextColor = color;
		} else {
			nextColor = otherColor;
		}

		for (Board board : boardFrontier) {
			int moveScore = getMinimaxScore(nextColor, board, depth - 1, inJumpSequence);
			moveScores.add(moveScore);
		}

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
