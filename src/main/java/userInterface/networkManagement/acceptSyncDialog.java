package userInterface.networkManagement;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import network.ClientHandler;
import network.RequestWorkspaceMessage;
import userInterface.DeveloperMainFrameWrapper;
import userInterface.UIController;
import java.awt.Dialog.ModalityType;
import java.awt.Dialog.ModalExclusionType;

@SuppressWarnings("serial")
/**
 * UI class that pops a dialog for users acting as clients to select a location
 * where a synced workspace will be placed
 * 
 * @author Carmen Gómez Moreno
 *
 */
public class acceptSyncDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField pathField;

	
	/**
	 * 
	 * @param parent : The class that creates this dialog
	 * @param name : The name of this user in the session
	 */
	public acceptSyncDialog(ClientHandler parent, String name) {
		setModalityType(ModalityType.APPLICATION_MODAL);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[] { 0, 0, 0 };
		gbl_contentPanel.rowHeights = new int[] { 0, 58, 0, 0 };
		gbl_contentPanel.columnWeights = new double[] { 1.0, 0.0, Double.MIN_VALUE };
		gbl_contentPanel.rowWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblNewLabel = new JLabel(
					"<html><p style=\\\"width:250px\\>You will need to synchronize your workspace with the server,choose a location to copy your workspace to.You can select a previous location if you have synchronized the server's workspace before.Exiting out of this dialog without making a valid selection will disconnect you from the current session.</p></html>");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.anchor = GridBagConstraints.NORTHWEST;
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 1;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			pathField = new JTextField();
			GridBagConstraints gbc_pathField = new GridBagConstraints();
			gbc_pathField.insets = new Insets(0, 0, 0, 5);
			gbc_pathField.fill = GridBagConstraints.HORIZONTAL;
			gbc_pathField.gridx = 0;
			gbc_pathField.gridy = 2;
			contentPanel.add(pathField, gbc_pathField);
			pathField.setColumns(10);
		}
		{
			JButton browseButton = new JButton("Browse");
			GridBagConstraints gbc_browseButton = new GridBagConstraints();
			gbc_browseButton.gridx = 1;
			gbc_browseButton.gridy = 2;
			contentPanel.add(browseButton, gbc_browseButton);
			browseButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {

					JFileChooser chooser = new JFileChooser();
					chooser.setCurrentDirectory(new java.io.File("."));
					chooser.setDialogTitle("Choose a location to copy the server's workspace to your device");
					chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

					chooser.setAcceptAllFileFilterUsed(false);

					if (chooser
							.showOpenDialog(DeveloperMainFrameWrapper.getInstance()) == JFileChooser.APPROVE_OPTION) {

						File directory = chooser.getSelectedFile();
						if (directory.exists()) {

							pathField.setText(directory.getAbsolutePath());
							UIController.getInstance().getDeveloperComponent().expectedWorkSpaceLocation = directory
									.getAbsolutePath();

						}

					}

				}

			});

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
					public void actionPerformed(ActionEvent arg0) {

						if (pathField.getText() != "" && pathField.getText() != null) {

							RequestWorkspaceMessage message = new RequestWorkspaceMessage(name);
							UIController.developerComponent.sendMessageToServer(message);

							dispose();

						}

					}

				});
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {

						UIController.getInstance().getDeveloperComponent().disconnect();
						dispose();

					}

				});
			}
		}

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {

				UIController.getInstance().getDeveloperComponent().disconnect();

				dispose();

			}
		});
		pathField.setEnabled(false);
		this.setVisible(true);
	}

}
