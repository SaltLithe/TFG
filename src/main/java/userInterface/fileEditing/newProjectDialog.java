package userInterface.fileEditing;

import javax.swing.JDialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import core.DEBUG;
import core.DeveloperComponent;
import userInterface.UIController;

import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

public class newProjectDialog extends JDialog{
	private JTextField nameField;
	public newProjectDialog(UIController uiController , DeveloperComponent developerComponent) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 265, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblNewProject = new JLabel("New project");
		GridBagConstraints gbc_lblNewProject = new GridBagConstraints();
		gbc_lblNewProject.anchor = GridBagConstraints.WEST;
		gbc_lblNewProject.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewProject.gridx = 1;
		gbc_lblNewProject.gridy = 2;
		getContentPane().add(lblNewProject, gbc_lblNewProject);
		
		nameField = new JTextField();
		GridBagConstraints gbc_nameField = new GridBagConstraints();
		gbc_nameField.insets = new Insets(0, 0, 5, 5);
		gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nameField.gridx = 1;
		gbc_nameField.gridy = 3;
		getContentPane().add(nameField, gbc_nameField);
		nameField.setColumns(10);
		
		JButton okButton = new JButton("OK");
		GridBagConstraints gbc_okButton = new GridBagConstraints();
		gbc_okButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_okButton.insets = new Insets(0, 0, 5, 5);
		gbc_okButton.gridx = 2;
		gbc_okButton.gridy = 3;
		getContentPane().add(okButton, gbc_okButton);
		
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
				String name = nameField.getText() ;
				if(name != "" && name != null ) {
				uiController.run(()-> developerComponent.createNewProject(name));
				}

			}

		});
		
		JButton cancelButton = new JButton("Cancel");
		GridBagConstraints gbc_cancelButton = new GridBagConstraints();
		gbc_cancelButton.insets = new Insets(0, 0, 5, 5);
		gbc_cancelButton.gridx = 2;
		gbc_cancelButton.gridy = 4;
		getContentPane().add(cancelButton, gbc_cancelButton);
		
		setSize(new Dimension(500,200));
		this.setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setVisible(true);
	}

}
