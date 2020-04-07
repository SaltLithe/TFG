import java.net.Socket;

public class MultiServerThread extends Thread {
	private Socket socket = null;

	public MultiServerThread(Socket socket2) {
		super("MultiServerThread");
		this.socket = socket2;
	}

	public void run() {

	}
}