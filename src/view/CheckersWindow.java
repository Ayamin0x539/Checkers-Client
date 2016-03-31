package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class CheckersWindow extends JFrame {
	
	public static final int HEIGHT = 800;
	public static final int WIDTH = 800;
	private CheckersCanvas canvas;
	
	public CheckersWindow() {
		super("Checkers");
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.getContentPane().add(new CheckersCanvas(), BorderLayout.CENTER);
		this.setVisible(true);
		this.setResizable(false);
		
		//pack();
	}

	public CheckersCanvas getCanvas() {
		return canvas;
	}

	public void setCanvas(CheckersCanvas canvas) {
		this.canvas = canvas;
	}
	
}
