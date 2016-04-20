package controller;

import model.Board;
import model.Color;

public class Game {
	private Board board;
	private Color current_turn;

	public Game(Color start) {
		this.board = new Board();
		current_turn = start;
	}

}
