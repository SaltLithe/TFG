package userInterface.networkManagement;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import userInterface.UIController;
import java.awt.Dialog.ModalityType;
import java.awt.Dialog.ModalExclusionType;

public class awaitSyncDialog extends JDialog {
	public awaitSyncDialog() {
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{440, 0};
		gridBagLayout.rowHeights = new int[]{0, 44, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JLabel lblNewLabel = new JLabel("Syncing");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		JButton cancelButton = new JButton("Cancel Sync");
		GridBagConstraints gbc_cancelButton = new GridBagConstraints();
		gbc_cancelButton.gridx = 0;
		gbc_cancelButton.gridy = 2;
		getContentPane().add(cancelButton, gbc_cancelButton);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		
		cancelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				UIController.getInstance().getDeveloperComponent().disconnect();
				dispose(); 
				
				
			}
			
			
			
		});
		

		addWindowListener(new WindowAdapter() {
		    public void windowClosing(WindowEvent e) {
		    	
				UIController.getInstance().getDeveloperComponent().disconnect();
		    	 dispose();
		    	
		    }
		});
		
		
		setSize(new Dimension(getPreferredSize().width, 200));
		setVisible(true);
	}

}
