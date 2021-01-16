package network;

import java.io.Serializable;

public class HighLightMessage implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 7661321599895855978L;
	public HighLightMessage (int linestart , int lineend , String name, String keypath) {
		
		this.linestart = linestart;
		this.lineend = lineend;
		this.name = name;
		this.keypath = keypath;
		
	}
	
	public int linestart;
	public int lineend;
	public String name; 
	public String keypath;
}
