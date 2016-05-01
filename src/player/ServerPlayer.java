package player;

import java.util.ArrayList;

import controller.Game;
import model.Board;
import model.Color;
import model.Move;

public class ServerPlayer extends Player {
	private Game game;
	
	public ServerPlayer(Color color, Board board, Game game) {
		super(color, board);
		this.game = game;
	}
	
	public ArrayList<Move> makeMove(Move move) {
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
		
		ArrayList<Move> computerMoves = new ArrayList<Move>();
		
		/* Make the computer move */
		Move computerMove = game.makeComputerMove();
		
		if (move != null) {
			computerMoves.add(computerMove);
		}
		
		/* While the computer can still move, ask it to move */
		while (game.getComputer().isInJumpSequence()) {
			computerMove = game.makeComputerMove();
			if (move != null) {
				computerMoves.add(computerMove);
			}
		}
		
		/* Return the list of moves made by the computer */
		return computerMoves;
	}
	
	public void listen() {
		/* Test code */
		while(true) {
			System.out.println("Listening");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/* Listen to server and wait for incoming messages.
		 * and GameConstants.SERVER_COLOR according to who the server says
		 * will go first.
		 */
		
		/* Busy-wait on moves sent from the server. When receiving a move
		 * message, call the static Communication.stringToMove() method.
		 * This will return an ArrayList of moves the server wishes to make.
		 * For each move, call the makeMove() method. This method will return
		 * the sequence of moves made by the computer in response to the
		 * server's move. However, in the case where the server is 
		 * making a sequence of moves, only the last call to makeMove will 
		 * return something meaningful. That last call will return an 
		 * ArrayList of moves made by the computer. Call the 
		 * Communication.moveToString() static method on this list to get 
		 * the string to send to the server;
		 */
	}
}
