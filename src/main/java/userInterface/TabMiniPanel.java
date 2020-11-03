package userInterface;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import javax.swing.JButton;
import javax.swing.SwingConstants;

import core.DEBUG;
import core.DeveloperComponent;

public class TabMiniPanel extends JPanel{
	
	
	private String path; 
	private String name; 
	private UIController uiController;
	private DeveloperComponent developerComponent;
	private JLabel nameLabel;
	private TextEditorTab parent; 
	
	
	public void setTab(TextEditorTab parent) {
		this.parent = parent; 
	}
	
	public TabMiniPanel(String name ,String path  ) {
		
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
		parent.unsavedChanges = true; 
		nameLabel.setText(name);
		updateUI();
	}

	public void setAsUnsaved() {

	 nameLabel.setText(name + "*");
	 updateUI();
	}

}
