package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import controller.Game;
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
		if (moveSource == null) {
			displayMessage("Select a piece to move.");
		} else if (moveDestination == null) {
			displayMessage("Select a destination.");
		} else {
			displayMessage("Select \"Move\" to move the piece.");
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
	
	public void dehighlightAllSquares() {
		canvas.invalidateAllSquares();
	}
	
	
	public void moveSelectedPiece() {
		Move theMove = new Move(moveSource.getCellLocation(), moveDestination.getCellLocation());
		game.movePiece(theMove);
		canvas.moveChecker(moveSource.getCellLocation(), moveDestination.getCellLocation());
		dehighlightAllSquares();
		moveSource.setSelected(false);
		moveDestination.setSelected(false);
		moveSource = null;
		moveDestination = null;
		if(!theMove.isJump()) {
			game.switchTurn();
		} else {
			Location monkeyLoc = new Location((theMove.destination.row 
					+ theMove.source.row)/2, 
					(theMove.source.column 
							+ theMove.destination.column) / 2);
			System.out.println(monkeyLoc);
			removePiece(monkeyLoc);
		}
	}
	
	public void removePiece(Location location ) {
		canvas.removeChecker(location);
	}
	
	public void moveArbitraryPiece(Move move) {
		canvas.moveChecker(move.source, move.destination);
	}
	
	public boolean isTurn(Color color) {
		return color == game.getCurrentTurn();
	}
}
