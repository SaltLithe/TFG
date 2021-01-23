package fileManagement;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;

import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;

/**
 * Class representing a project inside a workspace It contains useful variables
 * and methods to access them
 * 
 * @author Carmen Gómez Moreno
 *
 */
public class Project {

	private File dir;
	private PropertyChangeMessenger support;
	private final String srcFolderProperty = FILE_PROPERTIES.srcProperty;
	private String fullPath;
	private String name;

	/**
	 * 
	 * @param fullPath : Path of this project
	 * @param name     : Name of this project
	 */
	public Project(String fullPath, String name) {

		this.fullPath = fullPath;
		this.name = name;

		this.dir = new File(fullPath);
		Object[] message = { dir };
		support = PropertyChangeMessenger.getInstance();
		support.notify(ObserverActions.ADD_PROJECT_TREE, message);

	}

	/**
	 * 
	 * @return Directory of this project
	 */
	public File getDir() {
		return dir;
	}

	/**
	 * Set the directory
	 * 
	 * @param dir
	 */
	public void setDir(File dir) {
		this.dir = dir;
	}

	/**
	 * 
	 * @return Path of this project
	 */
	public String getFullPath() {
		return fullPath;
	}

	/**
	 * Set the path
	 * 
	 * @param fullPath
	 */
	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
	}

	/**
	 * 
	 * @return Name of this project
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

//TODO do we use this? 
	private void scanProject() {
		File path = new File(fullPath);

		File[] files;

		files = path.listFiles();
		for (File dir : files) {
			final Path file = Paths.get(dir.getAbsolutePath());
			final UserDefinedFileAttributeView view = Files.getFileAttributeView(file,
					UserDefinedFileAttributeView.class);
			ByteBuffer readBuffer = null;
			try {
				readBuffer = ByteBuffer.allocate(view.size(srcFolderProperty));
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				view.read(srcFolderProperty, readBuffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			readBuffer.flip();
			try {
				final String valueFromAttributes = new String(readBuffer.array(), "UTF-8");
				if (valueFromAttributes.equals(this.srcFolderProperty)) {
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		}

	}

}
