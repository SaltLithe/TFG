package userInterface.fileNavigation;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import core.DEBUG;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.ScrollPaneConstants;
import javax.swing.tree.AbstractLayoutCache;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;

@SuppressWarnings("serial")
public

/*
 * Clase que implementa el panel que contiene todos los botones necesarios para
 * cambiar de archivos en el editor , sus únicos comportamientos son añadir los
 * botones que se le indica e indicar si un boton a añadir existe para que no
 * sea duplicado
 */
class FileExplorerPanel extends JPanel {
	JScrollPane scrollPane ;
	LinkedList<JTree> treeList =  new LinkedList<JTree>(); 
	JPanel newpane; 

	public FileExplorerPanel() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		 newpane = new JPanel(); 
		 newpane.setLayout(new BoxLayout(newpane, BoxLayout.Y_AXIS));
		 newpane.setAlignmentX(LEFT_ALIGNMENT);
		 newpane.setAlignmentY(TOP_ALIGNMENT);

		DEBUG.debugmessage("SE HA INVOCADO AL CONSTRUCTOR DE FILEEXPLORERPANEL");

		setVisible(true);

	}
	
	  public DefaultTreeModel visitAllNodes(CustomTreeNode node , DefaultTreeModel model ) {
		    System.out.println(node);
		    if (node.getChildCount() >= 0) {
		      for (Enumeration e = node.children(); e.hasMoreElements();) {
		        CustomTreeNode n = (CustomTreeNode) e.nextElement();
		        model.nodeChanged(n);
		        visitAllNodes(n,model);
		      }
		    }
		    return model; 
	  }

	
	public void addProjectTree(ProjectTree tree) {
		
		newpane.add(tree);
		
		this.updateUI();
		this.removeAll();
		scrollPane = new JScrollPane(newpane);
		
	
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setSize(getPreferredSize());
		add(scrollPane);
		
		scrollPane.updateUI();
		
		this.updateUI();
		
	}

	public void removeAndClear(ProjectTree tree) {


		newpane.remove(tree);
		this.updateUI();
		this.removeAll();
		scrollPane = new JScrollPane(newpane);
		
	
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setSize(getPreferredSize());
		add(scrollPane);
		
		scrollPane.updateUI();
		
		this.updateUI();
		
	}
}
