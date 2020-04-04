package ui;
import java.awt.Color;

import javax.swing.JButton;

public class FileExplorerButton extends JButton {

	private String classname;
	private Color selectedColor = new Color(173, 173, 173);

	public FileExplorerButton(String name) {
		classname = name;
		this.setName(name);

	}

}
