package view;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class CheckersWindow extends JFrame {
	
	public static final int HEIGHT = 825;
	public static final int WIDTH = 800;
	private GamePanel gamePanel;
	
	public CheckersWindow() {
		super("Checkers");
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		initGamePanel();
		this.setVisible(true);
	}
	
	private void initGamePanel() {
		this.gamePanel = new GamePanel();
		this.getContentPane().add(this.gamePanel, BorderLayout.CENTER);
	}
	
}
