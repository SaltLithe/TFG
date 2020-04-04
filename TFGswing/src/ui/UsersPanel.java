package ui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class UsersPanel extends JPanel {

	JLabel sessionowner;
	JLabel ipIndicator;

	public UsersPanel() {

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

	}

	public void setSessionOwner(String name) {
		sessionowner = new JLabel("Owner : " + name);
		add(sessionowner);
		updateUI();
		// TODO Auto-generated method stub

	}

	public void setIpIndicator(String remoteaddr) {
		ipIndicator = new JLabel(remoteaddr);
		add(ipIndicator);
		updateUI();
		// TODO Auto-generated method stub

	}

}
