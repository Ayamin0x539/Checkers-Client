package model;

/**
 * Represents a (row, column) location on a chess board.
 * @author john
 *
 */
public class Location {
	public final int row;
	public final int column;
	
	public Location(int row, int column) {
		this.row = row;
		this.column = column;
	}
	
	public boolean equals(Location other) {
		return this.row == other.row && this.column == other.column;
	}
}
