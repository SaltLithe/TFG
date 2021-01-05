package userInterface.networkManagement;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Dialog.ModalExclusionType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import network.ServerHandler;
import userInterface.UIController;

public class serverAwaitSync extends JDialog {
	
	JLabel syncCountLabel ;
	int nClients;
	int clientsConnected;

	public void closeSession() {
		dispose();
	}
	
	public void  updateCount(int adding) {
		System.out.println("UPDATING SYNC ");
		if(!((clientsConnected + adding) < 0) ) {
		clientsConnected += adding;
		syncCountLabel.setText("Synced with " + clientsConnected + "/" + nClients  + " clients.");
		if(nClients == clientsConnected) {
			dispose();
		}
		}
		
	}
	
	public serverAwaitSync(int nClients , ServerHandler parent) {
		
		this.nClients = nClients;
		clientsConnected = 0;
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{440, 0};
		gridBagLayout.rowHeights = new int[]{0, 44, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		syncCountLabel = new JLabel("");
		updateCount(0);
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 0);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		getContentPane().add(syncCountLabel, gbc_lblNewLabel);
		
		JButton closeSessionButton = new JButton("Close Session");
		GridBagConstraints gbc_closeSessionButton = new GridBagConstraints();
		gbc_closeSessionButton.insets = new Insets(0, 0, 5, 0);
		gbc_closeSessionButton.gridx = 0;
		gbc_closeSessionButton.gridy = 2;
		getContentPane().add(closeSessionButton, gbc_closeSessionButton);
		
		JButton cancelButton = new JButton("Cancel Sync");
		GridBagConstraints gbc_cancelButton = new GridBagConstraints();
		gbc_cancelButton.insets = new Insets(0, 0, 5, 0);
		gbc_cancelButton.gridx = 0;
		gbc_cancelButton.gridy = 3;
		getContentPane().add(cancelButton, gbc_cancelButton);
		
				
				cancelButton.addActionListener(new ActionListener() {
		
					@Override
					public void actionPerformed(ActionEvent arg0) {
		
						UIController.getInstance().getDeveloperComponent().disconnect();
						dispose(); 
						
						
					}
					
					
					
				});
				
				closeSessionButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {

						parent.closeServer();
						dispose(); 
						
						
					}
					
					
					
				});
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		

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


