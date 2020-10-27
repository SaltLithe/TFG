package fileManagement;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.HashMap;

import javax.swing.JPanel;

import core.DEBUG;
import javax.swing.JMenu;
import java.awt.FlowLayout;
import javax.swing.JMenuItem;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Project extends JPanel{
	
	
	private HashMap<String,SrcFolder> projectFolders;
	private HashMap<String,File> projectFiles;
	
	private final String srcFolderProperty = "pairleap.srcfolder";

	
	
	private String fullPath;
	private String name; 
	
	public Project(String fullPath , String name) {
		FlowLayout flowLayout = (FlowLayout) getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		
		this.fullPath = fullPath;
		this.name = name;
		
		JButton btnNewButton = new JButton("Extend");
		add(btnNewButton);
		
		JLabel lblNewLabel = new JLabel(name);
		add(lblNewLabel);
		
		
		
		
		
		
		
		
		
		
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
