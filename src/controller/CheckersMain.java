package controller;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import model.Board;
import player.ComputerPlayer;
import player.HumanPlayer;
import player.ServerPlayer;
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

		ComputerPlayer computer = new ComputerPlayer(GameConstants.THUNK_COLOR, board);
		final HumanPlayer user = new HumanPlayer(GameConstants.USER_COLOR, board, game);

		game.setComputer(computer);

		if (mode == GameConstants.SERVER_MODE) {
			final ServerPlayer server = new ServerPlayer(GameConstants.SERVER_COLOR, board, game);
			game.setOpponent(server);
			new Thread(new Runnable() {

				@Override
				public void run() {
					server.listen("1", "010101", "2");
				}

			}).start();
		} else {
			game.setOpponent(user);
		}

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				CheckersWindow window = new CheckersWindow(game, user, mode);
				window.open();
			}
		});

	}

}
