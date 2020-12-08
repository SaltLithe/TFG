package network;

import java.awt.AWTEvent;
import java.io.Serializable;

public class WriteMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6106677430760056507L;

	/**
	 * 
	 */

	public WriteMessage() {
	}

	public String path; 
	public String changes;
	public int offset; 
	public int lenght;
	public boolean adding;

}
