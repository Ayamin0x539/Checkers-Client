package model;

public class Game {
	private Board board;
	private Player player_one, player_two;

	public Game(Player p1, Player p2) {
		this.player_one = p1;
		this.player_two = p2;
		this.board = new Board();
	}
	
	private void assignPieces() {
		if (player_one.getAssignedColor() == Color.RED) {
			player_one.setPieces(board.getRedPieces());
			player_two.setPieces(board.getBlackPieces());
		} else {
			player_two.setPieces(board.getRedPieces());
			player_one.setPieces(board.getBlackPieces());			
		}
	}

}
