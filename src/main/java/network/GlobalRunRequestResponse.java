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
	private static final long serialVersionUID = 8776767744223310271L;

	

	public GlobalRunRequestResponse() {
	}

	public boolean ok;
	boolean canceled;
	public String name;
	public boolean noProject = false;

}
