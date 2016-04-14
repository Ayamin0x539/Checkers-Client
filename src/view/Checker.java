package view;

import java.awt.*;
import javax.swing.*;

/**
 * Represents a graphical checkers piece on the checkers canvas.
 * @author john
 *
 */
@SuppressWarnings("serial")
public class Checker extends JPanel {

	/* The color of the checker */
	private Color color;
	
	public Checker(Color color) {
		super();
		this.color = color;
		initChecker();
	}
	
	/**
	 * Initializes the properties of the Checker as a JButton
	 */
	private void initChecker() {
		this.setFocusable(false);
		this.setOpaque(false);
		this.setEnabled(false);
	}

	/**
	 * Overrides the JButton's painComponent method to paint a circle which
	 * represents the checkers piece.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		/* Cast to a 2D graphics object */
		Graphics2D g2 = (Graphics2D) g;
		
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
		g2.setColor(color);
		g2.fillOval(5, 5, getSize().width-10,getSize().height-10);
		
		/* Make a call to the super classes painComponent method */
		super.paintComponent(g2);
	}

}
