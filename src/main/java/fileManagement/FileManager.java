package fileManagement;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFileChooser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import core.DEBUG;
import core.DeveloperComponent;
import network.ResponseCreateFileMessage;
import network.WriteMessage;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;
import userInterface.UIController;
import userInterface.fileNavigation.CustomTreeNode;
import org.apache.commons.*;

/*Clase que maneja todos los archivos sobre los que trabaja la aplicación , contiene un hashmap en el que
 * guarda objetos de la clase TextFile que se corresponden a ficheros en la carpeta actual que se guarda en 
 * currentPath , tambien contiene una referencia al archivo en el que se está trabajando con currentFocus
 * 
 */
public class FileManager {

	private JFileChooser fileChooser;
	private HashMap<String, TextFile> editorFiles;

	private HashMap<String, Project> editorProjects;
	private String currentPath;
	private String currentFocus = null;
	private String extension = ".java";
	private PropertyChangeMessenger support;
	private UIController uiController;
	private DeveloperComponent developerComponent;
	private ReentrantLock saveWriteLock;
	private LinkedList<ResponseCreateFileMessage> returningList; 
	
	public String getCurrentFolder() {
		return currentPath;
	}

	public FileManager() {
		DEBUG.debugmessage("SE HA INVOCADO AL CONSTRUCTOR DE FILEMANAGER");

		saveWriteLock = new ReentrantLock();
		uiController = UIController.getInstance();
		developerComponent = uiController.getDeveloperComponent();
		// Inicializa los objetos necesarios y variables necesarias

		support = PropertyChangeMessenger.getInstance();
		fileChooser = new JFileChooser();
		editorFiles = new HashMap<String, TextFile>();
		editorProjects = new HashMap<String, Project>();
		currentPath = null;

	}

	// Metodo que dado el nombre de una clase te devuelve un string con codigo
	// basico
	public String returnMain(String name) {

		String base = "public class " + name + "{" + System.lineSeparator() +"public static void main(String[] args) {"+System.lineSeparator()+"}"+System.lineSeparator()+"} ";
		return base;
	}

	// Metodo que dado un nombre devuelve el TextFile que esté guardado con ese
	// nombre
	public TextFile getTextFile(String filename) {
		DEBUG.debugmessage("SE HA LLAMADO A GETFILE EN FILEMANAGER");
		return editorFiles.get(filename);
	}

//Metodo para crear un fichero para una clase , crea tanto el fichero en la carpeta elegida del sistema como
//Un objeto TextFile para el editor
	public void createClassFile(String name, String path, String project, Boolean isfromeditor, boolean isMain) {

		DEBUG.debugmessage("SE HA LLAMADO A CREATECLASSFILE EN FILEMANAGER");

		String nameandpath = path + "/" + name + extension;
		File newFile = new File(nameandpath);
		try {
			FileWriter fw = new FileWriter(newFile);

			if(isMain) {
			fw.write(returnMain(name));
			}else {
				fw.write(returnBase(name));

			}

			fw.close();
			TextFile newfile = new TextFile(name, path, null, FileType.CLASS);

			editorFiles.put(newfile.getPath(), newfile);

			ArrayList<Object> list = new ArrayList<Object>();
			list.add(path);
			list.add(name + extension);
			list.add(project);
			list.add(true);
			support.notify(ObserverActions.UPDATE_PROJECT_TREE_ADD, null, list);
		} catch (IOException e) {
			DEBUG.debugmessage("NO SE HA PODIDO CREAR EL FICHERO DE LA CLASE");
			e.printStackTrace();
		}

	}

	private String returnBase(String name) {
		String base = "public class " + name + "{"
				+ System.lineSeparator() + "} ";
		return base;
}

	private void deleteInsides(File file) throws IOException {
		
		if(file.isDirectory()) {
			File[] files = file.listFiles();
			for(File f : files) {
				if(f.isDirectory()) {
					deleteInsides(f);
				}else {
					this.deleteFile(f.getName(), f.getPath(), false, null, null );
				}
				
			}
			file.delete();
		}
	
		
		
	}

	public void deleteFile(String name, String path, boolean isFolder, String project, CustomTreeNode node) {

		if (this.editorFiles.containsKey(path) && !isFolder) {
			editorFiles.remove(path);
		}

		try {
			if (!isFolder) {
				Files.deleteIfExists(Paths.get(path));
			} else if (isFolder) {
				File f = new File(path);
				deleteInsides(f);

				FileUtils.deleteDirectory(f);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		ArrayList<Object> list = new ArrayList<Object>();
		
		if(project!= null  && node != null) {
		list.add(project);
		list.add(node);

		support.notify(ObserverActions.UPDATE_PROJECT_TREE_REMOVE, null, list);
		list.clear();
		list.add(path);
		list.add(project);
		support.notify(ObserverActions.DELETE_CLASS_PATH, null, list);

		}else {
			list.add(path);
			support.notify(ObserverActions.DELETE_CLASS_PATH_FOCUSED,null,list);
		}
		
	}

	public void writeFolder(String path, FILE_TYPE foldertype , boolean updateTree , String name , String project) {

		File f = new File(path);
		f.mkdir();
		final Path file = Paths.get(f.getAbsolutePath());
		final UserDefinedFileAttributeView view = Files.getFileAttributeView(file, UserDefinedFileAttributeView.class);

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
		byte[] bytes = null;

		try {
			bytes = property.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		final ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
		writeBuffer.put(bytes);
		writeBuffer.flip();
		try {
			view.write(property, writeBuffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(updateTree) {
			
			ArrayList<Object> list = new ArrayList<Object>();
			list.add(path);
			list.add(name);
			list.add(project);
			list.add(false);
			support.notify(ObserverActions.UPDATE_PROJECT_TREE_ADD, null, list);

		}
		
		
		
		
	}

	public void newProject(String name, WorkSpace workSpace) {

		String newpath = workSpace.getPath() + "\\" + name;

		writeFolder(newpath, FILE_TYPE.PROJECT_FOLDER,false , null , null );

		String srcpath = newpath + "\\" + "src";

		writeFolder(srcpath, FILE_TYPE.SRC_FOLDER, false , null , null );

		String binpath = newpath + "\\" + "bin";

		writeFolder(binpath, FILE_TYPE.BIN_FOLDER, false , null , null );
		this.editorProjects.put(newpath, new Project(newpath, name));

		DEBUG.debugmessage("PROYECTO CREADO");

	}

	public void scanWorkSpace(WorkSpace workspace) {

		File path = new File(workspace.getPath());

		File[] files;

		files = path.listFiles();
		for (File dir : files) {
			final Path file = Paths.get(dir.getAbsolutePath());
			final UserDefinedFileAttributeView view = Files.getFileAttributeView(file,
					UserDefinedFileAttributeView.class);
			ByteBuffer readBuffer = null;
			try {
				readBuffer = ByteBuffer.allocate(view.size(FILE_PROPERTIES.projectProperty));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				view.read(FILE_PROPERTIES.projectProperty, readBuffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			readBuffer.flip();
			try {
				final String valueFromAttributes = new String(readBuffer.array(), "UTF-8");
				if (valueFromAttributes.equals(FILE_PROPERTIES.projectProperty)) {
					DEBUG.debugmessage("FOUND A PROJECT");
					this.editorProjects.put(dir.getAbsolutePath(), new Project(dir.getAbsolutePath(), dir.getName()));
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	// Metodo que abre un dialogo para elegir la carpeta actual
	private String openFolder() {

		DEBUG.debugmessage("SE HA LLAMADO A OPENFOLDER EN FILEMANAGER");

		fileChooser.setCurrentDirectory(new java.io.File("."));
		fileChooser.setDialogTitle("Select a Folder to Open");
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setAcceptAllFileFilterUsed(false);
		int returnValue = fileChooser.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			if (fileChooser.getSelectedFile().isDirectory()) {
				File path = fileChooser.getSelectedFile();
				currentPath = path.getAbsolutePath();
				System.out.println("You selected the directory: " + fileChooser.getSelectedFile());
			}
		}
		return currentPath;
	}

	// Metodo que devuelve el contenido de un TextFile
	public String getTextFileContent(String filename) {

		DEBUG.debugmessage("SE HA LLAMADO A GETTEXTFILECONTENT EN FILEMANAGER");

		TextFile file = editorFiles.get(filename);
		return file.getContent();

	}

	// Metodo que abre el TextFile correspondiente al nombre dado , si el focus ya
	// no es nulo , lo cual
	// significa que hay un fichero abierto en el editor , se guardarán antes los
	// cambios en su correspondiente
	// textfile
	public String openTextFile(String name, String path, String contents, String project) {

		DEBUG.debugmessage("SE HA LLAMADO A OPENTEXTFILE EN FILEMANAGER");
		if (currentFocus != null && contents != null) {
			editorFiles.get(currentFocus).setContent(contents);

		}
		currentFocus = path;
		if (!editorFiles.containsKey(path)) {
			TextFile tf = new TextFile(name, path, contents, FileType.CLASS);
			editorFiles.put(path, tf);

		}

		try {
			File file = new File(path);
			byte[] encoded = null;
			try {
				encoded = Files.readAllBytes(Paths.get(path));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String returningcontents = new String(encoded, StandardCharsets.UTF_8);
			ArrayList<Object> list = new ArrayList<Object>();
			list.add(returningcontents);
			list.add(editorFiles.get(path).getName());
			list.add(editorFiles.get(path).getPath());
			list.add(project);
			support.notify(ObserverActions.SET_TEXT_CONTENT, null, list);
			return returningcontents;
		} catch (NullPointerException e) {
			DEBUG.debugmessage("Por que señor");
			e.printStackTrace();
			String returningcontents = null;
			return returningcontents;
		}

		// TODO Auto-generated method stub

	}

	public void saveAllOpen(String[] paths, String[] contents) {

		if (contents.length == paths.length) {
			for (int i = 0; i < contents.length; i++) {
				try {
					saveCurrentFile(paths[i], contents[i]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	public synchronized void writeOnClosed(String path, String content) {
		saveWriteLock.lock();
		try {

			if (!editorFiles.containsKey(path)) {
				String name = path.substring(path.lastIndexOf("\\" + 1, 0));
				editorFiles.put(path, new TextFile(name, path, content, FileType.CLASS));

			}

			editorFiles.get(path).setContent(content);

		} catch (Exception e) {
		} finally {

			saveWriteLock.unlock();

		}

	}

	public void saveAllFull() {

		saveWriteLock.lock();
		try {
			for (String key : editorFiles.keySet()) {

				File file = new File(editorFiles.get(key).getPath());
				if (file != null) {
					FileWriter fw = null;
					try {
						fw = new FileWriter(file);
						fw.write(editorFiles.get(key).getContent());
						fw.close();

					} catch (IOException e) {
					}

				}

			}
		} catch (Exception e) {
		} finally {
			saveWriteLock.unlock();

		}

	}

	// Metodo para guardar todos los ficheros del editor
	public void saveAll(String editorContents) throws IOException {
		saveWriteLock.lock();
		try {
			DEBUG.debugmessage("SE HA LLAMADO A SAVEALL EN FILEMANAGER");

			saveCurrentFile(editorContents, null);

			// File[] files = this.returnAllFiles();
			File[] files = this.workspaceFullSearch();

			for (int i = 0; i < files.length; i++) {
				String name = files[i].getName();
				name = name.replace(extension, "");
				if (!name.equals(currentFocus)) {
					String contents = editorFiles.get(name).getContent();
					FileWriter fw = new FileWriter(files[i]);
					fw.write(contents);
					fw.close();
				}
			}
		} catch (Exception e) {
		} finally {
			saveWriteLock.unlock();
		}
	}

	private File[] workspaceFullSearch() {
		// TODO Auto-generated method stub
		return null;
	}

//Metodo para guardar unicamente el fichero en el focus 
	public void saveCurrentFile(String path, String editorcontents) throws IOException {
		saveWriteLock.lock();
		try {
			DEBUG.debugmessage("SE HA LLAMADO A SAVECURRENTFILE EN FILEMANAGER");
			// File file = this.returnSingleFile(currentFocus);
			File file = new File(path);
			if (file != null) {
				editorFiles.get(path).setContent(editorcontents);
				FileWriter fw = new FileWriter(file);
				fw.write(editorcontents);
				fw.close();
				ArrayList<Object> list = new ArrayList<Object>();
				list.add(path);
				support.notify(ObserverActions.SAVED_SINGLE_FILE, null, list);

			}
		} catch (Exception e) {
		} finally {

			saveWriteLock.unlock();

		}

	}

	// Metodo que devuelve el textfile correspondiente al focus actual
	public TextFile getCurrentTextFile() {
		DEBUG.debugmessage("SE HA LLAMADO A GETCURRENTTEXTFILE EN FILEMANAGER");
		TextFile returning = editorFiles.get(currentFocus);
		return returning;
	}

	// Metodo que devuelve el focus actual
	public String getCurrentFocus() {
		DEBUG.debugmessage("SE HA LLAMADO A GETCURRENTFOCUS EN FILEMANAGER");
		return currentFocus;
	}

	public void updatePanelContents(String editingpath, ArrayList<Object> results) {
		saveWriteLock.lock();

		if (!editorFiles.keySet().contains(editingpath)) {

			File f = new File(editingpath);
			if (f != null) {

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

		support.notify(ObserverActions.SET_SAVE_FLAG_TRUE, null, null);

		saveWriteLock.unlock();

	}
	
	
	private String typeOfExtension(String path) {
		String extension = null;
		try {
		extension = path.substring(path.lastIndexOf(".") + 1);
		} catch (Exception e) {
		}
		switch (extension) {
		case "java":
			return  FILE_TYPE.JAVA_FILE.toString();
		
		case "class":
			return FILE_TYPE.CLASS_FILE.toString(); 

		default:
			return  FILE_TYPE.ANY_FILE.toString();
	

		}
	}
	
	
	private String typeofFile(String path) {
		
		
		String returning = null; 
		
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
						returning = FILE_PROPERTIES.projectProperty.toString();
						break;
					case FILE_PROPERTIES.srcProperty:
						returning = FILE_PROPERTIES.srcProperty.toString();

						break;
					case FILE_PROPERTIES.binProperty:
						returning = FILE_PROPERTIES.binProperty.toString();

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
		return returning; 
	}
	
	
	
	private void scanAndReturnRecursive(String currentpath , boolean first  , String workspacePath , String workspaceName ){
		
		File current = new File(currentpath);
		
		if (current.exists()) {
			
			if(current.isDirectory()) {
				
				if(!first) {
				ResponseCreateFileMessage message = new ResponseCreateFileMessage(); 
				message.type = typeofFile(currentpath);
				String difference =currentpath.replace(workspacePath, "");
				difference = workspaceName + difference; 
				
				message.path = difference;
				returningList.add(message);
				}
				
				File[] files = current.listFiles();
				
				for(File f : files) {
					
					scanAndReturnRecursive(f.getAbsolutePath(),false,workspacePath, workspaceName);
					
				}
				
				
				
			}else {
				ResponseCreateFileMessage message = new ResponseCreateFileMessage(); 
				
				message.type = typeOfExtension(currentpath);
				String difference =currentpath.replace(workspacePath, "");
				difference = workspaceName + difference; 

				
				message.path = difference;
				
				if(!message.type.equals(FILE_TYPE.CLASS_FILE.toString())) {
				
				
				byte[] encoded = null;
				try {
					encoded = Files.readAllBytes(Paths.get(currentpath));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String contents = new String(encoded, StandardCharsets.UTF_8);
				message.contents = contents; 
				}
				returningList.add(message);
				
				
				
				
				
			}
			
			
			
		}
		
		
	}
	

	public LinkedList<ResponseCreateFileMessage> scanAndReturn(String workspacepath , String workspaceName) {
		saveAllFull();
		saveWriteLock.lock();
		
		returningList = new LinkedList<ResponseCreateFileMessage>(); 
		workspaceName += "\\";
		scanAndReturnRecursive(workspacepath,true,workspacepath , workspaceName);
		
		
		saveWriteLock.unlock();
		return returningList;
	}

}
