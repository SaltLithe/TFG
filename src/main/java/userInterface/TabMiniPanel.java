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
	private UIController uiController;
	private DeveloperComponent developerComponent;
	
	public TabMiniPanel(String name ,String path ) {
		
		this.path = path; 
		this.uiController = UIController.getInstance();
		this.developerComponent = uiController.getDeveloperComponent(); 
		
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		
		
		JLabel lblNewLabel = new JLabel(name);
		add(lblNewLabel);
		
		JButton closeButton = new JButton("X");
		closeButton.setOpaque(false);
		
		
		closeButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				uiController.run(() -> developerComponent.closeTab(path));

			}

		});
		
		add(closeButton);
		setOpaque(false);
		setSize(getPreferredSize());
		setVisible(true);
	}

}
