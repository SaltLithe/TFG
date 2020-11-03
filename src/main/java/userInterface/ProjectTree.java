

package userInterface;

import java.awt.BorderLayout;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import core.DEBUG;
import core.DeveloperComponent;



public class ProjectTree extends JPanel {
	
	private JTree tree; 
	private UIController uiController;
	private DeveloperComponent developerComponent;

  public ProjectTree(File dir) {
	  
	  uiController = UIController.getInstance();
	  developerComponent = uiController.getDeveloperComponent(); 
	  
    setLayout(new BorderLayout());

     tree = new JTree(scanAndAdd(null, dir,true));

     
    tree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
       CustomTreeNode node = (CustomTreeNode) e
            .getPath().getLastPathComponent();
            if(node.isFile){
       
            	uiController.run(()-> developerComponent.openFile(node.name , node.path, null));
            	//TODO abrir archivo y poner una jodida tab
            	
        }
      }
    });
    

    JScrollPane scrollpane = new JScrollPane();
    scrollpane.getViewport().add(tree);
    add(BorderLayout.CENTER, scrollpane);
    this.setVisible(true);
    
    
    
    
  }

  
  
  CustomTreeNode scanAndAdd(CustomTreeNode currentRootNode, File dir , boolean isStart) {
	  	String path = dir.getPath();
	  	String name = path.substring(path.lastIndexOf("\\")+1,path.length());
	  	boolean isDir = true;
	  	if(dir.isDirectory()) {isDir = false;}
	  	CustomTreeNode currentNode = new CustomTreeNode(path,name,isDir);
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
	  		boolean isFile= true; 
	  		if(g.isDirectory()) {isFile = false;}
	  		currentNode.add(new CustomTreeNode(g.getAbsolutePath(),filename,isFile));
	  		
	  		
		
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




           
         