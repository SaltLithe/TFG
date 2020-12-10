package userInterface.fileNavigation;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;

import core.DEBUG;
import fileManagement.FILE_PROPERTIES;
import fileManagement.FILE_TYPE;

public class CustomTreeNode extends DefaultMutableTreeNode   {
	
	
	public String path;
	public String name;
	public String project; 
	public boolean isFile;
	public boolean isProject;
	public boolean hideContents; 
	
	
	private static String folderPath =  "Icons/warning_icon.png";
	private String packagePath = "Icons/warning_icon.png";
	private String filePath = "Icons/warning_icon.png";
	
	
	
	

	
	
	public CustomTreeNode(String path, String name , String project , boolean isFile , boolean isProject ) {
		
		
		
		super();
		hideContents = false; 
		this.isProject = isProject; 
		this.isFile = isFile; 
		this.path = path;
		this.name = name; 
		this.project = project; 
		
		getMetaData(path);
		
		

	}
	
	public String getParentPath() {
		
		int last = path.lastIndexOf("\\");
		String parentpath = path.substring(0,last);
		
		return parentpath; 
	}	
	
	
	
	private void getMetaData(String path) {
		
		FILE_TYPE returning = null; 
		
		final Path file = Paths.get(path);
		final UserDefinedFileAttributeView view = Files.getFileAttributeView(file,
				UserDefinedFileAttributeView.class);
		ByteBuffer readBuffer = null;
		boolean success = false;
		int count = 0;
		while(count < FILE_PROPERTIES.properties.length && !success) {
			

			try {
				readBuffer = ByteBuffer.allocate(view.size(FILE_PROPERTIES.properties[count]));
			} catch (IOException e) {
			}
			try {
				view.read(FILE_PROPERTIES.properties[count], readBuffer);
			} catch (IOException e) {
			}
		
			try {
				readBuffer.flip();
				final String valueFromAttributes = new String(readBuffer.array(), "UTF-8");
				if (valueFromAttributes.equals(FILE_PROPERTIES.properties[count])) {
					switch (FILE_PROPERTIES.properties[count]) {
						
					case FILE_PROPERTIES.projectProperty:
						hideContents = false; 
						break;
					case FILE_PROPERTIES.srcProperty:
						hideContents = false;
						break;
					case FILE_PROPERTIES.binProperty:
						hideContents = true; 
						break;
					default:
						break;
					
					
					}
					success = true; 
				}else {
					count++;
				}
			} catch (Exception e) {
				count++;
			}
					
		}	
	}
	
	
	

	
	
}
