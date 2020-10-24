package fileManagement;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.io.File;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Component;

public class customWorkSpaceElement extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private String path;

	private JButton removeButton;
	private JButton deleteLocateButton;
	private JLabel nameLabel;

	private Color notFoundColor;
	
	
	private int forcedHeight = 30;
	private int forcedWidth = 0; 

	public customWorkSpaceElement(String name, String path) {

		this.name = name;
		this.path = path;
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		notFoundColor = new Color(222, 57, 49);

		nameLabel = new JLabel(name + ": " + path);

		removeButton = new JButton("Remove");
		add(removeButton);

		deleteLocateButton = new JButton("");
		add(deleteLocateButton);

		nameLabel = new JLabel(name + ": " + path);
		//this.setSize(this.getPreferredSize());

		checkExistance();

		nameLabel.setHorizontalAlignment(SwingConstants.LEFT);
		nameLabel.setAlignmentX(0.5f);

		removeButton.setHorizontalAlignment(SwingConstants.LEFT);
		removeButton.setAlignmentX(0.5f);

		deleteLocateButton.setHorizontalAlignment(SwingConstants.LEFT);
		deleteLocateButton.setAlignmentX(0.5f);

		this.setAlignmentX(Component.LEFT_ALIGNMENT);

		Font font = nameLabel.getFont();
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		nameLabel.setFont(font.deriveFont(attributes));
		
		
		
		add(nameLabel);
		
		forcedWidth = this.getPreferredSize().width;
		this.setMinimumSize(new Dimension(forcedWidth,forcedHeight));
		this.setPreferredSize(new Dimension(forcedWidth,forcedHeight));
		this.setMaximumSize(new Dimension(forcedWidth,forcedHeight));
		

		this.setVisible(true);

	}

	private void checkExistance() {

		File workspacefile = new File(path);
		if (workspacefile.exists()) {
			deleteLocateButton.setText("Delete");

		} else {
			deleteLocateButton.setText("Locate");
			nameLabel.setForeground(notFoundColor);

		}

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
