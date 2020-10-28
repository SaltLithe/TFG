package fileManagement;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import core.DEBUG;
import userInterface.FileTree;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;

import javax.swing.JMenu;
import java.awt.FlowLayout;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTree;

public class Project {
	
	
	private HashMap<String,SrcFolder> projectFolders;
	private HashMap<String,File> projectFiles;
	private File dir;
	private PropertyChangeMessenger pcm; 
	
	private final String srcFolderProperty = "pairleap.srcfolder";

	
	
	private String fullPath;
	private String name; 
	
	public Project(String fullPath , String name) {
		
		this.fullPath = fullPath;
		this.name = name;
		
		this.dir = new File(fullPath) ;
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(dir);
		pcm = PropertyChangeMessenger.getInstance();
		pcm.notify(ObserverActions.ADD_PROJECT_TREE, null, list);
	
		
		
		
	}
	
	
	public File getDir() {
		return dir;
	}


	public void setDir(File dir) {
		this.dir = dir;
	}


	public String getFullPath() {
		return fullPath;
	}


	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	private void scanProject() {
File path = new File(fullPath);
		
		
		File[] files;

		files = path.listFiles();
			for(File dir : files) {
				final Path file = Paths.get(dir.getAbsolutePath());
				final UserDefinedFileAttributeView view = Files.getFileAttributeView(file, UserDefinedFileAttributeView.class);
				 ByteBuffer readBuffer = null;
				try {
					readBuffer = ByteBuffer.allocate(view.size(srcFolderProperty));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				    try {
						view.read(srcFolderProperty, readBuffer);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    readBuffer.flip();
				    try {
						final String valueFromAttributes = new String(readBuffer.array(), "UTF-8");
						if(valueFromAttributes.equals(this.srcFolderProperty) ) {
							DEBUG.debugmessage("FOUND A SRC FOLDER");
						}
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
				
			}
	
		
		
	}
	

}
