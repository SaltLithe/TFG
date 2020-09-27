package TFG;

import javax.swing.JButton;

public class CustomFileButton extends JButton {

	private static final long serialVersionUID = 1L;
	public String subname;
	public String extension;
	public String fullName;

	public void setCustomName(String name, String extension) {

		this.fullName = name;
		this.extension = extension;
		this.subname = name.replace(extension, "");

	}

	public CustomFileButton(String name) {
		super(name);
	}

	public void setJButtonName() {
		new JButton(fullName);

	}

}
