package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;
import model.Location;

public class Square extends JPanel implements MouseListener {
	private Color color;
	private final Location location;
	private Checker piece;
	private boolean selected;
	
	public Square(Color color, Location location) {
		super();
		this.color = color;
		this.location = location;
		this.selected = false;
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
		return selected;
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
		return piece != null;
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
		this.dispatchEvent(newE);
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}


}
