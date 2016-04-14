package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import model.Location;

/**
 * Represents a square or cell on the checkers board. A square may contain a 
 * checker.
 * @author john
 *
 */
public class Square extends JPanel implements MouseListener {

	/** 
	 * The {@link Location} (row, col) of the square on the board.
	 */
	private final Location location;
	
	/** 
	 * The {@link Checker} object contained in this square. Null if there is none.
	 */
	private Checker piece;
	
	/**
	 * A boolean value representing whether the square is selected for movement.
	 */
	private boolean selected;
	
	
	/**
	 * Constructs a graphical square with the given color and location.
	 * @param color		A {@link Color} object representing the square's color.
	 * @param location	A {@link Location} object representing the row and column.
	 */
	public Square(Color color, Location location) {
		super();
		this.location = location;
		this.selected = false;
		initSquare(color);
	}
	

	private void initSquare(Color color) {
		this.setBackground(color);
		this.setLayout(new BorderLayout());
	}


	public Location getCellLocation() {
		return location;
	}

	public Checker getPiece() {
		return piece;
	}

	public void setPiece(Checker piece) {
		this.piece = piece;
		this.add(piece);
		piece.addMouseListener(this);
		this.validate();
	}
	
	public boolean isSelected() {
		return this.selected;
	}

	public void setSelected(boolean val) {
		if (val) {
			this.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		} else {
			this.setBorder(null);
		}
		this.selected = val;
	}
	
	public boolean hasPiece() {
		return this.piece != null;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		MouseEvent newE = new MouseEvent(this, e.getID(), e.getWhen(), e.getModifiers(), 
				e.getClickCount(), e.getX(), e.getY(), e.isPopupTrigger());
		//this.dispatchEvent(newE);
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
