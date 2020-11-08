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
		editorProjects = new HashMap<String,Project>(); 
		currentPath = null;

	}

	// Metodo que dado el nombre de una clase te devuelve un string con codigo
	// basico
	public String returnBase(String name) {

		String base = "public class " + name + "{public static void main(String[] args) {}} ";
		return base;
	}

	// Metodo que busca todos los ficheros .java en el directorio actual
	public File[] returnAllFiles() {
		DEBUG.debugmessage("SE HA LLAMADO A RETURNALLFILES EN FILEMANAGER");
		File dir = new File(currentPath);
		File[] files;

		files = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				DEBUG.debugmessage("Found file with name : " + filename);
				return filename.endsWith(extension);
			}
		});
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(files);
		support.notify(ObserverActions.ENABLE_TEXTEDITORTOOLBAR_BUTTONS, null, list);
		support.notify(ObserverActions.ENABLE_SAVE_BUTTONS, null, null);
		list.clear();
		list.add(files);
		support.notify(ObserverActions.UPDATE_FILE_EXPLORER_PANEL_BUTTONS, null, list);
		return files;

	}

	// Método que recupera todos los archivos de la carpeta actual y devuelve la
	// primera instancia que encuentre
	// del archivo que se llame igual que el nombre especificado
	public File returnSingleFile(String filename) {

		DEBUG.debugmessage("SE HA LLAMADO A RETURN SINGLEFILE EN FILEMANAGER");
		File[] allFiles = returnAllFiles();
		for (int i = 0; i < allFiles.length; i++) {
			String name = allFiles[i].getName();
			// name = name.replace(extension, "");
			if (name.equals(filename)) {

				return allFiles[i];

			}

		}

		return null;

	}

	// Metodo que dado un nombre devuelve el TextFile que esté guardado con ese
	// nombre
	public TextFile getTextFile(String filename) {
		DEBUG.debugmessage("SE HA LLAMADO A GETFILE EN FILEMANAGER");
		return editorFiles.get(filename);
	}

	// Metodo que refresca los archivos a los que puede acceder el editor
	// De la carpeta seleccionada , si un archivo no se encuentra en el mapa lo crea
	@SuppressWarnings("unlikely-arg-type")
	public void updateAllFiles() {
		DEBUG.debugmessage("SE HA LLAMADO A UPDATEALLFILES EN FILEMANAGER");
		File[] ficheros = returnAllFiles();

		for (int i = 0; i < ficheros.length; i++) {
			String name = ficheros[i].getName();
			// String key = name.replace(".java", "");
			String key = ficheros[i].getAbsolutePath();
			String contents = null;
			try {
				contents = new String(Files.readAllBytes(Paths.get(ficheros[i].getAbsolutePath())));

			} catch (IOException e) {
				contents = null;
			}

			if (!editorFiles.containsKey(key)) {
				TextFile newfile = new TextFile(name ,key,contents, FileType.CLASS);
				newfile.setContent(contents);
				editorFiles.put(key, newfile);
			}

		}

		ArrayList<String> names = new ArrayList<String>(ficheros.length);
		for (int j = 0; j < ficheros.length; j++) {

			names.add(ficheros[j].getName());
		}

		for (String k : editorFiles.keySet()) {
			if (!names.contains(k)) {
				this.createClassFile(k, editorFiles.get(k).getName() , editorFiles.get(k).getContent(), false);
			}
		}

	}

//Metodo para crear un fichero para una clase , crea tanto el fichero en la carpeta elegida del sistema como
//Un objeto TextFile para el editor
	public void createClassFile(String name, String contents, String path , Boolean isfromeditor) {

		DEBUG.debugmessage("SE HA LLAMADO A CREATECLASSFILE EN FILEMANAGER");
		String nameandpath = path + "/" + name + extension;
		File newFile = new File(nameandpath);
		try {
			FileWriter fw = new FileWriter(newFile);
			if (!isfromeditor) {
				fw.write(contents);
			} else {
				fw.write(returnBase(name));
			}
			fw.close();
			TextFile newfile = new TextFile(name, path , null , FileType.CLASS);
			if (contents == null && isfromeditor) {
				newfile.setContent(returnBase(name));
			} else {
				newfile.setContent(contents);
			}
			editorFiles.put(newfile.getPath(), newfile);
		} catch (IOException e) {
			DEBUG.debugmessage("NO SE HA PODIDO CREAR EL FICHERO DE LA CLASE");
			e.printStackTrace();
		}

	}

	public void newProject(String name , WorkSpace workSpace ) {
		
		String newpath = workSpace.getPath()+"\\"+ name;
		
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
			for(File dir : files) {
				final Path file = Paths.get(dir.getAbsolutePath());
				final UserDefinedFileAttributeView view = Files.getFileAttributeView(file, UserDefinedFileAttributeView.class);
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
						if(valueFromAttributes.equals(this.projectProperty) ) {
							DEBUG.debugmessage("FOUND A PROJECT");
							this.editorProjects.put(dir.getAbsolutePath(), new Project(dir.getAbsolutePath(),dir.getName()));
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
	public String openTextFile(String name , String path, String contents) {

		DEBUG.debugmessage("SE HA LLAMADO A OPENTEXTFILE EN FILEMANAGER");
		if (currentFocus != null) {
			editorFiles.get(currentFocus).setContent(contents);

		}
		currentFocus = path;
		if(!editorFiles.containsKey(path)) {
			TextFile tf = new TextFile(name , path , contents , FileType.CLASS);
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
			 String returningcontents = new String(encoded,StandardCharsets.UTF_8);
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

	
	public void saveAllOpen(String[] contents , String[] paths) {
		
		if(contents.length == paths.length) {
			for(int i = 0 ; i < contents.length ; i ++) {
				try {
					saveCurrentFile(contents[i],paths[i]);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
		}
		
	}
	
	// Metodo para guardar todos los ficheros del editor
	public void saveAll(String editorContents ) throws IOException {
		DEBUG.debugmessage("SE HA LLAMADO A SAVEALL EN FILEMANAGER");
		
		saveCurrentFile(editorContents , null);

		File[] files = this.returnAllFiles();

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

//Metodo para guardar unicamente el fichero en el focus 
	public void saveCurrentFile(String editorcontents, String path) throws IOException {
		DEBUG.debugmessage("SE HA LLAMADO A SAVECURRENTFILE EN FILEMANAGER");
		//File file = this.returnSingleFile(currentFocus);
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
