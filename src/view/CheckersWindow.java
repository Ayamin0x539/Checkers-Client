package view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;


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
		this.createMenuBar();
		this.setVisible(true);
		this.setResizable(false);
		
		//pack();
	}
	
	private void createMenuBar() {
		JMenuBar menubar = new JMenuBar(); 
		JMenu file = new JMenu("File");
		//New Game
		JMenuItem newGame = new JMenuItem("New game");
		newGame.setMnemonic(KeyEvent.VK_N);
		newGame.setToolTipText("Start a new game");
		newGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//this.getContentPane().add(new CheckersCanvas(), BorderLayout.CENTER);
			}
		});
		//Quit
		JMenuItem quit = new JMenuItem("Quit");
		quit.setMnemonic(KeyEvent.VK_Q);
		quit.setToolTipText("Exit application");
		quit.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
	                System.exit(0);
	            }
	        });
		//Rules
		JMenuItem instructions = new JMenuItem("Instructions");
		instructions.setMnemonic(KeyEvent.VK_I);
		instructions.setToolTipText("How to play");
		instructions.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "<html><ol><li>instr 1</li></ol></html>", "title", JOptionPane.INFORMATION_MESSAGE);
				//JOptionPane.showMessageDialog(null, "<html>instr 1<br></html>", "title", JOptionPane.INFORMATION_MESSAGE);
	            }
	        });
		
		file.add(quit);
		file.add(newGame);
		file.add(instructions);
		menubar.add(file);
		menubar.setVisible(true);
		setJMenuBar(menubar);	
	}
	
	
	private void initGamePanel() {
		this.gamePanel = new GamePanel();
		this.getContentPane().add(this.gamePanel, BorderLayout.CENTER);
	}
	
}
