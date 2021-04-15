package userInterface.uiNetwork;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.colorchooser.AbstractColorChooserPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

import commandController.CommandController;
import userInterface.uiGeneral.DeveloperMainFrameWrapper;

@SuppressWarnings("serial")
public class JoinSessionDialog extends JFrame {
	/**
	 * @wbp.nonvisual location=150,329
	 */
	private final JPanel panel;
	private JTextField clientNameField_C;
	private JTextField ipField_C;
	private JTextField clientIPField_C;
	private JTextField serverPortField_C;

	private JColorChooser colorChoosercl;
	private ProfileIIconComponent imageLabelClient;
	private JButton clientImageButton;
	private JFileChooser imageChooser;
	private GridBagConstraints gbc_imageLabelClient;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the dialog.
	 */
	public JoinSessionDialog() {
		this.setIconImage(DeveloperMainFrameWrapper.windowIcon);
		this.setTitle("Join a session");

		imageChooser = new JFileChooser();
		imageChooser.addChoosableFileFilter(new FileNameExtensionFilter("Image Files", "png"));
		getContentPane().setLayout(new BorderLayout());

		panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);

		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] { 82, 597, 0 };
		gbl_panel.rowHeights = new int[] { 0, 0, 0, 0, 0, 53, 65, 0 };
		gbl_panel.columnWeights = new double[] {};
		gbl_panel.rowWeights = new double[] {};
		panel.setLayout(gbl_panel);

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

				CommandController.developerComponent.setAsClient(ipField_C.getText(), clientIPField_C.getText(),
						Integer.valueOf(serverPortField_C.getText()), clientNameField_C.getText(),
						imageLabelClient.ImageByteData, colorChoosercl.getColor());

				

				dispose();
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

		panel.setSize(panel.getPreferredSize());

		GridBagLayout gbl_panel1 = new GridBagLayout();
		gbl_panel1.columnWidths = new int[] { 77, 410, 0 };
		gbl_panel1.rowHeights = new int[] { 0, 0, 0, 0, 0, 57, 0, 0 };
		gbl_panel1.columnWeights = new double[] { 0.0, 1.0, Double.MIN_VALUE };
		gbl_panel1.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel1);

		JLabel lblNewLabel_5 = new JLabel("Client name");
		GridBagConstraints gbc_lblNewLabel_5 = new GridBagConstraints();
		gbc_lblNewLabel_5.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_5.gridx = 0;
		gbc_lblNewLabel_5.gridy = 0;
		panel.add(lblNewLabel_5, gbc_lblNewLabel_5);

		clientNameField_C = new JTextField();
		clientNameField_C.setText("Tu nombre aqui");
		GridBagConstraints gbc_clientNameField_C = new GridBagConstraints();
		gbc_clientNameField_C.insets = new Insets(0, 0, 5, 0);
		gbc_clientNameField_C.fill = GridBagConstraints.HORIZONTAL;
		gbc_clientNameField_C.gridx = 1;
		gbc_clientNameField_C.gridy = 0;
		panel.add(clientNameField_C, gbc_clientNameField_C);
		clientNameField_C.setColumns(10);

		JLabel lblNewLabel_6 = new JLabel("Server IP");
		GridBagConstraints gbc_lblNewLabel_6 = new GridBagConstraints();
		gbc_lblNewLabel_6.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_6.gridx = 0;
		gbc_lblNewLabel_6.gridy = 1;
		panel.add(lblNewLabel_6, gbc_lblNewLabel_6);

		ipField_C = new JTextField();
		ipField_C.setText("25.113.201.139");
		GridBagConstraints gbc_ipField_C = new GridBagConstraints();
		gbc_ipField_C.insets = new Insets(0, 0, 5, 0);
		gbc_ipField_C.fill = GridBagConstraints.HORIZONTAL;
		gbc_ipField_C.gridx = 1;
		gbc_ipField_C.gridy = 1;
		panel.add(ipField_C, gbc_ipField_C);
		ipField_C.setColumns(10);

		JLabel lblNewLabel_7 = new JLabel("Server port");
		GridBagConstraints gbc_lblNewLabel_7 = new GridBagConstraints();
		gbc_lblNewLabel_7.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_7.gridx = 0;
		gbc_lblNewLabel_7.gridy = 2;
		panel.add(lblNewLabel_7, gbc_lblNewLabel_7);

		serverPortField_C = new JTextField();
		serverPortField_C.setText("8085");
		GridBagConstraints gbc_serverPortField_C = new GridBagConstraints();
		gbc_serverPortField_C.insets = new Insets(0, 0, 5, 0);
		gbc_serverPortField_C.fill = GridBagConstraints.HORIZONTAL;
		gbc_serverPortField_C.gridx = 1;
		gbc_serverPortField_C.gridy = 2;
		panel.add(serverPortField_C, gbc_serverPortField_C);
		serverPortField_C.setColumns(10);

		JLabel lblNewLabel_9 = new JLabel("Client IP");
		GridBagConstraints gbc_lblNewLabel_9 = new GridBagConstraints();
		gbc_lblNewLabel_9.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_9.gridx = 0;
		gbc_lblNewLabel_9.gridy = 3;
		panel.add(lblNewLabel_9, gbc_lblNewLabel_9);

		clientIPField_C = new JTextField();
		clientIPField_C.setText("Tu ipv4 de hamachi aqui");
		GridBagConstraints gbc_clientIPField_C = new GridBagConstraints();
		gbc_clientIPField_C.insets = new Insets(0, 0, 5, 0);
		gbc_clientIPField_C.fill = GridBagConstraints.HORIZONTAL;
		gbc_clientIPField_C.gridx = 1;
		gbc_clientIPField_C.gridy = 3;
		panel.add(clientIPField_C, gbc_clientIPField_C);
		clientIPField_C.setColumns(10);

		Label label = new Label("Color");
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.anchor = GridBagConstraints.NORTH;
		gbc_label.insets = new Insets(0, 0, 5, 5);
		gbc_label.gridx = 0;
		gbc_label.gridy = 4;
		panel.add(label, gbc_label);

		colorChoosercl = new JColorChooser();
		GridBagConstraints gbc_colorChoosercl = new GridBagConstraints();
		gbc_colorChoosercl.fill = GridBagConstraints.HORIZONTAL;
		gbc_colorChoosercl.insets = new Insets(0, 0, 5, 0);
		gbc_colorChoosercl.gridx = 1;
		gbc_colorChoosercl.gridy = 4;

		panel.add(colorChoosercl, gbc_colorChoosercl);

		clientImageButton = new JButton("Browse");

		imageLabelClient = new ProfileIIconComponent(null, null, null, true,null);
		clientImageButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				int result = imageChooser.showOpenDialog(DeveloperMainFrameWrapper.getInstance());
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = imageChooser.getSelectedFile();

					BufferedImage bimg = null;
					try {
						bimg = ImageIO.read(selectedFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
					int width = bimg.getWidth();
					int height = bimg.getHeight();

					if (width > 256 || height > 256) {

						JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
								"Image size can't be greater than 256x256 px.", "Error", JOptionPane.ERROR_MESSAGE);

					}

					else {
						panel.remove(imageLabelClient);
						imageLabelClient = new ProfileIIconComponent(selectedFile.getPath(), null, null, true,null);
						panel.add(imageLabelClient, gbc_imageLabelClient);
						panel.updateUI();
					}

				}
			}

		});

		GridBagConstraints gbc_clientImageButton = new GridBagConstraints();
		gbc_clientImageButton.insets = new Insets(0, 0, 5, 5);
		gbc_clientImageButton.gridx = 0;
		gbc_clientImageButton.gridy = 5;
		panel.add(clientImageButton, gbc_clientImageButton);

		gbc_imageLabelClient = new GridBagConstraints();
		gbc_imageLabelClient.anchor = GridBagConstraints.WEST;
		gbc_imageLabelClient.insets = new Insets(0, 0, 5, 0);
		gbc_imageLabelClient.gridx = 1;
		gbc_imageLabelClient.gridy = 5;
		panel.add(imageLabelClient, gbc_imageLabelClient);

		panel.setSize(panel.getPreferredSize());

		for (AbstractColorChooserPanel accp : colorChoosercl.getChooserPanels()) {
			if (!accp.getDisplayName().equals("RGB")) {
				colorChoosercl.removeChooserPanel(accp);
			}
		}
		setSize(new Dimension(getPreferredSize().width , getPreferredSize().height+50 ));
		setResizable(true);

		setVisible(true);
	}
}
