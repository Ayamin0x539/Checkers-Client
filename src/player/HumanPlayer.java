package player;

import controller.Game;
import model.Board;
import model.Color;
import model.Move;

public class HumanPlayer extends Player {
	private Game game;
	
	public HumanPlayer(Color color, Board board, Game game) {
		super(color, board);
		this.game = game;
	}
	
	public void makeMove(Move move) {
		/* If this is the first jump of the jump sequence */
		if (move.isJump() && !inJumpSequence) {
			inJumpSequence = true;
		}

		if (board.movePromotesPiece(move)) {
			inJumpSequence = false;
		}
		
		/* Request to move the piece */
		game.requestMove(move);
		
		if(getAvailableMoves(move.destination).isEmpty()) {
			inJumpSequence = false;
		}
		
		/* Make the computer move */
		game.makeComputerMove();
		
		/* While the computer can still move, ask it to move */
		while (game.getComputer().isInJumpSequence()) {
			game.makeComputerMove();
		}
	}
}
