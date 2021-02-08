package userInterface.networkManagement;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.io.FileUtils;

import fileManagement.WorkSpaceManager;

@SuppressWarnings("serial")
/**
 * UI class that represents the profile icon of the users , contains methods for
 * adding images from file paths and base64 Strings and displays it alongside a
 * color and a name for each use 
 * 
 * @author Usuario
 *
 */
public class ProfileIIconComponent extends JPanel {

	public String imagepath = "/resources/images/default_Profile.png";
	public String iconpath = "/resources/images/";

	public String chosenName = "";
	public Color chosenColor = Color.BLUE;
	public JLabel imagelabel;
	public Image raw = null;
	public int clientID;

	protected String ImageByteData = null;

	/**
	 * 
	 * @param image : A String containing an image path 
	 * @param color : An object representing the color chosen by the user
	 * @param name : The users name
	 * @param test : A flag indicathing if this component should be rendered with a color 
	 */
	public ProfileIIconComponent(String image, Color color, String name, boolean test) {
	
		if (image != null) {
			imagepath = image;
		}
	
		try {
			File f; 
			try {
			InputStream imageStream = WorkSpaceManager.class.getResourceAsStream(imagepath);
			
			 f = new File("src/main/resources/tempImage.tmp");
		    FileUtils.copyInputStreamToFile(imageStream, f);
			}
			catch(Exception e) {
			 f =new File(imagepath);
				
			
			}

			raw = ImageIO.read(f);
			Image rawRescale = raw.getScaledInstance(60, 60, Image.SCALE_FAST);
			BufferedImage bimage = new BufferedImage(rawRescale.getWidth(null), rawRescale.getHeight(null),
					BufferedImage.TYPE_INT_ARGB);
	
			Graphics2D bGr = bimage.createGraphics();
			bGr.drawImage(rawRescale, 0, 0, null);
			bGr.dispose();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bimage, getFormat(imagepath), baos);
			byte[] bytes = baos.toByteArray();
	
			encoder(bytes);
	
		} catch (IOException e) {
			e.printStackTrace();
		}
	
		createTestIconComponent(name, color, test);
	
	}

	/**
	 * 
	 * @param colorNum : An integer representing the color of the user
	 * @param image : A base64 String containing the user's image data
	 * @param name : The name of the user
	 * @param clientID : The id of the user for a session
	 */
	public ProfileIIconComponent(int colorNum, String image, String name, int clientID) {
	
		this.clientID = clientID;
		Color color = new Color(colorNum);
	
		decoder(image);
		createTestIconComponent(name, color, false);
	
	}

	/**
	 * Method that actually creates this component , called by the two constructors
	 * @param name : The name of the user
	 * @param color : An object representing the color chosen by the user
	 * @param test : A flag that indicates if the color should be rendered 
	 */
	private void createTestIconComponent(String name, Color color, boolean test) {
		if (color != null) {
	
			chosenColor = color;
	
		}
	
		if (name != null) {
			chosenName = name;
		}
		int setWidth = 60;
		int setHeight = 60;
		int diameter = Math.min(setWidth, setHeight);
		Image tmp = raw.getScaledInstance(setWidth, setHeight, Image.SCALE_FAST);
		BufferedImage tmpBuffered = new BufferedImage(tmp.getWidth(null), tmp.getHeight(null),
				BufferedImage.TYPE_INT_RGB);
		Graphics bg = tmpBuffered.getGraphics();
		bg.drawImage(tmp, 0, 0, null);
		bg.dispose();

		BufferedImage master = new BufferedImage(setWidth, setHeight, BufferedImage.TYPE_INT_ARGB);
		BufferedImage mask = new BufferedImage(setWidth, setHeight, BufferedImage.TYPE_INT_ARGB);
		BufferedImage background = new BufferedImage(setWidth + 10, setHeight + 10, BufferedImage.TYPE_INT_ARGB);
	
		Graphics2D g2d1 = background.createGraphics();
		Graphics2D g2d2 = mask.createGraphics();
		if (!test) {
			g2d1.setPaint(chosenColor);
			g2d1.fillOval(0, 0, diameter + 10 - 1, diameter + 10 - 1);
			g2d1.drawImage(background, 0, 0, null);
		}
		g2d1.dispose();
	
		g2d2.fillOval(0, 0, diameter - 1, diameter - 1);
		g2d2.dispose();
	
		Graphics2D g2d3 = master.createGraphics();
		g2d3.drawImage(tmp, 0, 0, null);
		g2d3.dispose();
	
		BufferedImage maskedIcon = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
		g2d2 = maskedIcon.createGraphics();
		int x2 = (diameter - master.getWidth()) / 2;
		int y2 = (diameter - master.getHeight()) / 2;
		g2d2.drawImage(master, x2, y2, null);
		g2d2.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_IN));
		g2d2.drawImage(mask, 0, 0, null);
		g2d2.dispose();
	
		ImageIcon backgroundicon = (new ImageIcon(background));
		ImageIcon imageicon = (new ImageIcon(maskedIcon));
		JLabel backgroundlabel = new JLabel(backgroundicon);
		backgroundlabel.setLayout(new FlowLayout());
		backgroundlabel.setSize(100, 100);
		imagelabel = new JLabel(imageicon);
		imagelabel.setSize(20, 20);
	
		backgroundlabel.add(imagelabel);
	
		add(backgroundlabel);
	
		JLabel namelabel = new JLabel(chosenName);
		add(namelabel);
	
		setVisible(true);
	}

	/**
	 * Support method that decodes a base64 String into an image
	 * @param base64Image : The base64 string that represents an image
	 */
	private void decoder(String base64Image) {
		try {

			byte[] imageByteArray = Base64.getDecoder().decode(base64Image);

			ByteArrayInputStream bais = new ByteArrayInputStream(imageByteArray);
			raw = ImageIO.read(bais);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	/**
	 * Support method that encodes an Image into a base64 string
	 * @param image : The image to be decoded 
	 */
	private void encoder(byte[] image) {
		String base64Image = "";
		try {
			
			base64Image = Base64.getEncoder().encodeToString(image);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ImageByteData = base64Image;
	}

	/**
	 * Support method that , given the path of an image , returns its format
	 * @param path : The path of an image 
	 * @return a String containing an image's format
	 */
	private String getFormat(String path) {
		return path.substring(path.lastIndexOf(".") + 1, path.length());
	}

}
