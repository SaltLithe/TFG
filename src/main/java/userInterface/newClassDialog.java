package userInterface;

import javax.swing.JDialog;
import java.awt.FlowLayout;
import java.awt.GridBagLayout;
import javax.swing.JTextField;

import core.DEBUG;
import core.DeveloperComponent;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;




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
		gridBagLayout.columnWidths = new int[]{0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		nameField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 0;
		gbc_textField.gridy = 1;
		getContentPane().add(nameField, gbc_textField);
		nameField.setColumns(10);
		
		JButton okButton = new JButton("New button");
		GridBagConstraints gbc_btnNewButton = new GridBagConstraints();
		gbc_btnNewButton.insets = new Insets(0, 0, 5, 0);
		gbc_btnNewButton.gridx = 1;
		gbc_btnNewButton.gridy = 1;
		getContentPane().add(okButton, gbc_btnNewButton);
		
		
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
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(getPreferredSize());
		setVisible(true);
	}

}
