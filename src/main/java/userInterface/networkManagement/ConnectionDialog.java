package userInterface.networkManagement;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import core.DeveloperComponent;
import userInterface.DeveloperMainFrameWrapper;
import userInterface.UIController;

public class ConnectionDialog extends JFrame {

	private final JPanel contentPanel = new JPanel();
	/**
	 * @wbp.nonvisual location=150,329
	 */
	private final JSplitPane splitPane;
	private JTextField serverNameField_S;
	private JTextField serverPortField_S;
	private JTextField maxClientsField_S;
	private JTextField ipField_S;
	private JTextField clientNameField_C;
	private JTextField ipField_C;
	private JTextField clientIPField_C;
	private JTextField serverPortField_C;
	private JCheckBox setServerCheck;
	private JCheckBox setClientCheck;
	private UIController controller;
	private DeveloperComponent dp;
	private JCheckBox autoStartCheck;
	private JCheckBox autoConnectCheck;
	private JColorChooser colorChoosersv;
	private JColorChooser colorChoosercl;
	private testIconComponent serverImageLabel;
	private testIconComponent imageLabelClient;
	private JButton serverImageButton;
	private JButton clientImageButton;
	private JFileChooser imageChooser;
	private GridBagConstraints gbc_imageLabelClient;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	public ConnectionDialog() {
		imageChooser = new JFileChooser();
		imageChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "jpg", "png"));
		controller = UIController.getInstance();
		dp = controller.getDeveloperComponent();
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
		{
			setServerCheck = new JCheckBox("Set as server");
			setServerCheck.setHorizontalAlignment(SwingConstants.LEFT);
			contentPanel.add(setServerCheck);
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
			setClientCheck.setAlignmentX(Component.RIGHT_ALIGNMENT);
			contentPanel.add(setClientCheck);
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
			gbl_panel.columnWidths = new int[] { 82, 597, 0 };
			gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 53, 65, 0 };
			gbl_panel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
			gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
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
									
										Integer.valueOf(maxClientsField_S.getText()), autostart));
										dp.setIcon(colorChoosersv.getColor(), serverImageLabel.imagepath , serverNameField_S.getText());

							} else if (setClientCheck.isSelected()) {

								boolean autoconnect = autoConnectCheck.isSelected();
								controller.run(() -> dp.setAsClient(ipField_C.getText(), clientIPField_C.getText(),
										Integer.valueOf(serverPortField_C.getText()),
										autoconnect));
								dp.setIcon(colorChoosercl.getColor(), imageLabelClient.imagepath , clientNameField_C.getText());

							}
							dispose(); 
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
				serverNameField_S.setHorizontalAlignment(SwingConstants.LEFT);
				serverNameField_S.setText("a");
				GridBagConstraints gbc_serverNameField_S = new GridBagConstraints();
				gbc_serverNameField_S.fill = GridBagConstraints.HORIZONTAL;
				gbc_serverNameField_S.insets = new Insets(0, 0, 5, 0);
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
				JLabel lblNewLabel_3 = new JLabel("Max clients");
				GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
				gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_3.gridx = 0;
				gbc_lblNewLabel_3.gridy = 2;
				panel.add(lblNewLabel_3, gbc_lblNewLabel_3);
			}
			{
				maxClientsField_S = new JTextField();
				maxClientsField_S.setText("1");
				GridBagConstraints gbc_maxClientsField_S = new GridBagConstraints();
				gbc_maxClientsField_S.insets = new Insets(0, 0, 5, 0);
				gbc_maxClientsField_S.fill = GridBagConstraints.HORIZONTAL;
				gbc_maxClientsField_S.gridx = 1;
				gbc_maxClientsField_S.gridy = 2;
				panel.add(maxClientsField_S, gbc_maxClientsField_S);
				maxClientsField_S.setColumns(10);
			}
			{
				JLabel lblNewLabel_4 = new JLabel("IP");
				GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
				gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_4.gridx = 0;
				gbc_lblNewLabel_4.gridy = 3;
				panel.add(lblNewLabel_4, gbc_lblNewLabel_4);
			}
			{
				ipField_S = new JTextField();
				ipField_S.setText("192.168.1.15");
				GridBagConstraints gbc_ipField_S = new GridBagConstraints();
				gbc_ipField_S.fill = GridBagConstraints.HORIZONTAL;
				gbc_ipField_S.insets = new Insets(0, 0, 5, 0);
				gbc_ipField_S.gridx = 1;
				gbc_ipField_S.gridy = 3;
				panel.add(ipField_S, gbc_ipField_S);
				ipField_S.setColumns(10);
			}
			{
				JLabel lblNewLabel_2 = new JLabel("Color");
				GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
				gbc_lblNewLabel_2.anchor = GridBagConstraints.NORTH;
				gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_2.gridx = 0;
				gbc_lblNewLabel_2.gridy = 4;
				panel.add(lblNewLabel_2, gbc_lblNewLabel_2);
			}
			{
				colorChoosersv = new JColorChooser(); 
				GridBagConstraints gbc_colorChooserpl = new GridBagConstraints();
				gbc_colorChooserpl.fill = GridBagConstraints.HORIZONTAL;
				gbc_colorChooserpl.insets = new Insets(0, 0, 5, 0);
				gbc_colorChooserpl.gridx = 1;
				gbc_colorChooserpl.gridy = 4;
				colorChoosersv.setSize(colorChoosersv.getPreferredSize());
				
		
				panel.add(colorChoosersv, gbc_colorChooserpl);
			}
			{
				serverImageButton = new JButton("Browse");

				 serverImageLabel = new testIconComponent(null , null , null, true);
				 serverImageButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						
					}
					 
					 
					 
				 });
				{
					GridBagConstraints gbc_serverImageButton = new GridBagConstraints();
					gbc_serverImageButton.insets = new Insets(0, 0, 5, 5);
					gbc_serverImageButton.gridx = 0;
					gbc_serverImageButton.gridy = 5;
					panel.add(serverImageButton, gbc_serverImageButton);
				}
				GridBagConstraints gbc_serverImageLabel = new GridBagConstraints();
				gbc_serverImageLabel.anchor = GridBagConstraints.WEST;
				gbc_serverImageLabel.insets = new Insets(0, 0, 5, 0);
				gbc_serverImageLabel.gridx = 1;
				gbc_serverImageLabel.gridy = 5;
				panel.add(serverImageLabel, gbc_serverImageLabel);
			}
			{
				autoStartCheck = new JCheckBox("Start");
				autoStartCheck.setSelected(true);
				GridBagConstraints gbc_autoStartCheck = new GridBagConstraints();
				gbc_autoStartCheck.anchor = GridBagConstraints.NORTH;
				gbc_autoStartCheck.insets = new Insets(0, 0, 0, 5);
				gbc_autoStartCheck.gridx = 0;
				gbc_autoStartCheck.gridy = 6;
				panel.add(autoStartCheck, gbc_autoStartCheck);
			}
			panel.setSize(panel.getPreferredSize());
		}
	
		{
			JPanel panel = new JPanel();
			splitPane.setRightComponent(panel);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[] { 77, 410, 0 };
			gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 57, 0, 0 };
			gbl_panel.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
			gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
			panel.setLayout(gbl_panel);
			
			{
				JLabel lblNewLabel_5 = new JLabel("Client name");
				GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
				gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
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
				gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_6.gridx = 0;
				gbc_lblNewLabel_6.gridy = 1;
				panel.add(lblNewLabel_6, gbc_lblNewLabel_6);
			}
			{
				ipField_C = new JTextField();
				ipField_C.setText("192.168.1.15");
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
				JLabel lblNewLabel_9 = new JLabel("Client IP");
				GridBagConstraints gbc_lblNewLabel_9 = new GridBagConstraints();
				gbc_lblNewLabel_9.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel_9.gridx = 0;
				gbc_lblNewLabel_9.gridy = 3;
				panel.add(lblNewLabel_9, gbc_lblNewLabel_9);
			}
			{
				clientIPField_C = new JTextField();
				clientIPField_C.setText("192.168.1.15");
				GridBagConstraints gbc_clientIPField_C = new GridBagConstraints();
				gbc_clientIPField_C.insets = new Insets(0, 0, 5, 0);
				gbc_clientIPField_C.fill = GridBagConstraints.HORIZONTAL;
				gbc_clientIPField_C.gridx = 1;
				gbc_clientIPField_C.gridy = 3;
				panel.add(clientIPField_C, gbc_clientIPField_C);
				clientIPField_C.setColumns(10);
			}
			{
				Label label = new Label("Color");
				GridBagConstraints gbc_label = new GridBagConstraints();
				gbc_label.anchor = GridBagConstraints.NORTH;
				gbc_label.insets = new Insets(0, 0, 5, 5);
				gbc_label.gridx = 0;
				gbc_label.gridy = 4;
				panel.add(label, gbc_label);
			}
			{
				colorChoosercl = new JColorChooser(); 
				GridBagConstraints gbc_colorChoosercl = new GridBagConstraints();
				gbc_colorChoosercl.fill = GridBagConstraints.HORIZONTAL;
				gbc_colorChoosercl.insets = new Insets(0, 0, 5, 0);
				gbc_colorChoosercl.gridx = 1;
				gbc_colorChoosercl.gridy = 4;
				colorChoosersv.setSize(colorChoosercl.getPreferredSize());

				panel.add(colorChoosercl, gbc_colorChoosercl);
			}
			{
				clientImageButton = new JButton("Browse");

				imageLabelClient = new testIconComponent(null , null , null ,true);
				 clientImageButton.addActionListener(new ActionListener() {

						@Override
						public void actionPerformed(ActionEvent arg0) {

							int result = imageChooser.showOpenDialog(DeveloperMainFrameWrapper.getInstance());
							if (result == JFileChooser.APPROVE_OPTION) {
								File selectedFile = imageChooser.getSelectedFile();
								panel.remove(imageLabelClient);

								panel.remove(imageLabelClient);
								imageLabelClient = new testIconComponent(selectedFile.getPath() ,null, null , true);
								panel.add(imageLabelClient, gbc_imageLabelClient);
								splitPane.updateUI();

								
							}
						}
						 
						 
						 
					 });
				{
					GridBagConstraints gbc_clientImageButton = new GridBagConstraints();
					gbc_clientImageButton.insets = new Insets(0, 0, 5, 5);
					gbc_clientImageButton.gridx = 0;
					gbc_clientImageButton.gridy = 5;
					panel.add(clientImageButton, gbc_clientImageButton);
				}
				gbc_imageLabelClient = new GridBagConstraints();
				gbc_imageLabelClient.anchor = GridBagConstraints.WEST;
				gbc_imageLabelClient.insets = new Insets(0, 0, 5, 0);
				gbc_imageLabelClient.gridx = 1;
				gbc_imageLabelClient.gridy = 5;
				panel.add(imageLabelClient, gbc_imageLabelClient);
			}
			{
				autoConnectCheck = new JCheckBox("Connect");
				autoConnectCheck.setSelected(true);
				GridBagConstraints gbc_auu = new GridBagConstraints();
				gbc_auu.anchor = GridBagConstraints.NORTH;
				gbc_auu.insets = new Insets(0, 0, 0, 5);
				gbc_auu.gridx = 0;
				gbc_auu.gridy = 6;
				panel.add(autoConnectCheck, gbc_auu);
			}
			panel.setSize(panel.getPreferredSize());
		}
		  
		    for (AbstractColorChooserPanel accp : colorChoosersv.getChooserPanels()) {
                if (!accp.getDisplayName().equals("RGB")) {
                	 colorChoosersv.removeChooserPanel(accp);
                }
            }
		    for (AbstractColorChooserPanel accp : colorChoosercl.getChooserPanels()) {
                if (!accp.getDisplayName().equals("RGB")) {
                	 colorChoosercl.removeChooserPanel(accp);
                }
            }
		setSize(getPreferredSize());
		setResizable(true);
		splitPane.setDividerLocation(657);
		
		setServerCheck.doClick();
		setVisible(true);
	}
}
