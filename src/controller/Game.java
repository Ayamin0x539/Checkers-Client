package controller;

import java.util.ArrayList;

import model.Board;
import model.Color;
import model.Location;
import model.Move;

public class Game {
	private Board board;
	private Color current_turn;
	
	public void movePiece(Move move) {
		board.move(move);
	}
	
	public ArrayList<Move> getAvailableMoves(Location source) {
		ArrayList<Move> moves = board.generateMoves(board.getPiece(source));
		ArrayList<Move> jumps = board.generateJumpMoves(board.getPiece(source));
		
		ArrayList<Move> allMoves = new ArrayList<Move>(moves);
		allMoves.addAll(jumps);
		return allMoves;
	}

	public Game(Color start) {
		this.board = new Board();
		current_turn = start;
	}

}
