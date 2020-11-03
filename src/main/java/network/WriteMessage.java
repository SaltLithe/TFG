package network;

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

	public boolean adding;
	public int lenght;
	public int caret;

	public String added;
	public Object path;

}
