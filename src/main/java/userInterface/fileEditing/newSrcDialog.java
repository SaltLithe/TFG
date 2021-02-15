package userInterface.fileEditing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import core.DEBUG;
import userInterface.DeveloperMainFrameWrapper;
import userInterface.UIController;
import javax.swing.JPanel;
import java.awt.GridLayout;

/**
 * Class used to pop a dialog for the user to add a source file
 * 
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings({ "unused", "serial" })
public class newSrcDialog extends JDialog {
	private JTextField nameField;
	private JTextField parentPathField;
	private String parentpath;

	public newSrcDialog(String parentpath, String project) {

		setTitle("Create a new source folder");
		this.setIconImage(DeveloperMainFrameWrapper.windowIcon);

		this.parentpath = parentpath;
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 31, 257, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 29, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel("New source folder");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.HORIZONTAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		getContentPane().add(lblNewLabel, gbc_lblNewLabel);

		parentPathField = new JTextField();
		GridBagConstraints gbc_parentPathField = new GridBagConstraints();
		gbc_parentPathField.insets = new Insets(0, 0, 5, 5);
		gbc_parentPathField.fill = GridBagConstraints.HORIZONTAL;
		gbc_parentPathField.gridx = 1;
		gbc_parentPathField.gridy = 1;
		getContentPane().add(parentPathField, gbc_parentPathField);
		parentPathField.setColumns(10);

		nameField = new JTextField();
		GridBagConstraints gbc_nameField = new GridBagConstraints();
		gbc_nameField.insets = new Insets(0, 0, 5, 5);
		gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nameField.gridx = 1;
		gbc_nameField.gridy = 2;
		getContentPane().add(nameField, gbc_nameField);
		nameField.setColumns(10);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.EAST;
		gbc_panel.insets = new Insets(0, 0, 0, 5);
		gbc_panel.fill = GridBagConstraints.VERTICAL;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 3;
		getContentPane().add(panel, gbc_panel);
				panel.setLayout(new GridLayout(0, 2, 0, 0));
		
				JButton okButton = new JButton("Ok");
				panel.add(okButton);
				
						JButton cancelButton = new JButton("Cancel");
						panel.add(cancelButton);
						
								cancelButton.addActionListener(new ActionListener() {
						
									@Override
									public void actionPerformed(ActionEvent e) {
						
										dispose();
									}
								});
				
						okButton.addActionListener(new ActionListener() {
				
							@Override
							public void actionPerformed(ActionEvent e) {
				
								if (nameField.getText() != null && !nameField.getText().equals("")) {
									String possiblename = nameField.getText().replace(" ", "_");
									if (possiblename.equals("bin")) {
										DEBUG.debugmessage("CREATING FILE IN " + parentpath + "\\" + possiblename);
										JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(), "Name can't be bin.",
												"Naming error", JOptionPane.ERROR_MESSAGE);
									} else {
										DEBUG.debugmessage("CREATING FILE IN " + parentpath + "\\" + possiblename);
										UIController.developerComponent.setProjectFocus(project);
										UIController.developerComponent.createSrcFolder(parentpath + "\\" + possiblename, possiblename);
										dispose();
				
									}
								}
				
							}
						});

		parentPathField.setText(parentpath);
		parentPathField.setEnabled(false);

		setSize(new Dimension(350, 160));

		setVisible(true);
	}

}
