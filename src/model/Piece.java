package model;

public class Piece {
	public final Color color;
	private Location location;
	private Type type;
	
	public Piece(Color color, int row, int col) {
		this.color = color;
		this.location = new Location(row, col);
		this.type = Type.NORMAL;
	}
	
	public void setLocation(Location location) {
		this.location = location;
	}
	
	public Color opposite() {
		if(this.color.equals(Color.RED)) return Color.BLACK;
		if(this.color.equals(Color.BLACK)) return Color.RED;
		return null;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public void promote() {
		this.type = Type.KING;
	}
	
	public Type getType() {
		return this.type;
	}
	
	public Color getColor() {
		return this.color;
	}
}
