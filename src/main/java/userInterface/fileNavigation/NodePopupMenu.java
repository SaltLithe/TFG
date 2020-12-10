package userInterface.fileNavigation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import core.DeveloperComponent;
import userInterface.DeveloperMainFrame;
import userInterface.UIController;
import userInterface.fileEditing.newClassDialog;
import userInterface.fileEditing.newSrcDialog;


public class NodePopupMenu extends JPopupMenu {
	
	private JMenu addNew;
	private JMenuItem addClass;
	private JMenuItem addFolder;
	private JMenuItem delete; 

	

	
	
	public NodePopupMenu(boolean isFile , boolean isProject, CustomTreeNode parent , DeveloperMainFrame frame , UIController uiController , DeveloperComponent developerComponent) {
		
	
		
		addNew = new JMenu("new");
		
		addClass = new JMenuItem("class"); 
		delete = new JMenuItem("delete");

		
		addFolder = new JMenuItem("src folder");
		
		
		if(developerComponent.isConnected) {
			
			addFolder.setEnabled(false);
			delete.setEnabled(false);
			
		}
		

		
		
		
		

		addClass.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
					newClassDialog d = new newClassDialog(parent.path, parent.project); 
			
			}

		});
		
		
		
		if(!isFile) {
			

			addFolder.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
		
				
				newSrcDialog srcd = new newSrcDialog(parent.path, parent.project); 
					
					
				}

			});
			
			
		
			
		
			
			addNew.add(addClass);
			addNew.add(addFolder);
			
			add(addNew);
			
			delete.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
				
					developerComponent.setProjectFocus(parent.project);
					int result = JOptionPane.showConfirmDialog(frame,
						    "Delete this Folder?",
						    "Delete",
						    JOptionPane.OK_CANCEL_OPTION); 
					if (result == JOptionPane.OK_OPTION) {
						
						uiController.run(()-> developerComponent.deleteFile(parent.name,parent.path, parent.isFile , parent.project, parent));
					}
				}

			});
			
	
			add(delete);
			
			
		}else if (isFile){
			
			add(delete);
			delete.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					developerComponent.setProjectFocus(parent.project);

					int result = JOptionPane.showConfirmDialog(frame,
						    "Delete this class?",
						    "Delete",
						    JOptionPane.OK_CANCEL_OPTION); 
					if (result == JOptionPane.OK_OPTION) {
						
						uiController.run(()-> developerComponent.deleteFile(parent.name,parent.path, parent.isFile , parent.project, parent));
					}
				}

			});

		}else if( isProject) {
			
		}
	}

}
