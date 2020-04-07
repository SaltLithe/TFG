import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

public class App {

	DeveloperMainFrame mainFrame;

	public static void main(String[] args) throws UnknownHostException {

		// Initialices the first part of the application in a new thread
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				InitFrame frame = new InitFrame();

			}

		});

	}

}
