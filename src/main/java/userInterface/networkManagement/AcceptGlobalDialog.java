package userInterface.networkManagement;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import network.ClientHandler;
import userInterface.UIController;
import java.awt.Dialog.ModalityType;

@SuppressWarnings("serial")
public class AcceptGlobalDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private ClientHandler parent;
	
	public AcceptGlobalDialog(String invokerName, ClientHandler parent) {
		this.parent = parent; 
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblNewLabel = new JLabel(invokerName + " is requesting a global run. Your work will be saved before running.");
			contentPanel.add(lblNewLabel);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Accept");
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						parent.acceptRun();
						dispose();
						
						

					}

				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Decline");
				cancelButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						parent.declineRun(); 
						dispose(); 
						
					}

				});
				cancelButton.setActionCommand("Cancel");
				
				buttonPane.add(cancelButton);
			}
		}
		setModal(false);
		setVisible(true);
	}

}
