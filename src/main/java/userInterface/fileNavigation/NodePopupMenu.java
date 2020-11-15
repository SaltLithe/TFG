package userInterface.fileNavigation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import core.DeveloperComponent;
import userInterface.DeveloperMainFrame;
import userInterface.UIController;
import userInterface.fileEditing.newClassDialog;


public class NodePopupMenu extends JPopupMenu {
	
	private JMenu addNew;
	private JMenuItem addClass;
	private JMenuItem addFolder;
	private JMenuItem addPackage;
	private JMenuItem delete; 
	

	

	
	
	public NodePopupMenu(boolean isFile , boolean isProject, CustomTreeNode parent , DeveloperMainFrame frame , UIController uiController , DeveloperComponent developerComponent) {
		
		
		
		addNew = new JMenu("new");
		
		addClass = new JMenuItem("class"); 
		delete = new JMenuItem("delete");

		
		addFolder = new JMenuItem("src folder");
		addPackage = new JMenuItem("package");
		

		delete.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
				int result = JOptionPane.showConfirmDialog(frame,
					    "Delete this class?",
					    "Delete",
					    JOptionPane.OK_CANCEL_OPTION); 
				if (result == JOptionPane.OK_OPTION) {
					
					uiController.run(()-> developerComponent.deleteFile(parent.name,parent.path, parent.isFile , parent.project, parent));
				}
			}

		});
		

		addClass.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
			
					newClassDialog d = new newClassDialog(parent.path, parent.project); 
			
			}

		});
		
		
		
		if(!isFile && !isProject) {
			
			
			
			
		
			
			addNew.add(addClass);
			addNew.add(addFolder);
			addNew.add(addPackage);
			
			add(addNew);
			
			
	
			add(delete);
			
			
		}else if (isFile){
			
			add(delete);

		}else if( isProject) {
			
		}
	}

}
