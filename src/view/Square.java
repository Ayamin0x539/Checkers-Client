package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Square extends JPanel implements MouseListener {
	private Color color;
	private int row;
	private int column;
	private GUIPiece piece;
	private boolean selected;
	
	public Square(Color color, int row, int column) {
		super();
		this.color = color;
		this.setRow(row);
		this.selected = false;
		this.setColumn(column);
		this.setBackground(color);
		this.setLayout(new BorderLayout());
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public GUIPiece getPiece() {
		return piece;
	}

	public void setPiece(GUIPiece piece) {
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
			System.out.println("setting border");
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
