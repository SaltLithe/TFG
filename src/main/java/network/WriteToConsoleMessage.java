package network;

import java.io.Serializable;

public class WriteToConsoleMessage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1267550844501389017L;
	public WriteToConsoleMessage(String line) {
		
		this.line = line;
		
	}
	public String line;

}
