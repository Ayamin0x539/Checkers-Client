package model;

public class Piece {
	public final Color color;
	private int x;
	private int y;
	private Type type;
	
	public Piece(Color color, int x, int y) {
		this.color = color;
		this.x = x;
		this.y = y;
		this.type = Type.NORMAL;
	}
	
	public void updateCoordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Color opposite() {
		if(this.color.equals(Color.RED)) return Color.BLACK;
		if(this.color.equals(Color.BLACK)) return Color.RED;
		return null;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public void promote() {
		this.type = Type.KING;
	}
	
	public Type getType() {
		return this.type;
	}
}
