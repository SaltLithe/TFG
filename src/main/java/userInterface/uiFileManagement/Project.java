package userInterface.uiFileManagement;

import java.io.File;

import observerController.ObserverActions;
import observerController.PropertyChangeMessenger;

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



}
