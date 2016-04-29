package view;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Color;
import model.Location;
import model.Move;
import controller.Game;

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
			displayMessage("Press enter to confirm move.");
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
	
	public Location getMoveSource(){
		return moveSource.getCellLocation();
	}
	
	public Location getMoveDestination(){
		return moveDestination.getCellLocation();
	}
	
	
	public void moveSelectedPiece() {
		Move move = new Move(getMoveSource(), getMoveDestination());
		game.movePiece(move);
		
		canvas.moveChecker(getMoveSource(), getMoveDestination());
		dehighlightAllSquares();
		
		if(move.isJump()) {
			Location monkeyLoc = new Location((move.destination.row 
					+ move.source.row)/2, 
					(move.source.column 
							+ move.destination.column) / 2);
			System.out.println(monkeyLoc);
			removePiece(monkeyLoc);
			moveSource.setSelected(false);
			//moveDestination.setSelected(false);
			moveSource = null;
			//moveDestination = null;
			
		} else {
			moveSource.setSelected(false);
			moveDestination.setSelected(false);
			moveSource = null;
			moveDestination = null;
			game.switchTurn();
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
