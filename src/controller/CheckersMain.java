package controller;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import model.Board;
import view.CheckersWindow;

public class CheckersMain {

	public static void main(String[] args) {
		
		/* Prompt the user to choose a mode */
		Object[] options = {"Human vs Computer", "Computer vs Server"};
		final int mode = JOptionPane.showOptionDialog(null,
				"Please choose a game mode",
				"Checkers Game Mode",
				JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null, 
				options,
				options[0]);
		
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

		if (mode == GameConstants.SERVER_MODE) {
			/* Create a ServerListener to listen for messages from the server */
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				CheckersWindow window = new CheckersWindow(game, mode);
				window.open();
			}
		});

	}

}
