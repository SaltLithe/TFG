package TFG;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ConnectionDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	/**
	 * @wbp.nonvisual location=150,329
	 */
	private final JSplitPane splitPane;
	private JTextField serverNameField_S;
	private JTextField serverPortField_S;
	private JTextField clientPortField_S;
	private JTextField maxClientsField_S;
	private JTextField ipField_S;
	private JTextField clientNameField_C;
	private JTextField ipField_C;
	private JTextField clientIPField_C;
	private JTextField serverPortField_C;
	private JTextField clientPortField_C;
	private JCheckBox setServerCheck;
	private JCheckBox setClientCheck;
	private UIController controller;
	private DeveloperComponent dp;
	private JCheckBox autoStartCheck;
	private JCheckBox autoConnectCheck;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	public ConnectionDialog() {
		controller = UIController.getInstance();
		dp = controller.getDeveloperComponent();
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 97, 230, 97, 0 };
		gbl_contentPanel.rowHeights = new int[] { 23, 0 };
		gbl_contentPanel.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JPanel panel = new JPanel();
			GridBagConstraints gbc_panel = new GridBagConstraints();
			gbc_panel.anchor = GridBagConstraints.NORTH;
			gbc_panel.fill = GridBagConstraints.HORIZONTAL;
			gbc_panel.gridwidth = 3;
			gbc_panel.gridx = 0;
			gbc_panel.gridy = 0;
			contentPanel.add(panel, gbc_panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[] { 0 };
			gbl_panel.rowHeights = new int[] { 0 };
			gbl_panel.columnWeights = new double[] { Double.MIN_VALUE };
			gbl_panel.rowWeights = new double[] { Double.MIN_VALUE };
			panel.setLayout(gbl_panel);
		}
		{
			setServerCheck = new JCheckBox("Set as server");
			GridBagConstraints gbc_chckbxNewCheckBox_1 = new GridBagConstraints();
			gbc_chckbxNewCheckBox_1.anchor = GridBagConstraints.NORTHWEST;
			gbc_chckbxNewCheckBox_1.insets = new Insets(0, 0, 0, 5);
			gbc_chckbxNewCheckBox_1.gridx = 0;
			gbc_chckbxNewCheckBox_1.gridy = 0;
			contentPanel.add(setServerCheck, gbc_chckbxNewCheckBox_1);
			setServerCheck.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					JCheckBox cb = (JCheckBox) event.getSource();
					if (cb.isSelected()) {
						setClientCheck.setSelected(false);
						JPanel p = (JPanel) splitPane.getRightComponent();
						for (Component c : p.getComponents()) {

							c.setEnabled(false);

						}
						JPanel p2 = (JPanel) splitPane.getLeftComponent();
						for (Component c : p2.getComponents()) {

							c.setEnabled(true);

						}

					} else {
						// check box is unselected, do something else
					}
				}
			});

		}

		{
			setClientCheck = new JCheckBox("Set as client");
			GridBagConstraints gbc_chckbxNewCheckBox_2 = new GridBagConstraints();
			gbc_chckbxNewCheckBox_2.anchor = GridBagConstraints.NORTHWEST;
			gbc_chckbxNewCheckBox_2.gridx = 2;
			gbc_chckbxNewCheckBox_2.gridy = 0;
			contentPanel.add(setClientCheck, gbc_chckbxNewCheckBox_2);
			setClientCheck.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent event) {
					JCheckBox cb = (JCheckBox) event.getSource();
					if (cb.isSelected()) {
						setServerCheck.setSelected(false);
						JPanel p = (JPanel) splitPane.getLeftComponent();
						for (Component c : p.getComponents()) {

							c.setEnabled(false);

						}

						JPanel p2 = (JPanel) splitPane.getRightComponent();
						for (Component c : p2.getComponents()) {

							c.setEnabled(true);

						}
					} else {
						// check box is unselected, do something else
					}
				}
			});

		}

		splitPane = new JSplitPane();
		getContentPane().add(splitPane, BorderLayout.CENTER);
		{
			JPanel panel = new JPanel();
			splitPane.setLeftComponent(panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[] { 0, 0, 0 };
			gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
			gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
			gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
			panel.setLayout(gbl_panel);
			{
				JLabel lblNewLabel = new JLabel("Server name");
				GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
				gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel.gridx = 0;
				gbc_lblNewLabel.gridy = 0;
				panel.add(lblNewLabel, gbc_lblNewLabel);
			}
			{
				JPanel buttonPane = new JPanel();
				buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
				getContentPane().add(buttonPane, BorderLayout.SOUTH);
				{
					JButton okButton = new JButton("OK");
					okButton.setActionCommand("OK");
					buttonPane.add(okButton);
					getRootPane().setDefaultButton(okButton);

					okButton.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent event) {

							if (setServerCheck.isSelected()) {
								boolean autostart = autoStartCheck.isSelected();

								controller.run(() -> dp.setAsServer(serverNameField_S.getText(), ipField_S.getText(),
										Integer.valueOf(maxClientsField_S.getText()),
										Integer.valueOf(serverPortField_S.getText()),
										Integer.valueOf(clientPortField_S.getText()),
										Integer.valueOf(maxClientsField_S.getText()), autostart));

							} else if (setClientCheck.isSelected()) {

								boolean autoconnect = autoConnectCheck.isSelected();
								controller.run(() -> dp.setAsClient(ipField_C.getText(), clientIPField_C.getText(),
										Integer.valueOf(serverPortField_C.getText()),
										Integer.valueOf(clientPortField_C.getText()), autoconnect));
							}

						}
					});

				}
				{
					JButton cancelButton = new JButton("Cancel");
					cancelButton.setActionCommand("Cancel");
					buttonPane.add(cancelButton);
				}
			}

			{
				serverNameField_S = new JTextField();
				serverNameField_S.setText("a");
				GridBagConstraints gbc_serverNameField_S = new GridBagConstraints();
				gbc_serverNameField_S.insets = new Insets(0, 0, 5, 0);
				gbc_serverNameField_S.fill = GridBagConstraints.HORIZONTAL;
				gbc_serverNameField_S.gridx = 1;
				gbc_serverNameField_S.gridy = 0;
				panel.add(serverNameField_S, gbc_serverNameField_S);
				serverNameField_S.setColumns(10);
			}
			{
				JLabel lblNewLabel_1 = new JLabel("Server port");
				GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
				gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_1.gridx = 0;
				gbc_lblNewLabel_1.gridy = 1;
				panel.add(lblNewLabel_1, gbc_lblNewLabel_1);
			}
			{
				serverPortField_S = new JTextField();
				serverPortField_S.setText("8080");
				GridBagConstraints gbc_serverPortField_S = new GridBagConstraints();
				gbc_serverPortField_S.insets = new Insets(0, 0, 5, 0);
				gbc_serverPortField_S.fill = GridBagConstraints.HORIZONTAL;
				gbc_serverPortField_S.gridx = 1;
				gbc_serverPortField_S.gridy = 1;
				panel.add(serverPortField_S, gbc_serverPortField_S);
				serverPortField_S.setColumns(10);
			}
			{
				JLabel lblNewLabel_2 = new JLabel("Cient port");
				GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
				gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_2.gridx = 0;
				gbc_lblNewLabel_2.gridy = 2;
				panel.add(lblNewLabel_2, gbc_lblNewLabel_2);
			}
			{
				clientPortField_S = new JTextField();
				clientPortField_S.setText("8081");
				GridBagConstraints gbc_clientPortField_S = new GridBagConstraints();
				gbc_clientPortField_S.insets = new Insets(0, 0, 5, 0);
				gbc_clientPortField_S.fill = GridBagConstraints.HORIZONTAL;
				gbc_clientPortField_S.gridx = 1;
				gbc_clientPortField_S.gridy = 2;
				panel.add(clientPortField_S, gbc_clientPortField_S);
				clientPortField_S.setColumns(10);
			}
			{
				JLabel lblNewLabel_3 = new JLabel("Max clients");
				GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
				gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_3.gridx = 0;
				gbc_lblNewLabel_3.gridy = 3;
				panel.add(lblNewLabel_3, gbc_lblNewLabel_3);
			}
			{
				maxClientsField_S = new JTextField();
				maxClientsField_S.setText("1");
				GridBagConstraints gbc_maxClientsField_S = new GridBagConstraints();
				gbc_maxClientsField_S.insets = new Insets(0, 0, 5, 0);
				gbc_maxClientsField_S.fill = GridBagConstraints.HORIZONTAL;
				gbc_maxClientsField_S.gridx = 1;
				gbc_maxClientsField_S.gridy = 3;
				panel.add(maxClientsField_S, gbc_maxClientsField_S);
				maxClientsField_S.setColumns(10);
			}
			{
				JLabel lblNewLabel_4 = new JLabel("IP");
				GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
				gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_4.gridx = 0;
				gbc_lblNewLabel_4.gridy = 4;
				panel.add(lblNewLabel_4, gbc_lblNewLabel_4);
			}
			{
				ipField_S = new JTextField();
				ipField_S.setText("192.168.1.14");
				GridBagConstraints gbc_ipField_S = new GridBagConstraints();
				gbc_ipField_S.insets = new Insets(0, 0, 5, 0);
				gbc_ipField_S.fill = GridBagConstraints.HORIZONTAL;
				gbc_ipField_S.gridx = 1;
				gbc_ipField_S.gridy = 4;
				panel.add(ipField_S, gbc_ipField_S);
				ipField_S.setColumns(10);
			}
			{
				autoStartCheck = new JCheckBox("Autostart");
				autoStartCheck.setSelected(true);
				GridBagConstraints gbc_autoStartCheck = new GridBagConstraints();
				gbc_autoStartCheck.insets = new Insets(0, 0, 0, 5);
				gbc_autoStartCheck.gridx = 0;
				gbc_autoStartCheck.gridy = 5;
				panel.add(autoStartCheck, gbc_autoStartCheck);
			}
			{
				JList list = new JList();
				GridBagConstraints gbc_list = new GridBagConstraints();
				gbc_list.fill = GridBagConstraints.BOTH;
				gbc_list.gridx = 1;
				gbc_list.gridy = 5;
				panel.add(list, gbc_list);
			}
		}
		{
			JPanel panel = new JPanel();
			splitPane.setRightComponent(panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[] { 0, 0, 0 };
			gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0 };
			gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
			gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
			panel.setLayout(gbl_panel);
			{
				JLabel lblNewLabel_5 = new JLabel("Client name");
				GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
				gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_5.anchor = GridBagConstraints.EAST;
				gbc_lblNewLabel_5.gridx = 0;
				gbc_lblNewLabel_5.gridy = 0;
				panel.add(lblNewLabel_5, gbc_lblNewLabel_5);
			}
			{
				clientNameField_C = new JTextField();
				clientNameField_C.setText("a");
				GridBagConstraints gbc_clientNameField_C = new GridBagConstraints();
				gbc_clientNameField_C.insets = new Insets(0, 0, 5, 0);
				gbc_clientNameField_C.fill = GridBagConstraints.HORIZONTAL;
				gbc_clientNameField_C.gridx = 1;
				gbc_clientNameField_C.gridy = 0;
				panel.add(clientNameField_C, gbc_clientNameField_C);
				clientNameField_C.setColumns(10);
			}
			{
				JLabel lblNewLabel_6 = new JLabel("Server IP");
				GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
				gbc_lblNewLabel_6.anchor = GridBagConstraints.EAST;
				gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_6.gridx = 0;
				gbc_lblNewLabel_6.gridy = 1;
				panel.add(lblNewLabel_6, gbc_lblNewLabel_6);
			}
			{
				ipField_C = new JTextField();
				ipField_C.setText("192.168.1.14");
				GridBagConstraints gbc_ipField_C = new GridBagConstraints();
				gbc_ipField_C.insets = new Insets(0, 0, 5, 0);
				gbc_ipField_C.fill = GridBagConstraints.HORIZONTAL;
				gbc_ipField_C.gridx = 1;
				gbc_ipField_C.gridy = 1;
				panel.add(ipField_C, gbc_ipField_C);
				ipField_C.setColumns(10);
			}
			{
				JLabel lblNewLabel_7 = new JLabel("Server port");
				GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
				gbc_lblNewLabel_7.anchor = GridBagConstraints.EAST;
				gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_7.gridx = 0;
				gbc_lblNewLabel_7.gridy = 2;
				panel.add(lblNewLabel_7, gbc_lblNewLabel_7);
			}
			{
				serverPortField_C = new JTextField();
				serverPortField_C.setText("8080");
				GridBagConstraints gbc_serverPortField_C = new GridBagConstraints();
				gbc_serverPortField_C.insets = new Insets(0, 0, 5, 0);
				gbc_serverPortField_C.fill = GridBagConstraints.HORIZONTAL;
				gbc_serverPortField_C.gridx = 1;
				gbc_serverPortField_C.gridy = 2;
				panel.add(serverPortField_C, gbc_serverPortField_C);
				serverPortField_C.setColumns(10);
			}
			{
				JLabel lblNewLabel_8 = new JLabel("Client port");
				GridBagConstraints gbc_lblNewLabel_8 = new GridBagConstraints();
				gbc_lblNewLabel_8.anchor = GridBagConstraints.EAST;
				gbc_lblNewLabel_8.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_8.gridx = 0;
				gbc_lblNewLabel_8.gridy = 3;
				panel.add(lblNewLabel_8, gbc_lblNewLabel_8);
			}
			{
				clientPortField_C = new JTextField();
				clientPortField_C.setText("8081");
				GridBagConstraints gbc_clientPortField_C = new GridBagConstraints();
				gbc_clientPortField_C.insets = new Insets(0, 0, 5, 0);
				gbc_clientPortField_C.fill = GridBagConstraints.HORIZONTAL;
				gbc_clientPortField_C.gridx = 1;
				gbc_clientPortField_C.gridy = 3;
				panel.add(clientPortField_C, gbc_clientPortField_C);
				clientPortField_C.setColumns(10);
			}
			{
				JLabel lblNewLabel_9 = new JLabel("Client IP");
				GridBagConstraints gbc_lblNewLabel_9 = new GridBagConstraints();
				gbc_lblNewLabel_9.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_9.gridx = 0;
				gbc_lblNewLabel_9.gridy = 4;
				panel.add(lblNewLabel_9, gbc_lblNewLabel_9);
			}
			{
				clientIPField_C = new JTextField();
				clientIPField_C.setText("192.168.1.14");
				GridBagConstraints gbc_clientIPField_C = new GridBagConstraints();
				gbc_clientIPField_C.insets = new Insets(0, 0, 5, 0);
				gbc_clientIPField_C.fill = GridBagConstraints.HORIZONTAL;
				gbc_clientIPField_C.gridx = 1;
				gbc_clientIPField_C.gridy = 4;
				panel.add(clientIPField_C, gbc_clientIPField_C);
				clientIPField_C.setColumns(10);
			}
			{
				autoConnectCheck = new JCheckBox("Autoconnect");
				autoConnectCheck.setSelected(true);
				GridBagConstraints gbc_auu = new GridBagConstraints();
				gbc_auu.insets = new Insets(0, 0, 0, 5);
				gbc_auu.gridx = 0;
				gbc_auu.gridy = 5;
				panel.add(autoConnectCheck, gbc_auu);
			}
			{
				JList list = new JList();
				GridBagConstraints gbc_list = new GridBagConstraints();
				gbc_list.fill = GridBagConstraints.BOTH;
				gbc_list.gridx = 1;
				gbc_list.gridy = 5;
				panel.add(list, gbc_list);
			}
		}
		splitPane.setDividerLocation(210);

		setServerCheck.doClick();
	}
}
