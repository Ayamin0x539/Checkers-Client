package player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

import controller.GameConstants;
import model.Board;
import model.Color;
import model.Location;
import model.Move;

public class ComputerPlayer extends Player {

	public ComputerPlayer(Color color, Board board) {
		super(color, board);
	}

	public Move getMove() {
		Move move = getMinimaxMove(GameConstants.MAX_SEARCH_DEPTH, inJumpSequence);
		if (move == null) {
			inJumpSequence = false;
		}else if (board.movePromotesPiece(move)) {
			inJumpSequence = false;
		}
		return move;
	}

	private Move getMinimaxMove(int depth, boolean inJumpSequence) {
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

		/* All moves have the same outcome */
		if (!moveScores.isEmpty() && maxScore == Collections.min(moveScores)) {
			return getBestOfSimilarMoves(moveFrontier);
		}
		System.out.println("Choosing: " + bestMove);

		return bestMove;
	}

	private int getMinimaxScore(Color color, Board b, int depth, boolean inJumpSequence) {
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

	private Move getBestOfSimilarMoves(ArrayList<Move> moves) {
		ArrayList<Move> regularMoves = new ArrayList<Move>();

		for (Move move : moves) {
			if (!move.isJump()) {
				regularMoves.add(move);
			}
		}

		/* We prefer to advance regular checkers over kings
		 * if all moves have the same outcome
		 */
		if (!regularMoves.isEmpty()) {
			return regularMoves.get(ThreadLocalRandom.current().nextInt(regularMoves.size()));
		}

		return moves.get(ThreadLocalRandom.current().nextInt(moves.size()));
	}



}
