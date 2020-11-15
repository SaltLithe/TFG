package userInterface;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import core.DEBUG;

public class CustomTreeNode extends DefaultMutableTreeNode   {
	
	
	public String path;
	public String name;
	public String project; 
	public boolean isFile;
	
	
	private static String folderPath =  "Icons/warning_icon.png";
	private String packagePath = "Icons/warning_icon.png";
	private String filePath = "Icons/warning_icon.png";
	
	

	
	
	public CustomTreeNode(String path, String name , String project , boolean isFile) {
		
		
		
		super();
		
		this.isFile = isFile; 
		this.path = path;
		this.name = name; 
		this.project = project; 
		
		

	}
	
	public String getParentPath() {
		
		int last = path.lastIndexOf("\\");
		String parentpath = path.substring(0,last);
		
		return parentpath; 
	}	
	

	
	
}
