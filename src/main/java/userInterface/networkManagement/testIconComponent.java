package userInterface.networkManagement;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class testIconComponent extends JPanel {

	protected String ImageByteData = null;
	public String imagepath = "Icons\\default_Profile.png";
	public String iconpath = "Icons\\";
	
	public String chosenName = "Kermit";
	public Color chosenColor = Color.BLUE;
	public JLabel imagelabel;
	public Image raw = null;

	
private void decoder(String base64Image) {
  try  {
    byte[] imageByteArray = Base64.getDecoder().decode(base64Image);
  
    ByteArrayInputStream bais = new ByteArrayInputStream(imageByteArray);
    raw = ImageIO.read(bais);
  } catch (FileNotFoundException e) {
    System.out.println("Image not found" + e);
  } catch (IOException ioe) {
    System.out.println("Exception while reading the Image " + ioe);
  }
}
	
	private void  encoder(String imagePath) {
		  String base64Image = "";
		  File file = new File(imagePath);
		  try (FileInputStream imageInFile = new FileInputStream(file)) {
		    // Reading a Image file from file system
		    byte imageData[] = new byte[(int) file.length()];
		    imageInFile.read(imageData);
		    base64Image = Base64.getEncoder().encodeToString(imageData);
		  } catch (FileNotFoundException e) {
		    System.out.println("Image not found" + e);
		  } catch (IOException ioe) {
		    System.out.println("Exception while reading the Image " + ioe);
		  }
		 ImageByteData =  base64Image;
		}
	
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
		Image tmp = raw.getScaledInstance(setWidth, setHeight, Image.SCALE_SMOOTH);
		BufferedImage tmpBuffered = new BufferedImage(tmp.getWidth(null), tmp.getHeight(null),
				BufferedImage.TYPE_INT_RGB);
		Graphics bg = tmpBuffered.getGraphics();
		bg.drawImage(tmp, 0, 0, null);
		bg.dispose();
		/*
		 * ByteArrayOutputStream baos = new ByteArrayOutputStream(); try {
		 * ImageIO.write(tmpBuffered, "jpg", baos ); } catch (IOException e) {
		 * e.printStackTrace(); } try { baos.close(); } catch (IOException e) { // TODO
		 * Auto-generated catch block e.printStackTrace(); }
		 */
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

	public testIconComponent(String image, Color color, String name, boolean test) {

		if (image != null) {
			imagepath = image;
		}

		try {
			File f = new File(imagepath);
			raw = ImageIO.read(f);
			encoder(imagepath);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

		createTestIconComponent(name, color, test);

	}

	public testIconComponent(int colorNum, String image, String name) {

		Color color = new Color(colorNum);

		/*
		ByteArrayInputStream bais = new ByteArrayInputStream(string);
		BufferedImage retrieved = null;
		try {
			retrieved = ImageIO.read(bais);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ByteArrayOutputStream baos = new ByteArrayOutputStream(string.length);
		ImageOutputStream ios = null;
		try {
			baos.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			ios = ImageIO.createImageOutputStream(baos);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			ImageIO.write(retrieved, "jpg", ios);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		raw = (Image) retrieved;
		*/
		decoder(image );
		createTestIconComponent(name, color, false);

	}

}
