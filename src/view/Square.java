package view;

import java.awt.Color;

import javax.swing.JButton;

public class Square extends JButton {
	private Color color;
	private int row;
	private int column;
	
	public Square(Color color, int row, int column) {
		super("");
		this.color = color;
		this.setRow(row);
		this.setColumn(column);
		this.setBackground(color);
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


}
