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
@SuppressWarnings("serial")
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

	private boolean valid;


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


	/**
	 * Initializes the properties of the square such as color and layout.
	 * @param color	A {@link Color} object representing the square's color. 
	 */
	private void initSquare(Color color) {
		this.setBackground(color);
		this.setLayout(new BorderLayout());
	}


	/**
	 * {@link Square#location}
	 */
	public Location getCellLocation() {
		return location;
	}

	/**
	 * {@link Square#piece}
	 */
	public Checker getPiece() {
		return piece;
	}

	/**
	 * Adds the given piece to the square's panel.
	 * @param piece A {@link Checker} object to place in the square.
	 */
	public void setPiece(Checker piece) {
		this.piece = piece;
		if (piece != null) {
			this.add(piece);
			piece.addMouseListener(this);
			this.validate();
		}
	}

	/**
	 * {@link Square#selected}
	 */
	public boolean isSelected() {
		return this.selected;
	}

	/**
	 * Set's the {@link Square#selected} member to the given value and updates
	 * the panel's border.
	 * @param val The boolean value to set the <code>selected</code> member to.
	 */
	public void setSelected(boolean val) {
		if (val) {
			this.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		} else if (valid){
			this.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
		} else {
			this.setBorder(null);
		}
		this.selected = val;
	}

	public void setValid(boolean state) {
		this.valid = state;
	}

	public boolean isValid() {
		return valid;
	}

	/**
	 * Check's if the square contains a {@link Checker} object or not.
	 * @return	<code>true</code> if the square contains a checker. <br />
	 * 			<code>false</code> otherwise.
	 */
	public boolean hasPiece() {
		return this.piece != null;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		/* Send the event to the lister of the square (GameEventListener) */
		MouseEvent newE = new MouseEvent(this, e.getID(), e.getWhen(), e.getModifiers(), 
				e.getClickCount(), e.getX(), e.getY(), e.isPopupTrigger());
		this.dispatchEvent(newE);	
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}


	public void highlight() {
		this.setBorder(BorderFactory.createLineBorder(Color.YELLOW));

	}


	public void dehighlight() {
		this.setBorder(null);

	}


}
