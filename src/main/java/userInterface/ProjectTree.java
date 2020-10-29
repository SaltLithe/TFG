

package userInterface;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;


import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;


public class ProjectTree extends JPanel {
  /** Construct a FileTree */
  public ProjectTree(File dir) {
    setLayout(new BorderLayout());

    // Make a tree list with all the nodes, and make it a JTree
    JTree tree = new JTree(scanAndAdd(null, dir,true));

    // Add a listener
    tree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) e
            .getPath().getLastPathComponent();
        System.out.println("You selected " + node);
      }
    });

    // Lastly, put the JTree into a JScrollPane.
    JScrollPane scrollpane = new JScrollPane();
    scrollpane.getViewport().add(tree);
    add(BorderLayout.CENTER, scrollpane);
    this.setVisible(true);
  }

  
  
  CustomTreeNode scanAndAdd(CustomTreeNode currentRootNode, File dir , boolean isStart) {
	  	String path = dir.getPath();
	  	String name = path.substring(path.lastIndexOf("\\")+1,path.length());
	  	CustomTreeNode currentNode = new CustomTreeNode(path,name);
	  	if(!isStart) {
	  		//Si no estamos empezando podemos añadir la ruta al nodo anterior 
	  		//Esto es para que el project no se añada a sí mismo
	  		currentRootNode.add(currentNode);
	  	}
	  	
	  	String[] fileArray = dir.list();
	  	ArrayList<File> files = new ArrayList<File>();
	  	
	  	for(String filename: fileArray) {
	  		File newfile = new File(path + "//" + filename);
	  		files.add(newfile);
	  	}
	  	
	  	
	  	ArrayList<File> addLater = new ArrayList<File>();
	  	for(File f : files) {
	  		/*
	  		String nextPath = null; 
	  		if(path.equals(".")) {
	  			nextPath = f.getPath();
	  		}else {
	  			nextPath = path + "//" + f.getPath();
	  		}
	  		File nextFile = new File(nextPath);
	  		*/
	  		if(f.isDirectory()) {
	  			scanAndAdd(currentNode,f,false);
	  		}else {
	  			
	  			addLater.add(f);
	  		}


	  	
	  

	  	}
	  	//No puedes iterar y añadir no se por que
	  	for(File g : addLater) {
	  		String filepath = g.getAbsolutePath();
	  		String filename = filepath.substring(filepath.lastIndexOf("\\")+1,filepath.length());
	  		currentNode.add(new CustomTreeNode(g.getAbsolutePath(),filename));
		
  }
	  	return currentNode;
  }
  
  
  
  
 
  public Dimension getMinimumSize() {
    return new Dimension(200, 400);
  }

  public Dimension getPreferredSize() {
    return new Dimension(200, 400);
  }

  /** Main: make a Frame, add a FileTree */

}




           
         