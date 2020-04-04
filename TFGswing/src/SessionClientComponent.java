import java.net.Socket;

public class SessionClientComponent extends SessionComponent {

	private Socket clientSocket;

	public SessionClientComponent(Socket clientSocket) {
		this.clientSocket = clientSocket;
		// TODO Auto-generated constructor stub
	}

}
