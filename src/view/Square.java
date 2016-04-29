package view;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import model.Color;
import model.Location;

/**
 * Represents a square or cell on the checkers board. A square may contain a 
 * checker.
 * @author john
 *
 */
@SuppressWarnings("serial")
public class Square extends JPanel {

	/** 
	 * The {@link Location} (row, col) of the square on the board.
	 */
	private final Location location;

	private boolean hasPiece;

	private Color pieceColor;


	/**
	 * A boolean value representing whether the square is selected for movement.
	 */
	private boolean selected;

	private boolean valid;
	
	private boolean king;


	/**
	 * Constructs a graphical square with the given color and location.
	 * @param color		A {@link Color} object representing the square's color.
	 * @param location	A {@link Location} object representing the row and column.
	 */
	public Square(Color color, Location location) {
		super();
		this.location = location;
		this.selected = false;
		this.king = false;
		this.valid = false;
		initSquare(color);
	}


	/**
	 * Initializes the properties of the square such as color and layout.
	 * @param color	A {@link Color} object representing the square's color. 
	 */
	private void initSquare(Color color) {
		if (color == Color.BLACK) {
			this.setBackground(java.awt.Color.BLACK);
		} else if (color == Color.WHITE) {
			this.setBackground(new java.awt.Color(150, 0, 0));
		}
	}


	/**
	 * {@link Square#location}
	 */
	public Location getCellLocation() {
		return location;
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
			this.setBorder(BorderFactory.createLineBorder(java.awt.Color.GREEN));
		} else if (valid){
			this.setBorder(BorderFactory.createLineBorder(java.awt.Color.YELLOW));
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
		return hasPiece;
	}
	
	public void promotePiece() {
		System.out.println("Kinged");
		this.king = true;
	}
	
	public boolean isKing() {
		return king;
	}


	public void highlight() {
		this.setBorder(BorderFactory.createLineBorder(java.awt.Color.YELLOW));

	}

	/**
	 * Overrides the paintComponent method to paint a circle which
	 * represents the checkers piece.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		
		/* Cast to a 2D graphics object */
		Graphics2D g2 = (Graphics2D) g;
		/* Make a call to the super classes painComponent method */
		super.paintComponent(g2);
		if (hasPiece) {
			/* Add some hints from rendering */
			RenderingHints hints = new RenderingHints(null);
			hints.put(RenderingHints.KEY_ANTIALIASING, 
					RenderingHints.VALUE_ANTIALIAS_ON);
			hints.put(RenderingHints.KEY_INTERPOLATION, 
					RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			hints.put(RenderingHints.KEY_RENDERING, 
					RenderingHints.VALUE_RENDER_QUALITY);
			g2.setRenderingHints(hints);

			/* Set the graphics object's color to the checker's color
			 * and paint an oval which represents the checker. */
			if (this.pieceColor == Color.WHITE) {
				g2.setColor(new java.awt.Color(0xB1B2B3));
				g2.fillOval(5, 5, getSize().width-10,getSize().height-10);
				g2.setColor(java.awt.Color.BLACK);
				if (king) {
					g2.setFont(new Font("Courier", Font.PLAIN, 24));
					g2.drawString("KING", this.getWidth()/2 - 26, this.getHeight()/2 + 6);
				}
			} else if (this.pieceColor == Color.BLACK) {
				g2.setColor(new java.awt.Color(89, 89, 89));
				g2.fillOval(5, 5, getSize().width-10,getSize().height-10);
				g2.setColor(java.awt.Color.WHITE);
				if (king) {
					g2.setFont(new Font("Courier", Font.PLAIN, 24));
					g2.drawString("KING", this.getWidth()/2 - 26, this.getHeight()/2 + 6);
				}
			}
			
		}
	}

	public void dehighlight() {
		this.setBorder(null);

	}

	public Color getPieceColor() {
		return pieceColor;
	}
<<<<<<< HEAD


	public void placePiece(Color color) {
		this.hasPiece = true;
		this.pieceColor = color;
	}

	public Color removePiece() {
		this.hasPiece = false;
		Color color = this.pieceColor;
		this.pieceColor = null;
		this.king = false;
		return color;
	}

=======
	
	public void dehighlight() {
		this.setBorder(null);
>>>>>>> 430f2be346c625bc746ce1659838d93253901c11

	public void setKing(boolean king) {
		this.king = king;
	}


}
