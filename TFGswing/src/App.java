//

import java.net.UnknownHostException;

import javax.swing.SwingUtilities;

public class App {

	DeveloperMainFrame mainFrame;

	public static void main(String[] args) throws UnknownHostException {

		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {

				InitFrame frame = new InitFrame();
				// DeveloperMainFrame mainFrame = new DeveloperMainFrame();

			}

		});

		/*
		 * Socket socket = new Socket();
		 * 
		 * int port = 8080; SocketAddress remote = new InetSocketAddress(remoteaddr,
		 * port); try { socket.connect(remote); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
	}

}
