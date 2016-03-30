package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;

public class Square extends JButton {
	private Color color;
	private int row;
	private int column;
	private GUIPiece piece;
	
	public Square(Color color, int row, int column) {
		super("");
		this.color = color;
		this.setRow(row);
		this.setColumn(column);
		this.setBackground(color);
		this.setLayout(new BorderLayout());
		//this.setPreferredSize(new Dimension(100, 100));
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
	}


}
