package userInterface.networkManagement;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class testIconComponent extends JPanel {

	public String imagepath = "Icons\\default_Profile.png";
	public String chosenName = "Kermit";
	public Color chosenColor = Color.GREEN;
	public JLabel imagelabel;

	public testIconComponent(String image, Color color, String name, boolean test) {

		if (color != null) {

			chosenColor = color;

		}

		if (name != null) {
			chosenName = name;
		}

		if (image != null) {
			imagepath = image;
		}

		int setWidth = 60;
		int setHeight = 60;

		Image raw = null;
		try {
			raw = ImageIO.read(new File(imagepath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		int diameter = Math.min(setWidth, setHeight);
		Image tmp = raw.getScaledInstance(setWidth, setHeight, Image.SCALE_SMOOTH);
		BufferedImage master = new BufferedImage(setWidth, setHeight, BufferedImage.TYPE_INT_ARGB);
		BufferedImage mask = new BufferedImage(setWidth, setHeight, BufferedImage.TYPE_INT_ARGB);
		BufferedImage background = new BufferedImage(setWidth + 10, setHeight + 10, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d1 = background.createGraphics();
		Graphics2D g2d2 = mask.createGraphics();
		if(!test) {
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

}
