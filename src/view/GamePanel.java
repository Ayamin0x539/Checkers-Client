package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Game;
import controller.GameConstants;
import model.Color;
import model.Location;
import model.Move;


/**
 * Represents the panel which will hold all of the graphical
 * components of the game.
 * @author john
 *
 */
@SuppressWarnings("serial")
public class GamePanel extends JPanel {
	
	private Game game;
	
	private JLabel messageBar;
	private CheckersCanvas canvas;
	private GridBagConstraints layoutConstraints;

	private Square moveDestination;
	private Square moveSource;


	public GamePanel(Game game, GameEventListener gameListener) {
		super(new GridBagLayout());
		
		this.game = game;
		
		/* Initialize the layout manager */
		this.layoutConstraints = new GridBagConstraints();
		this.layoutConstraints.gridy = 0;
		
		/* Add the game listener as a listener */
		this.addKeyListener(gameListener);
		this.setFocusable(true);
		
		/* Initialize the components */
		this.initMessageBar();
		this.initCanvas(gameListener);
	}

	private void initMessageBar() {
		this.messageBar = new JLabel("Select a piece to move.");
		this.layoutConstraints.ipady = 10;
		this.add(this.messageBar, layoutConstraints);
		this.layoutConstraints.gridy++;
	}
	
	private void initCanvas(GameEventListener gameListener) {
		this.canvas = new CheckersCanvas(gameListener);
		this.add(this.canvas, layoutConstraints);
		this.layoutConstraints.gridy++;
	}

	public void displayMessage(String message) {
		this.messageBar.setText(message);
	}

	public void updateMoveMessage() {
//		if (moveSource == null && moveDestination == null) {
//			displayMessage("---");
//		} else 
		if (moveSource == null) {
			displayMessage("Select a piece to move.");
		} else if (moveDestination == null) {
			displayMessage("Select a destination.");
		} else {
			displayMessage(" ");
		}
	}

	public void setMoveSource(Square square) {
		if (moveSource != null) moveSource.setSelected(false);
		if (moveDestination != null) moveDestination.setSelected(false);

		this.moveDestination = null;
		
		if (square == moveSource) {
			this.moveSource = null;
		} else {
			this.moveSource = square;
			square.setSelected(true);
		}
		
	}

	public void setMoveDestination(Square square) {
		if (moveDestination != null) moveDestination.setSelected(false);
		
		if (square == moveDestination) {
			this.moveDestination = null;
		} else {
			this.moveDestination = square;
			square.setSelected(true);
		}
	}

	public boolean moveReady() {
		return moveSource != null && moveDestination != null;
	}

	public void highlightValidDestinations(Location source) {
		ArrayList<Move> availMoves = game.getAvailableMoves(source);
		
		for (Move move : availMoves) {
			canvas.highlightAndValidateSquare(move.destination);
		}
	}
	
	public void dehighlightValidDestinations() {
		canvas.invalidateAllSquares();
	}

	public void movePiece(Move move) {
		if (move.isJump()) {
			int monkeyRow = (move.destination.row + move.source.row)/2;
			int monkeyCol = (move.destination.column + move.source.column)/2;
	
			/* Remove the piece being jumped ("monkey in the middle") */
			canvas.removePiece(new Location(monkeyRow, monkeyCol));
		}
	
		canvas.movePiece(move);
		
		if (canPromote(canvas.getSquare(move.destination))) {
			canvas.getSquare(move.destination).promotePiece();
		}
	}
	
	
	public void moveSelectedPiece() {

		/* Create the move */
		final Move move = new Move(moveSource.getCellLocation(), moveDestination.getCellLocation());

		/* Get rid of valid destination options */
		dehighlightValidDestinations();

		new Thread(new Runnable() {
			@Override
			public void run() {
				/* Request the move */
				game.requestMove(move);

				/* If the user just jumped and the game is still in a jump sequence */
				/* Select the same piece again */
				if (move.isJump() && game.isInJumpSequence()) {
					moveDestination.setSelected(true);
					highlightValidDestinations(moveDestination.getCellLocation());
					moveSource = moveDestination;
					moveDestination = null;
				} else {
					/* Reset the move choices */
					resetMove();
				}

				/* See if any jump moves are available */
				ArrayList<Move> jumpMoves = 
						game.getAllAvailableJumpMoves(GameConstants.USER_COLOR);

				if (!jumpMoves.isEmpty()) {
					for (Move jump : jumpMoves) {
						canvas.highlightAndValidateSquare(jump.source);
					}
				}
			}
		}).start();
	}
	
	public void resetMove() {
		moveSource.setSelected(false);
		moveDestination.setSelected(false);
		moveSource = null;
		moveDestination = null;
	}
	
	public boolean canPromote(Square square) {
		int row = square.getCellLocation().row;

		return !square.isKing() && 
				((row == 0 && square.getPieceColor() == Color.WHITE) || 
						(row == CheckersCanvas.BOARD_DIM - 1 && 
						square.getPieceColor() == Color.BLACK));
	}
	

	public boolean isInJumpSequence() {
		return game.isInJumpSequence();
	}
	
	public boolean isForceJump() {
		return !game.getAllAvailableJumpMoves(GameConstants.USER_COLOR).isEmpty();
	}
}
