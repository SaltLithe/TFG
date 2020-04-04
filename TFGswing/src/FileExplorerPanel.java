
import java.util.HashMap;
//

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

class FileExplorerPanel extends JPanel {
	private JPanel fileArea;
	private FileExplorerToolbar fileExplorerToolbar;

	private HashMap<String, JButton> buttons = new HashMap<String, JButton>();

	public FileExplorerPanel() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		setVisible(true);

	}

	public boolean containsButton(String name) {

		return buttons.containsKey(name);
	}

	public void addButton(JButton newbutton) {

		add(newbutton);
		buttons.put(newbutton.getName(), newbutton);

	}

}
