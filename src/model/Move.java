package model;

public class Move {
	private Location source;
	private Location destination;
	
	public Move(Location source, Location destination) {
		this.setSource(source);
		this.setDestination(destination);
	}

	public Location getSource() {
		return source;
	}

	public void setSource(Location source) {
		this.source = source;
	}

	public Location getDestination() {
		return destination;
	}

	public void setDestination(Location destination) {
		this.destination = destination;
	}
	
	
}
