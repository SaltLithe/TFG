package userInterface.networkManagement;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/*Esta clase contiene un panel en el que de momento se añaden labels con el dueño de sesion y su ip
 * en el futuro tiene que ser capaz de sacar notificaciones cuando ocurran eventos como que se conecte un 
 * usuario , se comience una ejecucion global , etc 
 */

public class UsersPanel extends JPanel {

	JLabel sessionowner;
	JLabel ipIndicator;
	JButton CreateJoinSessionButton;

	public UsersPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 92, 35, 0, 37, 22, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		setLayout(gridBagLayout);

		CreateJoinSessionButton = new JButton("Create/Join Session");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnNewButton.gridwidth = 4;
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 5);
		gbc_btnNewButton.gridx = 0;
		gbc_btnNewButton.gridy = 0;
		add(CreateJoinSessionButton, gbc_btnNewButton);

		JLabel ServerName = new JLabel("Server name : ");
		GridBagConstraints gbc_ServerName = new GridBagConstraints();
		gbc_ServerName.gridwidth = 2;
		gbc_ServerName.insets = new Insets(0, 0, 5, 5);
		gbc_ServerName.gridx = 0;
		gbc_ServerName.gridy = 1;
		add(ServerName, gbc_ServerName);

		JLabel ServerNameLabel = new JLabel("");
		GridBagConstraints gbc_ServerNameLabel = new GridBagConstraints();
		gbc_ServerNameLabel.anchor = GridBagConstraints.WEST;
		gbc_ServerNameLabel.gridwidth = 2;
		gbc_ServerNameLabel.insets = new Insets(0, 0, 5, 5);
		gbc_ServerNameLabel.gridx = 2;
		gbc_ServerNameLabel.gridy = 1;
		add(ServerNameLabel, gbc_ServerNameLabel);

		JLabel ServerIP = new JLabel("Server IP : ");
		GridBagConstraints gbc_ServerIP = new GridBagConstraints();
		gbc_ServerIP.gridwidth = 2;
		gbc_ServerIP.insets = new Insets(0, 0, 5, 5);
		gbc_ServerIP.gridx = 0;
		gbc_ServerIP.gridy = 2;
		add(ServerIP, gbc_ServerIP);

		JLabel ServerIPLabel = new JLabel("");
		GridBagConstraints gbc_ServerIPLabel = new GridBagConstraints();
		gbc_ServerIPLabel.anchor = GridBagConstraints.WEST;
		gbc_ServerIPLabel.insets = new Insets(0, 0, 5, 5);
		gbc_ServerIPLabel.gridx = 2;
		gbc_ServerIPLabel.gridy = 2;
		add(ServerIPLabel, gbc_ServerIPLabel);

		JLabel ClientList = new JLabel("Client List : ");
		GridBagConstraints gbc_ClientList = new GridBagConstraints();
		gbc_ClientList.gridwidth = 2;
		gbc_ClientList.insets = new Insets(0, 0, 0, 5);
		gbc_ClientList.gridx = 0;
		gbc_ClientList.gridy = 4;
		add(ClientList, gbc_ClientList);

		CreateJoinSessionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				ConnectionDialog d = new ConnectionDialog();
				d.setVisible(true);

			}

		});

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
