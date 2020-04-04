
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

public class SessionOwnerComponent extends SessionComponent {

	ServerSocket serverSocket;
	DataOutputStream os;
	BufferedReader is;

	public SessionOwnerComponent(ServerSocket server) {

		serverSocket = server;
		boolean listening = true;

		new Thread(() -> {
			while (listening) {
				try {

					serverSocket.accept();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

		// os = new DataOutputStream(serverSocket.getOutputStream());
		// is = new BufferedReader(new
		// InputStreamReader(serverSocket.getInputStream()));
		// TODO Auto-generated constructor stub
	}

}
