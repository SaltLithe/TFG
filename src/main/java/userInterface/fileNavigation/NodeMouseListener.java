package userInterface.fileNavigation;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTree;

import core.DEBUG;
import core.DeveloperComponent;
import userInterface.UIController;

public class NodeMouseListener implements MouseListener {

	private UIController uiController;
	private DeveloperComponent developerComponent; 
	private JTree tree; 
	
	public NodeMouseListener() {
		
		this.tree = tree; 
		uiController = UIController.getInstance();
		developerComponent = uiController.getDeveloperComponent();
		
	}
	
	
	@Override
	public void mouseClicked(MouseEvent evt) {
		
		
    
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
