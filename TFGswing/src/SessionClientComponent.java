import java.net.Socket;

public class SessionClientComponent extends DeveloperComponent {

	private Socket clientSocket;

	public SessionClientComponent(DeveloperMainFrame dpmf, Socket clientSocket, String ip, String password) {
		super(dpmf);
		this.clientSocket = clientSocket;
		// TODO Auto-generated constructor stub
	}

}
