package userInterface.uiFileEditing;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import commandController.CommandController;
import userInterface.uiGeneral.DeveloperMainFrameWrapper;

/**
 * Class used to pop a dialog for the user to add a new class
 * 
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
		setTitle("Create a new class");
		this.setIconImage(DeveloperMainFrameWrapper.windowIcon);

		this.path = path;
		this.project = project;

		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] { 18, 281, 0, 0 };
		gridBagLayout.rowHeights = new int[] { 27, 38, 0, 0, 0 };
		gridBagLayout.columnWeights = new double[] { 0.0, 0.0, 0.0, Double.MIN_VALUE };
		gridBagLayout.rowWeights = new double[] { 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE };
		getContentPane().setLayout(gridBagLayout);
		
				JLabel lblNewLabel = new JLabel("New Class");
				GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
				gbc_lblNewLabel.anchor = GridBagConstraints.SOUTHWEST;
				gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
				gbc_lblNewLabel.gridx = 1;
				gbc_lblNewLabel.gridy = 0;
				getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.gridx = 1;
		gbc_panel_1.gridy = 1;
		getContentPane().add(panel_1, gbc_panel_1);
				panel_1.setLayout(new BoxLayout(panel_1, BoxLayout.X_AXIS));
		
				nameField = new JTextField();
				panel_1.add(nameField);
				nameField.setColumns(30);
		
		JPanel panel = new JPanel();
		GridBagConstraints gbc_panel = new GridBagConstraints();
		gbc_panel.insets = new Insets(0, 0, 5, 5);
		gbc_panel.fill = GridBagConstraints.BOTH;
		gbc_panel.gridx = 1;
		gbc_panel.gridy = 2;
		getContentPane().add(panel, gbc_panel);
				panel.setLayout(new GridLayout(0, 3, 0, 0));
		
				mainCheckBox = new JCheckBox("Main");
				panel.add(mainCheckBox);
												
														JButton okButton = new JButton("OK");
														panel.add(okButton);
														
																okButton.addActionListener(new ActionListener() {
														
																	@Override
																	public void actionPerformed(ActionEvent e) {
														
																		String name = nameField.getText();
														
																		if (name != null && !name.equals("")) {
														
																			CommandController.developerComponent.createNewClassFile(name, path, project, mainCheckBox.isSelected());
																			dispose();
																		}
																	}
														
																});
										
												JButton cancelButton = new JButton("Cancel");
												panel.add(cancelButton);
										
												cancelButton.addActionListener(new ActionListener() {
										
													@Override
													public void actionPerformed(ActionEvent e) {
										
														dispose();
										
													}
										
												});

		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(new Dimension(330, 150));
		this.setResizable(false);
		setVisible(true);
	}

}
