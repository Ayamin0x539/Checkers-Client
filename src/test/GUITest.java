package test;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import view.CheckersWindow;

public class GUITest {

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
		CheckersWindow window = new CheckersWindow();
	}
}
