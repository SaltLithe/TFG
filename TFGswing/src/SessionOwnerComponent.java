
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.ServerSocket;

public class SessionOwnerComponent extends DeveloperComponent {

	ServerSocket serverSocket;
	DataOutputStream os;
	BufferedReader is;

	public SessionOwnerComponent(DeveloperMainFrame dpmf, ServerSocket server) {

		super(dpmf);

		serverSocket = server;

		// os = new DataOutputStream(serverSocket.getOutputStream());
		// is = new BufferedReader(new
		// InputStreamReader(serverSocket.getInputStream()));
		// TODO Auto-generated constructor stub
	}

}
