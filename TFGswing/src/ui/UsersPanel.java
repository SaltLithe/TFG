package ui;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*Esta clase contiene un panel en el que de momento se añaden labels con el dueño de sesion y su ip
 * en el futuro tiene que ser capaz de sacar notificaciones cuando ocurran eventos como que se conecte un 
 * usuario , se comience una ejecucion global , etc 
 */

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
