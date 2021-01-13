package network;

import java.io.Serializable;

public class ImageDataMessage implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7563569887642449884L;
	
	public ImageDataMessage(String chosenImage , int color , String name , boolean isServer) {
		this.isServer = isServer;
		this.image = chosenImage;
		this.color = color;
		this.name = name; 
	}
	
	public boolean isServer;
	public String image;
	public int color;
	public String name; 
	public int clientID;
	
	
}
