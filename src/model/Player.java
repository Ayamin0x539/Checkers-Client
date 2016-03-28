package model;

import java.util.ArrayList;

public class Player {
	private ArrayList<Piece> pieces;
	private Color assigned_color;
	public final String name;
	
	public Player(String name, Color c) {
		this.name = name;
		this.pieces = new ArrayList<Piece>();
		this.setAssignedColor(c);
	}
	
	public void setPieces(ArrayList<Piece> pieces) {
		this.pieces = pieces;
	}
	
	public ArrayList<Piece> getPieces() {
		return pieces;
	}
	
	public String getName() {
		return name;
	}

	public Color getAssignedColor() {
		return assigned_color;
	}

	public void setAssignedColor(Color assigned_color) {
		this.assigned_color = assigned_color;
	}
}
