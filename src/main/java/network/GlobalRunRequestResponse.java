package network;

import java.io.Serializable;

/**
 * Class acting as message used by users acting as clients to signal ready to
 * the server
 * 
 * @author Usuario
 *
 */
public class GlobalRunRequestResponse implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3905619381952337113L;

	public GlobalRunRequestResponse() {
	}

	boolean ok;
	boolean canceled;

}
