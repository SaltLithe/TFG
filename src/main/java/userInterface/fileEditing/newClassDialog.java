package userInterface.fileEditing;

import javax.swing.JDialog;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import javax.swing.JTextField;

import core.DEBUG;
import core.DeveloperComponent;
import userInterface.UIController;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;




public class newClassDialog extends JDialog {
	
	private UIController uiController;
	private DeveloperComponent developerComponent;
	
	private JTextField nameField;
	
	private String path;
	private String project;
	
	public newClassDialog(String path , String project) {
		
		this.path = path;
		this.project = project; 
		
		uiController = UIController.getInstance();
		developerComponent = uiController.getDeveloperComponent();
		
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{31, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
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
				
				if(name != null) {
				uiController.run(()->developerComponent.createNewClassFile(name, path,project));
				dispose(); 
				}
			}

		});
		
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
		setSize(new Dimension(400,200));
		setVisible(true);
	}

}
