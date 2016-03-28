package model;

import java.util.ArrayList;

public class Player {
	private ArrayList<Piece> pieces;
	public final String name;
	
	public Player(String name) {
		this.name = name;
		this.pieces = new ArrayList<Piece>();
	}
	
	public void addPiece(Piece p) {
		pieces.add(p);
	}
	
	public ArrayList<Piece> getPieces() {
		return pieces;
	}
	
	public String getName() {
		return name;
	}
}
