package userInterface.uiNetwork;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import commandController.CommandController;
import userInterface.uiGeneral.DeveloperMainFrameWrapper;

@SuppressWarnings("serial")
public class CreateSessionDialog extends JFrame {

	private final JPanel contentPanel = new JPanel();
	/**
	 * @wbp.nonvisual location=150,329
	 */
	private final JPanel panel;
	private JTextField serverNameField_S;
	private JTextField serverPortField_S;
	private JTextField maxClientsField_S;
	private JTextField ipField_S;
	private JColorChooser colorChoosersv;
	private ProfileIIconComponent serverImageLabel;
	private JButton serverImageButton;
	private JFileChooser imageChooser;
	private GridBagConstraints gbc_serverImageLabel;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	public CreateSessionDialog() {
		this.setIconImage(DeveloperMainFrameWrapper.windowIcon);
		this.setTitle("Create a session");

		imageChooser = new JFileChooser();
		imageChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "png"));

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));

		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);

		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 82, 597, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 53, 65, 0 };
		gbl_panel.columnWeights = new double[] { 0.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel lblNewLabel = new JLabel("Server name");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		panel.add(lblNewLabel, gbc_lblNewLabel);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {

				if (Integer.valueOf(maxClientsField_S.getText()) <= 4) {
					CommandController.developerComponent.setIcon(colorChoosersv.getColor(), serverImageLabel.imagepath,
							serverNameField_S.getText());
					CommandController.developerComponent.setAsServer(serverNameField_S.getText(), ipField_S.getText(),
							Integer.valueOf(maxClientsField_S.getText()), Integer.valueOf(serverPortField_S.getText()),
							Integer.valueOf(maxClientsField_S.getText()), serverImageLabel.ImageByteData,
							colorChoosersv.getColor());

					dispose();
				} else {

					JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
							"Can't create a session for more than four clients.", "Too many users",
							JOptionPane.ERROR_MESSAGE);
				}

			}
		});

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				dispose();
			}

		});
		buttonPane.add(cancelButton);

		serverNameField_S = new JTextField();
		serverNameField_S.setHorizontalAlignment(SwingConstants.LEFT);
		serverNameField_S.setText("");
		GridBagConstraints gbc_serverNameField_S = new GridBagConstraints();
		gbc_serverNameField_S.fill = GridBagConstraints.HORIZONTAL;
		gbc_serverNameField_S.insets = new Insets(0, 0, 5, 0);
		gbc_serverNameField_S.gridx = 1;
		gbc_serverNameField_S.gridy = 0;
		panel.add(serverNameField_S, gbc_serverNameField_S);
		serverNameField_S.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Server port");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 1;
		panel.add(lblNewLabel_1, gbc_lblNewLabel_1);

		serverPortField_S = new JTextField();
		serverPortField_S.setText("");
		GridBagConstraints gbc_serverPortField_S = new GridBagConstraints();
		gbc_serverPortField_S.insets = new Insets(0, 0, 5, 0);
		gbc_serverPortField_S.fill = GridBagConstraints.HORIZONTAL;
		gbc_serverPortField_S.gridx = 1;
		gbc_serverPortField_S.gridy = 1;
		panel.add(serverPortField_S, gbc_serverPortField_S);
		serverPortField_S.setColumns(10);

		JLabel lblNewLabel_3 = new JLabel("Max clients");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 2;
		panel.add(lblNewLabel_3, gbc_lblNewLabel_3);

		maxClientsField_S = new JTextField();
		maxClientsField_S.setText("");
		GridBagConstraints gbc_maxClientsField_S = new GridBagConstraints();
		gbc_maxClientsField_S.insets = new Insets(0, 0, 5, 0);
		gbc_maxClientsField_S.fill = GridBagConstraints.HORIZONTAL;
		gbc_maxClientsField_S.gridx = 1;
		gbc_maxClientsField_S.gridy = 2;
		panel.add(maxClientsField_S, gbc_maxClientsField_S);
		maxClientsField_S.setColumns(10);

		JLabel lblNewLabel_4 = new JLabel("IP");
		GridBagConstraints gbc_lblNewLabel_4 = new GridBagConstraints();
		gbc_lblNewLabel_4.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_4.gridx = 0;
		gbc_lblNewLabel_4.gridy = 3;
		panel.add(lblNewLabel_4, gbc_lblNewLabel_4);

		ipField_S = new JTextField();
		ipField_S.setText("");
		GridBagConstraints gbc_ipField_S = new GridBagConstraints();
		gbc_ipField_S.fill = GridBagConstraints.HORIZONTAL;
		gbc_ipField_S.insets = new Insets(0, 0, 5, 0);
		gbc_ipField_S.gridx = 1;
		gbc_ipField_S.gridy = 3;
		panel.add(ipField_S, gbc_ipField_S);
		ipField_S.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Color");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.NORTH;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 4;
		panel.add(lblNewLabel_2, gbc_lblNewLabel_2);

		colorChoosersv = new JColorChooser();
		GridBagConstraints gbc_colorChooserpl = new GridBagConstraints();
		gbc_colorChooserpl.fill = GridBagConstraints.HORIZONTAL;
		gbc_colorChooserpl.insets = new Insets(0, 0, 5, 0);
		gbc_colorChooserpl.gridx = 1;
		gbc_colorChooserpl.gridy = 4;
		colorChoosersv.setSize(colorChoosersv.getPreferredSize());

		panel.add(colorChoosersv, gbc_colorChooserpl);

		serverImageButton = new JButton("Browse");

		serverImageLabel = new ProfileIIconComponent(null, null, null, true, null);
		serverImageButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				int result = imageChooser.showOpenDialog(DeveloperMainFrameWrapper.getInstance());
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = imageChooser.getSelectedFile();

					panel.remove(serverImageLabel);
					serverImageLabel = new ProfileIIconComponent(selectedFile.getPath(), null, null, true, null);
					panel.add(serverImageLabel, gbc_serverImageLabel);
					panel.updateUI();

				}
			}

		});

		GridBagConstraints gbc_serverImageButton = new GridBagConstraints();
		gbc_serverImageButton.insets = new Insets(0, 0, 5, 5);
		gbc_serverImageButton.gridx = 0;
		gbc_serverImageButton.gridy = 5;
		panel.add(serverImageButton, gbc_serverImageButton);

		gbc_serverImageLabel = new GridBagConstraints();
		gbc_serverImageLabel.anchor = GridBagConstraints.WEST;
		gbc_serverImageLabel.insets = new Insets(0, 0, 5, 0);
		gbc_serverImageLabel.gridx = 1;
		gbc_serverImageLabel.gridy = 5;
		panel.add(serverImageLabel, gbc_serverImageLabel);

		panel.setSize(panel.getPreferredSize());

		for (AbstractColorChooserPanel accp : colorChoosersv.getChooserPanels()) {
			if (!accp.getDisplayName().equals("RGB")) {
				colorChoosersv.removeChooserPanel(accp);
			}
		}

		setSize(800, 600);
		setResizable(false);

		setVisible(true);
	}
}
