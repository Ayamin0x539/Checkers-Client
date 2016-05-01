package view;

import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import controller.Game;
import controller.GameConstants;

/**
 * Represents the JFrame window that will hold the graphical components
 * of the game.
 * @author john
 *
 */
@SuppressWarnings("serial")
public class CheckersWindow extends JFrame {
	/* Constants for the size of the window */
	public static final int HEIGHT = 868;
	public static final int WIDTH = 800;
	
	/**
	 * The {@link GamePanel} instance which contains all of the game's visual
	 * components.
	 */
	private GamePanel gamePanel;
	
	/**
	 * The {@link GameEventListener} instance which will be listening to all
	 * of the game panel's component's
	 */
	private GameEventListener gameListener;
	
	
	public CheckersWindow(Game game, int mode) {
		super("Checkers");
		this.setSize(WIDTH, HEIGHT);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		
		/* MUST be done in this order */
		this.initGameListener();
		this.createMenuBar();
		this.initGamePanel(game, mode);
		this.pack();
	}
	
	/**
	 * Opens the window by setting the frame's visibility
	 */
	public void open() {
		this.setVisible(true);
	}

	/**
	 * Initializes the {@link GameEventListener} instance.
	 */
	private void initGameListener() {
		this.gameListener = new GameEventListener();
	}

	/**
	 * Initializes the frame's menu bar and all of its items.
	 */
	private void createMenuBar() {
		JMenuBar menubar = new JMenuBar(); 
		JMenu file = new JMenu("File");
		/* New Game Option */
		JMenuItem newGame = new JMenuItem("New game");
		newGame.setMnemonic(KeyEvent.VK_N);
		newGame.setToolTipText("Start a new game");
		newGame.addActionListener(gameListener);
		
		/* Quit Option */
		JMenuItem quit = new JMenuItem("Quit");
		quit.setMnemonic(KeyEvent.VK_Q);
		quit.setToolTipText("Exit application");
		quit.addActionListener(gameListener);
		
		/* Instructions Option */
		JMenuItem instructions = new JMenuItem("Instructions");
		instructions.setMnemonic(KeyEvent.VK_I);
		instructions.setToolTipText("How to play");
		instructions.addActionListener(gameListener);
		
		/* Add items to menu bar */
		file.add(quit);
		file.add(newGame);
		file.add(instructions);
		
		menubar.add(file);
		menubar.setVisible(true);
		this.setJMenuBar(menubar);	
	}
	
	/**
	 * Initializes the {@link GamePanel} instance 
	 */
	private void initGamePanel(Game game, int mode) {
		this.gamePanel = new GamePanel(game, gameListener);
		if (mode == GameConstants.SERVER_MODE) {
			gamePanel.disableInteraction();
		} else {
			gamePanel.enableInteraction();
		}
		game.setGamePanel(this.gamePanel);
		this.getContentPane().add(this.gamePanel);
		this.gameListener.setGamePanel(gamePanel);
	}
	
}
