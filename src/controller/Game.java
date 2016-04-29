package controller;

import java.io.Console;
import java.util.ArrayList;

import view.GamePanel;
import model.Board;
import model.Color;
import model.Location;
import model.Move;
import model.Piece;

public class Game {
	private Board board;
	private Color current_turn;
	private GamePanel panel;


	public Game(Board board) {
		this.board = board;
		current_turn = Color.BLACK;
	}
	
	public void movePiece(Move move) {
		if (move.isJump()) {
			board.jump(move);
			panel.removePiece(new Location((move.destination.row 
							+ move.source.row)/2, 
							(move.source.column 
									+ move.destination.column) / 2));
		} else {
			board.move(move);
		}
	}
	
	public void switchTurn() {
		this.current_turn = this.current_turn == Color.BLACK ?
							Color.WHITE : Color.BLACK;
	}
	
	public ArrayList<Move> getAvailableMoves(Location source) {
		Piece moved = board.getLastPieceMoved();
		if (moved != null && moved.color == current_turn) {
			// player just jumped
			ArrayList<Move> jumpset = board.generateJumpMoves(moved);
			if (jumpset.isEmpty()) {
				switchTurn();
			}
			return jumpset;
		}
		ArrayList<Move> moves = board.generateMoves(board.getPiece(source));
		ArrayList<Move> jumps = board.generateJumpMoves(board.getPiece(source));
		ArrayList<Move> allMoves = new ArrayList<Move>(moves);
		allMoves.addAll(jumps);
		return allMoves;
	}
	
	public void playVsThunk() {
		Color USER_COLOR = Color.BLACK;
		Color THUNK_COLOR = Color.WHITE;
		while (true) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (this.board.getMovesSinceCapture() > 50) break;
			if (this.current_turn == THUNK_COLOR) {
				Piece moved = this.board.getLastPieceMoved();
				if (moved.color == THUNK_COLOR) {
					// thunk just jumped
					ArrayList<Move> jumpset = board.generateJumpMoves(moved);
					if (jumpset.isEmpty()) {
						switchTurn();
						continue;
					}
					Move jump = jumpset.get(0);
					movePiece(jump);
					panel.moveArbitraryPiece(jump);
				}
				else {
					ArrayList<Move> moveset = board.generateAllPossibleMoves(THUNK_COLOR);
					if (moveset.isEmpty()) {
						System.out.println("Thunk is out of moves.");
						break;
					}
					Move theMove = moveset.get(0);
					movePiece(theMove);
					panel.moveArbitraryPiece(theMove);
					
					if(!theMove.isJump()) {
						switchTurn();
					}
				}
			}
			if (this.current_turn == USER_COLOR) {
				//ArrayList<Move> moveset = board.generateAllPossibleMoves(USER_COLOR);
				//if (moveset.isEmpty()) break;
			}
			
			//board.print();
		}
		Color winner = current_turn == Color.BLACK ? Color.WHITE : Color.BLACK;
		System.out.println("THE WINNER IS " + winner.toString());
	}

	
	public void setGamePanel(GamePanel panel) {
		this.panel = panel;
	}

	public Color getCurrentTurn() {
		return current_turn;
	}

}
