package userInterface;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class AddWorkSpaceDialog extends JDialog {
	private JTextField nameField;
	private JTextField pathField;

	public AddWorkSpaceDialog() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 161, 0, 84, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 41, 43, 123, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel(" New WorkSpace");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 0;
		getContentPane().add(lblNewLabel, gbc_lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel(" Name");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 2;
		getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);

		nameField = new JTextField();
		GridBagConstraints gbc_nameField = new GridBagConstraints();
		gbc_nameField.insets = new Insets(0, 0, 5, 5);
		gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nameField.gridx = 1;
		gbc_nameField.gridy = 2;
		getContentPane().add(nameField, gbc_nameField);
		nameField.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel(" Path");
		GridBagConstraints gbc_lblNewLabel_2 = new GridBagConstraints();
		gbc_lblNewLabel_2.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_2.gridx = 0;
		gbc_lblNewLabel_2.gridy = 3;
		getContentPane().add(lblNewLabel_2, gbc_lblNewLabel_2);

		pathField = new JTextField();
		GridBagConstraints gbc_pathField = new GridBagConstraints();
		gbc_pathField.insets = new Insets(0, 0, 5, 5);
		gbc_pathField.fill = GridBagConstraints.HORIZONTAL;
		gbc_pathField.gridx = 1;
		gbc_pathField.gridy = 3;
		getContentPane().add(pathField, gbc_pathField);
		pathField.setColumns(10);

		JButton BrowseButton = new JButton("Browse");
		GridBagConstraints gbc_BrowseButton = new GridBagConstraints();
		gbc_BrowseButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_BrowseButton.insets = new Insets(0, 0, 5, 5);
		gbc_BrowseButton.gridx = 2;
		gbc_BrowseButton.gridy = 3;
		getContentPane().add(BrowseButton, gbc_BrowseButton);

		JButton okButton = new JButton("OK");
		okButton.setVerticalAlignment(SwingConstants.BOTTOM);
		GridBagConstraints gbc_okButton = new GridBagConstraints();
		gbc_okButton.anchor = GridBagConstraints.SOUTHEAST;
		gbc_okButton.insets = new Insets(0, 0, 0, 5);
		gbc_okButton.gridx = 1;
		gbc_okButton.gridy = 5;
		okButton.setPreferredSize(BrowseButton.getPreferredSize());
		getContentPane().add(okButton, gbc_okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.setVerticalAlignment(SwingConstants.BOTTOM);
		GridBagConstraints gbc_cancelButton = new GridBagConstraints();
		gbc_cancelButton.insets = new Insets(0, 0, 0, 5);
		gbc_cancelButton.anchor = GridBagConstraints.SOUTH;
		gbc_cancelButton.gridx = 2;
		gbc_cancelButton.gridy = 5;
		cancelButton.setPreferredSize(BrowseButton.getPreferredSize());
		getContentPane().add(cancelButton, gbc_cancelButton);

		setSize(500, 300);
		setResizable(false);
		setVisible(true);
	}

}
