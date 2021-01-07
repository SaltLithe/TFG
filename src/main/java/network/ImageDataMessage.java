package network;

import java.io.Serializable;

public class ImageDataMessage implements Serializable{

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8780145969106765248L;
	public ImageDataMessage(String chosenImage , int color , String name) {
		this.image = chosenImage;
		this.color = color;
		this.name = name; 
	}
	
	public String image;
	public int color;
	public String name; 
	
	
}
