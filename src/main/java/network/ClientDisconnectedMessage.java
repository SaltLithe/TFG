package network;

import java.io.Serializable;

/**
 * Class acting as a message to indicate client disconnection
 * @author Carmen Gómez Moreno
 *
 */
public class ClientDisconnectedMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2515833657679366870L;

	public ClientDisconnectedMessage(int clientID) {
		this.clientID = clientID;
	}

	public int clientID;
}
