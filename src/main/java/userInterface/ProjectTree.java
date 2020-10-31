

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



public class ProjectTree extends JPanel {
	
	private JTree tree; 

  public ProjectTree(File dir) {
    setLayout(new BorderLayout());

    // Make a tree list with all the nodes, and make it a JTree
     tree = new JTree(scanAndAdd(null, dir,true));

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
    
    tree.addMouseListener ( new MouseAdapter ()
    {
        public void mouseClicked ( MouseEvent e )
        {
            if ( SwingUtilities.isLeftMouseButton ( e ) )
            {
                TreePath path = tree.getPathForLocation ( e.getX (), e.getY () );
                Rectangle pathBounds = tree.getUI ().getPathBounds ( tree, path );
                if ( pathBounds != null && pathBounds.contains ( e.getX (), e.getY () ) )
                {
                	
                	CustomTreeNode clickedNode = (CustomTreeNode) path.getPathComponent(0);
                	
                //	if(!clickedNode.isFile)
                	/*
                    JPopupMenu menu = new JPopupMenu ();
                    menu.add ( new JMenuItem ( "Test" ) );
                    menu.show ( tree, pathBounds.x, pathBounds.y + pathBounds.height );
                    */
                }
            }
        }
    } );
    
    
  }

  
  
  CustomTreeNode scanAndAdd(CustomTreeNode currentRootNode, File dir , boolean isStart) {
	  	String path = dir.getPath();
	  	String name = path.substring(path.lastIndexOf("\\")+1,path.length());
	  	CustomTreeNode currentNode = new CustomTreeNode(path,name,false);
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
	  		currentNode.add(new CustomTreeNode(g.getAbsolutePath(),filename,true));
	  		
	  		
		
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




           
         