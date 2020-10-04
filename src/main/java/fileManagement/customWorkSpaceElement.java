package fileManagement;

import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

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

	public customWorkSpaceElement(String name, String path) {

		this.name = name;
		this.path = path;
		setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		nameLabel = new JLabel(name + ": " + path);
		add(nameLabel);

		removeButton = new JButton("Remove");
		add(removeButton);

		deleteLocateButton = new JButton("");
		add(deleteLocateButton);

		nameLabel = new JLabel(name + ": " + path);
		this.setSize(this.getPreferredSize());

		checkExistance();

		this.setVisible(true);

	}

	private void checkExistance() {

		File workspacefile = new File(path);
		if (workspacefile.exists()) {
			deleteLocateButton.setText("Delete");

		} else {
			deleteLocateButton.setText("Locate");
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
