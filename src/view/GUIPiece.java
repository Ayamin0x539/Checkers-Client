package view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class GUIPiece extends JButton {
	private Color color;
	
	public GUIPiece(Color color) {
		super("");
		this.color = color;
		this.setContentAreaFilled(false);
		this.setBorderPainted(false);
	}
	
	protected void paintComponent(Graphics g) {
		  if (getModel().isArmed()) {
		    g.setColor(color);
		  } else {
		    g.setColor(getBackground());
		  }
		  Graphics2D g2 = (Graphics2D)g;
		  RenderingHints hints = new RenderingHints(null);
		  hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		  hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		  hints.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		  g2.setRenderingHints(hints);
		  g2.fillOval(0, 0, getSize().width,getSize().height-1);
		  super.paintComponent(g2);
		}

}
