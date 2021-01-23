package network;

import java.io.Serializable;
/**
 * Class acting as a message to indicate to a server that a clients needs it's workspace information
 * @author Carmen Gómez Moreno
 *
 */
public class RequestWorkspaceMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 305008218692678861L;
	public String clientName;

	public RequestWorkspaceMessage(String clientName) {
		this.clientName = clientName;
	}

}
