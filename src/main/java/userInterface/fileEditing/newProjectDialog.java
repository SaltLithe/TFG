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
import javax.swing.JTextField;

import userInterface.UIController;
/**
 * Class used to create a new project taking input from the user for the project name 
 * @author Carmen Gómez Moreno
 *
 */
@SuppressWarnings("serial")
public class newProjectDialog extends JDialog{
	private JTextField nameField;
	public newProjectDialog() {
		setTitle("Create a new Project");
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 265, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblNewProject = new JLabel("New project");
		GridBagConstraints gbc_lblNewProject = new GridBagConstraints();
		gbc_lblNewProject.anchor = GridBagConstraints.WEST;
		gbc_lblNewProject.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewProject.gridx = 2;
		gbc_lblNewProject.gridy = 3;
		getContentPane().add(lblNewProject, gbc_lblNewProject);
		
		nameField = new JTextField();
		GridBagConstraints gbc_nameField = new GridBagConstraints();
		gbc_nameField.insets = new Insets(0, 0, 5, 5);
		gbc_nameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_nameField.gridx = 2;
		gbc_nameField.gridy = 4;
		getContentPane().add(nameField, gbc_nameField);
		nameField.setColumns(10);
		
		JButton okButton = new JButton("OK");
		GridBagConstraints gbc_okButton = new GridBagConstraints();
		gbc_okButton.fill = GridBagConstraints.HORIZONTAL;
		gbc_okButton.insets = new Insets(0, 0, 5, 5);
		gbc_okButton.gridx = 3;
		gbc_okButton.gridy = 4;
		getContentPane().add(okButton, gbc_okButton);
		
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
				String name = nameField.getText() ;
				if(name != "" && name != null ) {
				UIController.developerComponent.createNewProject(name,true,true);
				dispose(); 
				}

			}

		});
		
		JButton cancelButton = new JButton("Cancel");
		GridBagConstraints gbc_cancelButton = new GridBagConstraints();
		gbc_cancelButton.insets = new Insets(0, 0, 5, 5);
		gbc_cancelButton.gridx = 3;
		gbc_cancelButton.gridy = 5;
		getContentPane().add(cancelButton, gbc_cancelButton);
		
		setSize(new Dimension(375,175));
		this.setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		setVisible(true);
	}

}
