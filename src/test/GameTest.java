package test;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import controller.Game;
import model.Board;
import view.CheckersWindow;

public class GameTest {

	public static void main(String[] args) {
		
		// Need to set the look and feel for this to be cross platform
		try {
			// Set cross-platform Java L&F (also called "Metal")
			UIManager.setLookAndFeel(
					UIManager.getCrossPlatformLookAndFeelClassName());
		} 
		catch (UnsupportedLookAndFeelException e) {
			// handle exception
		}
		catch (ClassNotFoundException e) {
			// handle exception
		}
		catch (InstantiationException e) {
			// handle exception
		}
		catch (IllegalAccessException e) {
			// handle exception
		}
		Board board = new Board();
		
		final Game game = new Game(board);
		
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				CheckersWindow window = new CheckersWindow(game);
				window.open();
			}
		});
		
	}
}
