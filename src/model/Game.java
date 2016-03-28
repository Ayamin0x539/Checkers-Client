package model;

public class Game {
	private Board board;
	private Player player_one, player_two;

	public Game(Player p1, Player p2) {
		this.player_one = p1;
		this.player_two = p2;
		this.board = new Board();
	}

}
