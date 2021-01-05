package network;

import java.io.Serializable;

public class ResponseCreateFileMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5597576436024776409L;
	/**
	 * 
	 */


	
	public ResponseCreateFileMessage() {}
	
	
	
	public String path;
	public String type;
	public String contents;
	public String newname;

	
}
