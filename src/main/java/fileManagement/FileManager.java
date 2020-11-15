package fileManagement;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
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

import javax.swing.JFileChooser;

import core.DEBUG;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;

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

	public final String projectProperty = "pairleap.projectfolder";

	public String getCurrentFolder() {
		return currentPath;
	}

	public FileManager() {
		DEBUG.debugmessage("SE HA INVOCADO AL CONSTRUCTOR DE FILEMANAGER");

		// Inicializa los objetos necesarios y variables necesarias

		support = PropertyChangeMessenger.getInstance();
		fileChooser = new JFileChooser();
		editorFiles = new HashMap<String, TextFile>();
		editorProjects = new HashMap<String, Project>();
		currentPath = null;

	}

	// Metodo que dado el nombre de una clase te devuelve un string con codigo
	// basico
	public String returnBase(String name) {

		String base = "public class " + name + "{public static void main(String[] args) {}} ";
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
	public void createClassFile(String name, String path, String project, Boolean isfromeditor) {

		DEBUG.debugmessage("SE HA LLAMADO A CREATECLASSFILE EN FILEMANAGER");

		String nameandpath = path + "/" + name + extension;
		File newFile = new File(nameandpath);
		try {
			FileWriter fw = new FileWriter(newFile);

			fw.write(returnBase(name));

			fw.close();
			TextFile newfile = new TextFile(name, path, null, FileType.CLASS);

			editorFiles.put(newfile.getPath(), newfile);

			ArrayList<Object> list = new ArrayList<Object>();
			list.add(path);
			list.add(name + extension);
			list.add(true);
			list.add(project);

			support.notify(ObserverActions.UPDATE_PROJECT_TREE, null, list);
		} catch (IOException e) {
			DEBUG.debugmessage("NO SE HA PODIDO CREAR EL FICHERO DE LA CLASE");
			e.printStackTrace();
		}

	}

	public void newProject(String name, WorkSpace workSpace) {

		String newpath = workSpace.getPath() + "\\" + name;

		File f = new File(newpath);
		f.mkdir();
		final Path file = Paths.get(f.getAbsolutePath());

		final UserDefinedFileAttributeView view = Files.getFileAttributeView(file, UserDefinedFileAttributeView.class);

		/* The file attribute */
		final String projectProperty = "pairleap.projectfolder";
		/* Write the properties */
		byte[] bytes = null;
		try {
			bytes = projectProperty.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		final ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
		writeBuffer.put(bytes);
		writeBuffer.flip();
		try {
			view.write(projectProperty, writeBuffer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
				readBuffer = ByteBuffer.allocate(view.size(projectProperty));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				view.read(projectProperty, readBuffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			readBuffer.flip();
			try {
				final String valueFromAttributes = new String(readBuffer.array(), "UTF-8");
				if (valueFromAttributes.equals(this.projectProperty)) {
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
	public String openTextFile(String name, String path, String contents) {

		DEBUG.debugmessage("SE HA LLAMADO A OPENTEXTFILE EN FILEMANAGER");
		if (currentFocus != null) {
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

	public void saveAllOpen(String[] contents, String[] paths) {

		if (contents.length == paths.length) {
			for (int i = 0; i < contents.length; i++) {
				try {
					saveCurrentFile(contents[i], paths[i]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	// Metodo para guardar todos los ficheros del editor
	public void saveAll(String editorContents) throws IOException {
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

	}

	private File[] workspaceFullSearch() {
		// TODO Auto-generated method stub
		return null;
	}

//Metodo para guardar unicamente el fichero en el focus 
	public void saveCurrentFile(String editorcontents, String path) throws IOException {
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

}
