package userInterface.textEditing;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import core.DEBUG;
import core.DeveloperComponent;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;
import userInterface.UIController;

public class TabMiniPanel extends JPanel{
	
	
	private String path; 
	private String name; 
	private UIController uiController;
	private DeveloperComponent developerComponent;
	private PropertyChangeMessenger support; 
	private JLabel nameLabel;
	private TextEditorTab parent; 
	private String project; 
	
	
	public void setParent(TextEditorTab parent ) {
		this.parent = parent; 
	}
	
	public TabMiniPanel(String name ,String path , String project ) {
		
		support = PropertyChangeMessenger.getInstance();
		this.project = project; 
		DEBUG.debugmessage("project in constructor is " + project);
		this.name = name; 
		this.path = path; 
		this.uiController = UIController.getInstance();
		this.developerComponent = uiController.getDeveloperComponent(); 
		
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);		
			
		nameLabel = new JLabel(name);
		add(nameLabel);
		
		JButton closeButton = new JButton("X");
		closeButton.setOpaque(false);
			
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				uiController.run(() -> developerComponent.closeTab(path, parent.unsavedChanges, parent.getContents()));

			}

		});
		
		add(closeButton);
		setOpaque(false);
		setSize(getPreferredSize());
		setVisible(true);
	}
	
	public void setAsSaved() {
		nameLabel.setText(name);
		updateUI(); 
	}

	public void setAsUnsaved() {

	 nameLabel.setText(name + "*");
	 updateUI();
	}

}
