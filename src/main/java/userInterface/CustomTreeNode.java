package userInterface;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;

import core.DEBUG;

public class CustomTreeNode extends DefaultMutableTreeNode  {
	
	
	public String path;
	public String name;
	public boolean isFile;
	
	
	public CustomTreeNode(String path, String name , boolean isFile) {
		
		super(name);
		this.isFile = isFile; 
		this.path = path;
		this.name = name; 
		
		

	
	

	}
	
	
}
