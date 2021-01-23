package userInterface.fileEditing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import userInterface.UIController;

/**
 * Class used to pop a dialog for the user to add a new class
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings({ "unused", "serial" })
public class newClassDialog extends JDialog {

	private JTextField nameField;

	private String path;
	private String project;
	private JCheckBox mainCheckBox;

	public newClassDialog(String path, String project) {

		this.path = path;
		this.project = project;



		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 0, 0, 0, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 31, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		JLabel lblNewLabel = new JLabel("New Class");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.SOUTHWEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 0;
		getContentPane().add(lblNewLabel, gbc_lblNewLabel);

		nameField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 1;
		getContentPane().add(nameField, gbc_textField);
		nameField.setColumns(10);

		JButton okButton = new JButton("OK");
		GridBagConstraints gbc_cancelButton = new GridBagConstraints();
		gbc_cancelButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_cancelButton.insets = new Insets(0, 0, 5, 5);
		gbc_cancelButton.gridx = 2;
		gbc_cancelButton.gridy = 1;
		getContentPane().add(okButton, gbc_cancelButton);

		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String name = nameField.getText();

				if (name != null) {

					UIController.developerComponent.createNewClassFile(name, path, project, mainCheckBox.isSelected());
					dispose();
				}
			}

		});

		mainCheckBox = new JCheckBox("Main");
		GridBagConstraints gbc_mainCheckBox = new GridBagConstraints();
		gbc_mainCheckBox.anchor = GridBagConstraints.WEST;
		gbc_mainCheckBox.insets = new Insets(0, 0, 5, 5);
		gbc_mainCheckBox.gridx = 1;
		gbc_mainCheckBox.gridy = 2;
		getContentPane().add(mainCheckBox, gbc_mainCheckBox);

		JButton cancelButton = new JButton("Cancel");
		GridBagConstraints gbc_cancelButton1 = new GridBagConstraints();
		gbc_cancelButton1.insets = new Insets(0, 0, 5, 5);
		gbc_cancelButton1.gridx = 2;
		gbc_cancelButton1.gridy = 2;
		getContentPane().add(cancelButton, gbc_cancelButton1);

		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				dispose();

			}

		});

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(400, 200));
		setVisible(true);
	}

}
