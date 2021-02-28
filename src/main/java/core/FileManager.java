package core;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.io.FileUtils;

import networkMessages.ResponseCreateFileMessage;
import networkMessages.WriteMessage;
import observerController.ObserverActions;
import observerController.PropertyChangeMessenger;
import userInterface.uiFileManagement.FILE_PROPERTIES;
import userInterface.uiFileManagement.FILE_TYPE;
import userInterface.uiFileManagement.FileType;
import userInterface.uiFileManagement.Project;
import userInterface.uiFileManagement.TextFile;
import userInterface.uiFileManagement.WorkSpace;
import userInterface.uiFileNavigation.CustomTreeNode;

/**
 * Class that manages the files that this program uses , it keeps certain files
 * in memory in order to keep track of changes Contains useful methods to create
 * files and folders , write into files , save progress and others
 * 
 * @author Carmen Gómez Moreno
 *
 */
public class FileManager {

	private HashMap<String, TextFile> editorFiles;

	private HashMap<String, Project> editorProjects;
	private String extension = ".java";
	private PropertyChangeMessenger support;
	private ReentrantLock saveWriteLock;
	private LinkedList<ResponseCreateFileMessage> returningList;

	public FileManager() {

		saveWriteLock = new ReentrantLock();
		support = PropertyChangeMessenger.getInstance();
		editorFiles = new HashMap<>();
		editorProjects = new HashMap<>();

	}

	/**
	 * Method used to write different kinds of files
	 * 
	 * @param path     : The path of the file to be written
	 * @param contents : The file's contents
	 * @param type     : The type of file to write
	 */
	public void writeFile(String path, String contents, FILE_TYPE type) {

		File newFile = new File(path);
		FileWriter fw = null;
		try {
			fw = new FileWriter(newFile);
			fw.write(contents);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fw != null) {
					fw.close();
				}
			} catch (IOException | NullPointerException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Method used to scan a workspace and get condensed information about its
	 * folders and files
	 * 
	 * @param workspacepath : The path of the workspace to be scanned
	 * @param workspaceName : The workspace name
	 * @return a list containing messages that condense file and folder information
	 */
	public List<ResponseCreateFileMessage> scanAndReturn(String workspacepath, String workspaceName) {
		saveAllFull();
		saveWriteLock.lock();

		returningList = new LinkedList<>();
		workspaceName += "\\";
		scanAndReturnRecursive(workspacepath, true, workspacepath, workspaceName);

		saveWriteLock.unlock();
		return returningList;
	}

	/**
	 * Method that creates a class file and writes a file in the file system if a
	 * file for this class does not exist It always creates a TextFile for this
	 * class and reads the contents of the file to make them easily available inside
	 * of the TextFile object
	 * 
	 * @param name         : The name of the class to be created
	 * @param path         : The path of the class to be created
	 * @param project      : The project this class belongs to
	 * @param isMain       : Flag indicating if this class should contain a main
	 *                     method
	 */
	public void createClassFile(String name, String path, String project,  boolean isMain) {

		String nameandpath = path + FILE_PROPERTIES.singleSlash + name + extension;
		File newFile = new File(nameandpath);
		// Create a file if this class file does not exist
		FileWriter fw = null;
		try {
			 fw = new FileWriter(newFile);

			// Write a base string for this class
			if (isMain) {
				fw.write(returnMain(name));
			} else {
				fw.write(returnBase(name));

			}

			fw.close();
			TextFile newfile = new TextFile(name, path, null, FileType.CLASS);

			// Create a TextFile for this class
			editorFiles.put(newfile.getPath(), newfile);
			Object[] message = { path, name + extension, project, true };
			support.notify(ObserverActions.UPDATE_PROJECT_TREE_ADD, message);
		} catch (IOException  e) {
			
			e.printStackTrace();
		}finally {
			if(fw != null) {
				try {
					fw.close();
				} catch (IOException  | NullPointerException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * The public method we use to delete folders or files
	 * 
	 * @param name     : The name of the file or folder to be deleted
	 * @param path     : The path of the file or folder to be deleted
	 * @param isFolder : Flag indicating if we are deleting a folder
	 * @param project  : Indicates if what we are deleting is a project
	 * @param node     : The node that represents the element to be deleted in the
	 *                 file navigation ui
	 */
	public void deleteFile(String name, String path, boolean isFolder, String project, CustomTreeNode node) {

		// Remove file from memory if we have it loaded
		if (this.editorFiles.containsKey(path) && !isFolder) {
			editorFiles.remove(path);
		}

		try {
			// If its a file , delete it if it exists
			if (!isFolder) {
				Files.deleteIfExists(Paths.get(path));
				// If its not , delete recursively
			} else  {
				File f = new File(path);
				deleteInsides(f);

				FileUtils.deleteDirectory(f);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Delete classpaths and update ui
		if (project != null && node != null) {
			Object[] messageOne = { project, node };

			support.notify(ObserverActions.UPDATE_PROJECT_TREE_REMOVE, messageOne);

			Object[] messageTwo = { path, project };

			support.notify(ObserverActions.DELETE_CLASS_PATH, messageTwo);
			
			Object[] messageThree = {path};
			support.notify(ObserverActions.CLOSE_TAB,messageThree);

		} else {
			Object[] message = { path };
			support.notify(ObserverActions.DELETE_CLASS_PATH_FOCUSED, message);
		}

	}

	/**
	 * Method used to write folders with metadata
	 * 
	 * @param path       : Path where the folder should be written
	 * @param foldertype : The type of the folder to be written
	 * @param updateTree : Indicates if this method should update the user interface
	 * @param name       : The name of the folder to be written
	 * @param project    : The project this folder belongs to
	 */
	public void writeFolder(String path, FILE_TYPE foldertype, boolean updateTree, String name, String project) {

		// Create the folder
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
			final Path file = Paths.get(f.getAbsolutePath());
			final UserDefinedFileAttributeView view = Files.getFileAttributeView(file,
					UserDefinedFileAttributeView.class);

			// Set the property to write
			String property = null;
			switch (foldertype) {

			case PROJECT_FOLDER:
				property = FILE_PROPERTIES.projectProperty;
				break;
			case SRC_FOLDER:
				property = FILE_PROPERTIES.srcProperty;
				break;
			case BIN_FOLDER:
				property = FILE_PROPERTIES.binProperty;
				break;
			default:
				break;

			}
			// Write the metadata
			byte[] bytes = null;

			try {
				if(property != null) {
				bytes = property.getBytes(StandardCharsets.UTF_8);
				}
			} catch (NullPointerException e1) {
				e1.printStackTrace();
			}

			
			final ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
			writeBuffer.put(bytes);
			writeBuffer.flip();
			try {
				view.write(property, writeBuffer);
			} catch (IOException e) {
				e.printStackTrace();
			}

			// Update ui if necessary
			if (updateTree) {

				Object[] message = { path, name, project, false };
				support.notify(ObserverActions.UPDATE_PROJECT_TREE_ADD, message);

			}

		}

	}

	/**
	 * Method that creates a new project inside a workspace
	 * 
	 * @param name               : Name of the new project
	 * @param workSpace          : Workspace where the new project will be created
	 * @param includeHelpFolders : Flag indicating if this method should add src and
	 *                           bin folders
	 * @param updateEditor       : Flag indicating if the ui should be updated
	 */
	public void newProject(String name, WorkSpace workSpace, boolean includeHelpFolders, boolean updateEditor) {
		// Create the project folder
		String newpath = workSpace.getPath() + FILE_PROPERTIES.doubleSlash + name;

		writeFolder(newpath, FILE_TYPE.PROJECT_FOLDER, false, null, null);
		// Write the help folders if needed
		if (includeHelpFolders) {
			String srcpath = newpath + FILE_PROPERTIES.doubleSlash + "src";

			writeFolder(srcpath, FILE_TYPE.SRC_FOLDER, false, null, null);

			String binpath = newpath + FILE_PROPERTIES.doubleSlash + "bin";

			writeFolder(binpath, FILE_TYPE.BIN_FOLDER, false, null, null);
		}
		// Update the editor if needed
		if (updateEditor) {
			this.editorProjects.put(newpath, new Project(newpath, name));
		}

	}

	/**
	 * Method used to scan a workspace for projects
	 * 
	 * @param workspace : The workspace to search for projects from
	 */
	public List<Project> scanWorkSpace(WorkSpace workspace) {

		List<Project> returning = new ArrayList<>(); 
		// Get workspace path
		File path = new File(workspace.getPath());

		File[] files;
		// Get the files inside of the workspace
		files = path.listFiles();
		// For each file , read if they have the project metadata property
		for (File dir : files) {
			final Path file = Paths.get(dir.getAbsolutePath());
			final UserDefinedFileAttributeView view = Files.getFileAttributeView(file,
					UserDefinedFileAttributeView.class);
			ByteBuffer readBuffer = null;
			try {
				readBuffer = ByteBuffer.allocate(view.size(FILE_PROPERTIES.projectProperty));
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				view.read(FILE_PROPERTIES.projectProperty, readBuffer);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(readBuffer != null) {
			readBuffer.flip();
			}
			// If they are projeccts , create an object representing the project and add it
			// to a collection
			try {
				final String valueFromAttributes = new String(readBuffer.array(), StandardCharsets.UTF_8);
				if (valueFromAttributes.equals(FILE_PROPERTIES.projectProperty)) {
					Project newProject = new Project(dir.getAbsolutePath(),dir.getName());
					this.editorProjects.put(dir.getAbsolutePath(), newProject);
					returning.add(newProject);
				}
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

		}
		return returning; 

	}

	/**
	 * Method that gets the content of a file given its name and file , this method
	 * will create a textfile object and add it to the collection if it does not
	 * exist and return the contents of the file that needs to be opened
	 * @param name     : The name of the textfile to open
	 * @param path     : The path of the file to be opened if necessary
	 * @param contents : The contents of the file to be opened if necesary
	 * @param project  : The project this file belongs to
	 * @return a string with the contents of the file
	 */
	public String openTextFile(String name, String path, String project) {


		String returningcontents = null;
		if (!editorFiles.containsKey(path)) {
			TextFile tf = new TextFile(name, path, null, FileType.CLASS);
			editorFiles.put(path, tf);

			try {

				byte[] encoded = this.readFromTextFiles(path);
				
				returningcontents = new String(encoded, StandardCharsets.UTF_8);
				editorFiles.get(path).setContent(returningcontents);

			} catch (NullPointerException e) {
				e.printStackTrace();
				returningcontents = null;
				return returningcontents;
			}
		} else {
			returningcontents = editorFiles.get(path).getContent();

		}
		Object[] message = { returningcontents, editorFiles.get(path).getName(), editorFiles.get(path).getPath(),
				project };

		support.notify(ObserverActions.SET_TEXT_CONTENT, message);
		return returningcontents;

	}

	/**
	 * Method used to save all open files given their paths and contents to be saved
	 * 
	 * @param paths    : The paths of all of the opened files to be saved
	 * @param contents : The contents of those files that will be written into them
	 */
	public void saveAllOpen(String[] paths, String[] contents) {

		if (contents.length == paths.length) {
			for (int i = 0; i < contents.length; i++) {
				try {
					saveCurrentFile(paths[i], contents[i]);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

	}

	/**
	 * Method used to write into a closed file , used when the user receives a file
	 * creation instruction
	 * 
	 * @param path    : The path of the file to be written into
	 * @param content : The contents to write into the files
	 */
	public synchronized void writeOnClosed(String path, String content) {
		saveWriteLock.lock();
		try {

			if (!editorFiles.containsKey(path)) {
				String name = path.substring(path.lastIndexOf("\\" + 1, 0));
				editorFiles.put(path, new TextFile(name, path, content, FileType.CLASS));

			}

			editorFiles.get(path).setContent(content);

		} catch (Exception e) {
			e.printStackTrace();
			
		} finally {

			saveWriteLock.unlock();

		}

	}

	/**
	 * Method used to save all of the files that are opened in memory
	 */
	public void saveAllFull() {

		saveWriteLock.lock();
		try {
			for (String key : editorFiles.keySet()) {

				File file = new File(editorFiles.get(key).getPath());
				if(file.exists()) {
					saveFileSupport(file , key);
				}
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			saveWriteLock.unlock();
		}
	}

	/**
	 * Method used to save the opened file the user is focused in
	 * 
	 * @param path           : The path of this file
	 * @param editorcontents : The contents to be saved into this file
	 * @throws IOException
	 */
	public void saveCurrentFile(String path, String editorcontents) throws IOException {
		saveWriteLock.lock();
		FileWriter fw = null;

		try {
			
			File file = new File(path);
			if (file.exists()) {
				editorFiles.get(path).setContent(editorcontents);
				fw = new FileWriter(file);
				fw.write(editorcontents);
				fw.close();
				Object[] message = { path };
				support.notify(ObserverActions.SAVED_SINGLE_FILE, message);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(fw != null) {
			fw.close(); 
			}
			saveWriteLock.unlock();
		}
	}

	/**
	 * Method called by the ui to update the contents of a textfile when it receives
	 * a writeorder it will create a textfile if there isnt any
	 * 
	 * @param editingpath : The path of the file to be edited
	 * @param results     : The list containing the data necessary to edit the file
	 */
	public void updatePanelContents(String editingpath, ArrayList<Object> results) {
		saveWriteLock.lock();

		if (!editorFiles.keySet().contains(editingpath)) {

			File f = new File(editingpath);
			if (f.exists()) {

				String contents = null;
				try {
					contents = new String(Files.readAllBytes(Paths.get(editingpath)));
				} catch (IOException e) {
					e.printStackTrace();
				}
				TextFile newfile = new TextFile(f.getName(), f.getAbsolutePath(), contents, FileType.CLASS);
				editorFiles.put(editingpath, newfile);
			}
		}

		WriteMessage incoming = (WriteMessage) results.get(1);
		if (incoming.adding) {

			editorFiles.get(editingpath).insert(incoming.changes, incoming.offset);

		} else {

			editorFiles.get(editingpath).replace(incoming.offset, incoming.offset + incoming.lenght);
		}

		support.notify(ObserverActions.SET_SAVE_FLAG_TRUE, null);

		saveWriteLock.unlock();

	}

	/**
	 * Support method for openTextFile , reduces nesting
	 * @param path
	 */
	private byte[] readFromTextFiles (String path) {
		try {
			return Files.readAllBytes(Paths.get(path));
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}
		
		
	}

	/**
	 * Support method for saveAllFull , reduces complexity 
	 * @param file
	 */
	private void saveFileSupport (File file, String key ) {
	
		FileWriter fw = null;
		
		try {
			fw = new FileWriter(file);
			fw.write(editorFiles.get(key).getContent());
			fw.close();
	
		} catch (IOException e) {
		}finally {
			if(fw!= null) {
			try {
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
			
		}
	}

	/**
	 * Support method that just writes the declaration for a class given it's name
	 * 
	 * @param name : The name of the class
	 * @return the string declaring the class
	 */
	private String returnBase(String name) {
	
		return "public class " + name + "{" + System.lineSeparator() + "} ";
	}

	/**
	 * Support method that recursively deletes an entire folder system . Java does
	 * not allow for deletion of folders if they have other folders or files inside
	 * so we need to delete them recursively
	 * 
	 * @param file : The file or folder we start deleting from
	 * @throws IOException
	 */
	private void deleteInsides(File file) throws IOException {

		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (File f : files) {
				if (f.isDirectory()) {
					deleteInsides(f);
				} else {
					this.deleteFile(f.getName(), f.getPath(), false, null, null);
				}

			}
			
			file.delete();
		}

	}

	/**
	 * Support method that , given the name of a class it construct a base string
	 * for the declaration of a main method and a class
	 * 
	 * @param name : The name of the class
	 * @return The string declaring this class and it's main method
	 */
	private String returnMain(String name) {

		return "public class " + name + "{" + System.lineSeparator() + "public static void main(String[] args) {"
				+ System.lineSeparator() + "}" + System.lineSeparator() + "} ";
		
	}

	/**
	 * Support method used to retrieve the type of extension of a given file
	 * 
	 * @param path : The path of the file we want to check
	 * @return
	 */
	private String typeOfExtension(String path) {
		String extensionType = null;
		try {
			extensionType = path.substring(path.lastIndexOf(".") + 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (extensionType != null) {
		switch (extensionType) {
		case "java":
			return FILE_TYPE.JAVA_FILE.toString();

		case "class":
			return FILE_TYPE.CLASS_FILE.toString();
		
			
		default:
			return FILE_TYPE.ANY_FILE.toString();

		}
		}
		else {
			return FILE_TYPE.ANY_FILE.toString();
		}
	}

	/**
	 * Support method used to check the type of a folder checking it's metadata
	 * 
	 * @param path : The path of the folder to check
	 * @return
	 */
	private String typeofFile(String path) {

		String returning = null;

		// Get ready to read metadata
		final Path file = Paths.get(path);
		final UserDefinedFileAttributeView view = Files.getFileAttributeView(file, UserDefinedFileAttributeView.class);
		ByteBuffer readBuffer = null;
		boolean success = false;
		int count = 0;
		// Find the kind of property that could be written into this folder
		while (count < FILE_PROPERTIES.properties.length && !success) {

			try {
				readBuffer = ByteBuffer.allocate(view.size(FILE_PROPERTIES.properties[count]));
			} catch (IOException e) {
				
			}
			try {
				view.read(FILE_PROPERTIES.properties[count], readBuffer);
			} catch (IOException e) {
			}

			try {
				// Read metadata and return the corresponding value
				
				readBuffer.flip();
				
				final String valueFromAttributes = new String(readBuffer.array(), StandardCharsets.UTF_8);
				if (valueFromAttributes.equals(FILE_PROPERTIES.properties[count])) {
					switch (FILE_PROPERTIES.properties[count]) {

					case FILE_PROPERTIES.projectProperty:
						returning = FILE_PROPERTIES.projectProperty;
						break;
					case FILE_PROPERTIES.srcProperty:
						returning = FILE_PROPERTIES.srcProperty;

						break;
					case FILE_PROPERTIES.binProperty:
						returning = FILE_PROPERTIES.binProperty;

						break;
					default:
						break;

					}
					success = true;
				} else {
					count++;
				}
			
			} catch (Exception e) {
				count++;
			}

		}
		return returning;
	}

	/**
	 * Method that writes file information data in speciallized messages in a list
	 * in a way that any folders necessary to write any file will be always above
	 * them inside the list
	 * 
	 * @param currentpath   : The path where the method starts reading files and
	 *                      folders
	 * @param first         : Flag indicating if this is the first element to be
	 *                      scanned
	 * @param workspacePath : Partial path of the workspace this is scanning from
	 * @param workspaceName : Name of the workspace this is scanning from
	 */
	private void scanAndReturnRecursive(String currentpath, boolean first, String workspacePath, String workspaceName) {

		File current = new File(currentpath);

		if (current.exists()) {
			// If what is being scanned is a directory
			if (current.isDirectory()) {
				// If this is not the first folder to scan do not write a message

				if (!first) {
					ResponseCreateFileMessage message = new ResponseCreateFileMessage();
					message.type = typeofFile(currentpath);
					String difference = currentpath.replace(workspacePath, "");
					message.path = difference;
					returningList.add(message);
				}
				// Get the files from the directory
				File[] files = current.listFiles();

				// Call this method again
				for (File f : files) {

					scanAndReturnRecursive(f.getAbsolutePath(), false, workspacePath, workspaceName);

				}

				// If this is not a folder create the message in a different way
			} else {
				ResponseCreateFileMessage message = new ResponseCreateFileMessage();

				message.type = typeOfExtension(currentpath);
				String difference = currentpath.replace(workspacePath + "\\", "");
				difference = workspaceName + difference;
				difference = difference.substring(difference.indexOf("\\"), difference.length());

				message.path = difference;
				// If this is a class get its contents and write them into the message too
				if (!message.type.equals(FILE_TYPE.CLASS_FILE.toString())) {

					byte[] encoded = null;
					try {
						encoded = Files.readAllBytes(Paths.get(currentpath));
					} catch (IOException e) {
						e.printStackTrace();
					}
					String contents = new String(encoded, StandardCharsets.UTF_8);
					message.contents = contents;
				}
				returningList.add(message);

			}

		}

	}

}
