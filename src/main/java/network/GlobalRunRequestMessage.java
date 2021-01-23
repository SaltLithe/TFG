package network;

import java.io.Serializable;

/**
 * Class acting as a message for users acting as clients to request a global run to the server
 * @author Carmen Gómez Moreno
 *
 */
public class GlobalRunRequestMessage implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8962556969283424840L;

	public GlobalRunRequestMessage(String name) {
		this.name = name;
	}
	
	public String name;
}
