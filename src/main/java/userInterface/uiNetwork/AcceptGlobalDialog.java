package userInterface.uiNetwork;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import network.ClientHandler;
import network.ServerHandler;
import networkMessages.GlobalRunRequestMessage;
import userInterface.uiGeneral.DeveloperMainFrameWrapper;

@SuppressWarnings("serial")
public class AcceptGlobalDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	@SuppressWarnings("unused")
	private ClientHandler clientParent;
	private ServerHandler serverParent;
	private String invokerName; 
	private int invokerID;
	private GlobalRunRequestMessage request;
	
	/**
	 * @wbp.parser.constructor
	 */
	public AcceptGlobalDialog(String invokerName , ServerHandler parent, int invokerID, GlobalRunRequestMessage request) {
		
		this.serverParent = parent;
		this.invokerName = invokerName;
		this.invokerID = invokerID; 
		this.request = request; 
		construct(); 
		
		
	}
	
	public AcceptGlobalDialog(String invokerName, ClientHandler parent) {
		this.clientParent = parent;
		this.invokerName = invokerName;
		construct(); 
	
	}
	
	public void construct() {
		this.setTitle("Global run request");
		this.setIconImage(DeveloperMainFrameWrapper.windowIcon);

		setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		{
			JLabel lblNewLabel = new JLabel(invokerName + " is requesting a global run.");
			lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
			JLabel savelbl = new JLabel("Your work will be saved before running.");
			contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
			contentPanel.add(lblNewLabel);
			contentPanel.add(savelbl);
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
						if(clientParent != null) {
						clientParent.decideRun(true);
					
						}else {
							serverParent.decideRun(true, invokerID,  request);
						}
						
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
						if(clientParent != null) {
						clientParent.decideRun(false);
						}
						else {
							serverParent.decideRun(false , invokerID ,request);
						}
						dispose(); 
						
					}

				});
				cancelButton.setActionCommand("Cancel");
				
				buttonPane.add(cancelButton);
			}
		}
		int width = 300;
		int height = 150;
		setMinimumSize(new Dimension(width,height));
		setMaximumSize(new Dimension(width,height));
		setPreferredSize(new Dimension(width,height));
		setSize(width,height);
		setAlwaysOnTop(true);
		setVisible(true);
		
	}

}
