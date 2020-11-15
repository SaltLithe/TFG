package userInterface.fileNavigation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import userInterface.newClassDialog;


public class NodePopupMenu extends JPopupMenu {
	
	private JMenu addNew;
	private JMenuItem addClass;
	private JMenuItem addFolder;
	private JMenuItem addPackage;
	
	private JMenuItem delete; 

	
	
	public NodePopupMenu(boolean isFile , boolean isPackage , CustomTreeNode parent ) {
		

		
		
		if(!isFile && !isPackage) {
			
			addNew = new JMenu("new");
			
			addClass = new JMenuItem("class"); 
			
			
			addClass.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
				
						newClassDialog d = new newClassDialog(parent.path, parent.project); 
				
				}

			});
			
			
			
			
			addFolder = new JMenuItem("src folder");
			addPackage = new JMenuItem("package");
			
			
			addNew.add(addClass);
			addNew.add(addFolder);
			addNew.add(addPackage);
			
			add(addNew);
			
			delete = new JMenuItem("delete");
			add(delete);
			
			
		}
	}

}
