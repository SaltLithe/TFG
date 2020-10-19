package userInterface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import core.DEBUG;
import fileManagement.WorkSpaceManager;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class warningDialog extends JDialog {

	/**
	 * Launch the application.
	 */
	
	private JButton okButton; 
	

	/**
	 * Create the dialog.
	 */
	public warningDialog(String message, String icon) {
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{213, 218, 0};
		gridBagLayout.rowHeights = new int[]{228, 33, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		{
			ImageIcon createIcon = new ImageIcon(icon);
			JLabel iconLabel = new JLabel(createIcon);
			GridBagConstraints gbc_iconLabel = new GridBagConstraints();
			gbc_iconLabel.anchor = GridBagConstraints.EAST;
			gbc_iconLabel.insets = new Insets(0, 0, 5, 5);
			gbc_iconLabel.gridx = 0;
			gbc_iconLabel.gridy = 0;
			getContentPane().add(iconLabel, gbc_iconLabel);
		}
		{
			JLabel messageLabel = new JLabel(message);
			messageLabel.setHorizontalAlignment(SwingConstants.LEFT);
			GridBagConstraints gbc_messageLabel = new GridBagConstraints();
			gbc_messageLabel.anchor = GridBagConstraints.WEST;
			gbc_messageLabel.insets = new Insets(0, 0, 5, 0);
			gbc_messageLabel.gridx = 1;
			gbc_messageLabel.gridy = 0;
			getContentPane().add(messageLabel, gbc_messageLabel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			GridBagConstraints gbc_buttonPane = new GridBagConstraints();
			gbc_buttonPane.anchor = GridBagConstraints.NORTH;
			gbc_buttonPane.fill = GridBagConstraints.HORIZONTAL;
			gbc_buttonPane.gridx = 1;
			gbc_buttonPane.gridy = 1;
			getContentPane().add(buttonPane, gbc_buttonPane);
			{
			 okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
		okButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
 
				dispose(); 
			
				

			}

		});
		
		
		setSize(200,100);
		setVisible(true);
	}

}
