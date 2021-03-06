package userInterface.uiFileEditing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import commandController.CommandController;
import userInterface.uiGeneral.DeveloperMainFrameWrapper;

/**
 * Class used to create a new project taking input from the user for the project
 * name
 * 
 * @author Carmen G�mez Moreno
 *
 */
@SuppressWarnings("serial")
public class newProjectDialog extends JDialog {
	private JTextField nameField;

	public newProjectDialog() {
		setTitle("Create a new Project");
		this.setIconImage(DeveloperMainFrameWrapper.windowIcon);

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 44, 255, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 1.0, 0.0, 1.0, 1.0, 1.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);

		JLabel lblNewProject = new JLabel("New project");
		GridBagConstraints gbc_lblNewProject = new GridBagConstraints();
		gbc_lblNewProject.anchor = GridBagConstraints.WEST;
		gbc_lblNewProject.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewProject.gridx = 1;
		gbc_lblNewProject.gridy = 1;
		getContentPane().add(lblNewProject, gbc_lblNewProject);

		nameField = new JTextField();
		GridBagConstraints gbc_nameField = new GridBagConstraints();
		gbc_nameField.insets = new Insets(0, 0, 5, 5);
		gbc_nameField.fill = GridBagConstraints.BOTH;
		gbc_nameField.gridx = 1;
		gbc_nameField.gridy = 2;
		getContentPane().add(nameField, gbc_nameField);
		nameField.setColumns(10);

		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.anchor = GridBagConstraints.EAST;
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 5;
		getContentPane().add(panel, gbc_panel);
		panel.setLayout(new GridLayout(0, 2, 0, 0));

		JButton okButton = new JButton("OK");
		panel.add(okButton);

		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String name = nameField.getText();
				if (!name.equals("") && name != null) {
					CommandController.developerComponent.createNewProject(name, true, true);
					dispose();
				}

			}

		});

		JButton cancelButton = new JButton("Cancel");
		panel.add(cancelButton);
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				dispose();
			}

		});

		setSize(new Dimension(350, 160));

		this.setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		setVisible(true);
	}

}
