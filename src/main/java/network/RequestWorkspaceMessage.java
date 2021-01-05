package network;

import java.io.Serializable;

public class RequestWorkspaceMessage  implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 305008218692678861L;
	public String clientName;
	
	public RequestWorkspaceMessage(String clientName) {
		this.clientName = clientName; 
	}
	
	
	
}
