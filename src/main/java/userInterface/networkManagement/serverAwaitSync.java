package userInterface.networkManagement;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import network.ServerHandler;
import userInterface.UIController;
import java.awt.Dialog.ModalityType;
import java.awt.Dialog.ModalExclusionType;

@SuppressWarnings("serial")
/**
 * UI Class used for users acting as servers as to update them of their sync
 * status TODO this should block the ui
 * 
 * @author Carmen Gómez Moreno
 *
 */
public class serverAwaitSync extends JDialog {

	int nClients;
	int clientsSynced;
	int clientsConnected;
	JLabel syncCountLabel;
	JLabel clientsConnectedLabel;
	ServerHandler parent;

	/**
	 * 
	 * @param nClients : Number of clients expected in this session
	 * @param parent   : The class that creates this dialog
	 */
	public serverAwaitSync(int nClients, ServerHandler parent) {

		this.parent = parent;
		this.nClients = nClients;
		clientsSynced = 0;
		clientsConnected = 0;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 440, 0 };
		gridBagLayout.rowHeights = new int[] { 37, 33, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		syncCountLabel = new JLabel("");
		updateSyncCount(0);

		clientsConnectedLabel = new JLabel(clientsConnected + " Clients connected.");
		GridBagConstraints gbc_clientsConnectedLabel = new GridBagConstraints();
		gbc_clientsConnectedLabel.insets = new Insets(0, 0, 5, 0);
		gbc_clientsConnectedLabel.gridx = 0;
		gbc_clientsConnectedLabel.gridy = 0;
		getContentPane().add(clientsConnectedLabel, gbc_clientsConnectedLabel);
		GridBagConstraints gbc_clientsConnectedLabel1 = new GridBagConstraints();
		gbc_clientsConnectedLabel1.insets = new Insets(0, 0, 5, 0);
		gbc_clientsConnectedLabel1.gridx = 0;
		gbc_clientsConnectedLabel1.gridy = 1;
		getContentPane().add(syncCountLabel, gbc_clientsConnectedLabel1);

		JButton closeSessionButton = new JButton("Close Session");
		GridBagConstraints gbc_closeSessionButton = new GridBagConstraints();
		gbc_closeSessionButton.insets = new Insets(0, 0, 5, 0);
		gbc_closeSessionButton.gridx = 0;
		gbc_closeSessionButton.gridy = 2;
		getContentPane().add(closeSessionButton, gbc_closeSessionButton);

		JButton cancelButton = new JButton("Cancel Sync");
		GridBagConstraints gbc_cancelButton = new GridBagConstraints();
		gbc_cancelButton.insets = new Insets(0, 0, 5, 0);
		gbc_cancelButton.gridx = 0;
		gbc_cancelButton.gridy = 3;
		getContentPane().add(cancelButton, gbc_cancelButton);

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				UIController.getInstance().getDeveloperComponent().disconnect();
				dispose();

			}

		});

		closeSessionButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				parent.closeServer();
				closeNumberOfClients();
				updateSyncCount(0);

			}

		});

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				UIController.getInstance().getDeveloperComponent().disconnect();
				dispose();

			}
		});

		setSize(new Dimension(getPreferredSize().width, 200));
		setVisible(true);
	}

	/**
	 * Method used to update the number or clients connected to the session
	 * 
	 * @param adding : The number to add or substract to the total client count
	 */
	public void updateConnectCount(int adding) {
		if (!((clientsConnected + adding) < 0)) {
			clientsConnected += adding;
			clientsConnectedLabel.setText(clientsConnected + " Clients connected.");

		}
	}

	/**
	 * Method used to update the number or clients synced to the session
	 * 
	 * @param adding : The number to add or substract to the total sync count
	 */
	public void updateSyncCount(int adding) {
		if (!((clientsSynced + adding) < 0)) {
			clientsSynced += adding;
			syncCountLabel.setText("Synced with " + clientsSynced + "/" + nClients + " clients.");
			if (nClients == clientsSynced) {
				parent.activateGlobal();
				dispose();
			}
		}

	}

	/**
	 * Support method used to set the number of expected clients to the number of
	 * clients currently connected when the server closes the session
	 */
	private void closeNumberOfClients() {
		nClients = clientsConnected;

	}
}
