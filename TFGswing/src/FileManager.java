
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import javax.swing.JFileChooser;

import developer.FileType;
import developer.TextFile;

/*Clase que maneja todos los archivos sobre los que trabaja la aplicación , contiene un hashmap en el que
 * guarda objetos de la clase TextFile que se corresponden a ficheros en la carpeta actual que se guarda en 
 * currentPath , tambien contiene una referencia al archivo en el que se está trabajando con currentFocus
 * 
 */
public class FileManager {

	private JFileChooser fileChooser;
	private HashMap<String, TextFile> editorFiles;
	private String currentPath;
	private String currentFocus = null;
	private String extension = ".java";

	private TextEditorToolbar textEditorToolbar;

	public String getCurrentFolder() {
		return currentPath;
	}

	public FileManager(TextEditorToolbar textEditorToolbar) {
		DEBUG.debugmessage("SE HA INVOCADO AL CONSTRUCTOR DE FILEMANAGER");

		// Inicializa los objetos necesarios y variables necesarias
		this.textEditorToolbar = textEditorToolbar;
		fileChooser = new JFileChooser();
		editorFiles = new HashMap<String, TextFile>();
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

		return dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(extension);
			}
		});

	}

	// Método que recupera todos los archivos de la carpeta actual y devuelve la
	// primera instancia que encuentre
	// del archivo que se llame igual que el nombre especificado
	public File returnSingleFile(String filename) {

		DEBUG.debugmessage("SE HA LLAMADO A RETURN SINGLEFILE EN FILEMANAGER");
		File[] allFiles = returnAllFiles();
		for (int i = 0; i < allFiles.length; i++) {
			String name = allFiles[i].getName();
			name = name.replace(extension, "");
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
	public void updateAllFiles() {
		DEBUG.debugmessage("SE HA LLAMADO A UPDATEALLFILES EN FILEMANAGER");
		File[] ficheros = returnAllFiles();

		for (int i = 0; i < ficheros.length; i++) {
			String name = ficheros[i].getName();
			String key = name.replace(".java", "");
			String contents = null;
			try {
				contents = new String(Files.readAllBytes(Paths.get(ficheros[i].getAbsolutePath())));

			} catch (IOException e) {
				contents = null;
			}

			if (!editorFiles.containsKey(key)) {
				createClassFile(key, contents, false);
				TextFile newfile = new TextFile(key, FileType.CLASS);
				newfile.setContent(contents);
				editorFiles.put(key, newfile);
			}

		}

	}

//Metodo para crear un fichero para una clase , crea tanto el fichero en la carpeta elegida del sistema como
//Un objeto TextFile para el editor
	public void createClassFile(String Name, String contents, Boolean isfromeditor) {

		DEBUG.debugmessage("SE HA LLAMADO A CREATECLASSFILE EN FILEMANAGER");
		String nameandpath = currentPath + "/" + Name + extension;
		File newFile = new File(nameandpath);
		try {
			FileWriter fw = new FileWriter(newFile);
			if (!isfromeditor) {
				fw.write(contents);
			} else {
				fw.write(returnBase(Name));
			}
			fw.close();
			TextFile newfile = new TextFile(Name, FileType.CLASS);
			if (contents == null && isfromeditor) {
				newfile.setContent(returnBase(Name));
			} else {
				newfile.setContent(contents);
			}
			editorFiles.put(Name, newfile);
		} catch (IOException e) {
			DEBUG.debugmessage("NO SE HA PODIDO CREAR EL FICHERO DE LA CLASE");
			e.printStackTrace();
		}

	}

	// Metodo que abre un dialogo para elegir la carpeta actual
	public void openFolder(DeveloperMainFrame developerMainFrame) {

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
	public String openTextFile(String name, String contents) {

		DEBUG.debugmessage("SE HA LLAMADO A OPENTEXTFILE EN FILEMANAGER");
		if (currentFocus != null) {
			editorFiles.get(currentFocus).setContent(contents);

		}
		currentFocus = name;

		try {
			String returningcontents = editorFiles.get(name).getContent();
			return returningcontents;
		} catch (NullPointerException e) {
			String returningcontents = null;
			return returningcontents;
		}

		// TODO Auto-generated method stub

	}

	// Metodo para guardar todos los ficheros del editor
	public void saveAll() throws IOException {
		DEBUG.debugmessage("SE HA LLAMADO A SAVEALL EN FILEMANAGER");
		saveCurrentFile();

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
	public void saveCurrentFile() throws IOException {
		DEBUG.debugmessage("SE HA LLAMADO A SAVECURRENTFILE EN FILEMANAGER");
		File file = this.returnSingleFile(currentFocus);
		String editorcontents = textEditorToolbar.getContents();
		if (file != null) {
			editorFiles.get(currentFocus).setContent(editorcontents);
			FileWriter fw = new FileWriter(file);
			fw.write(editorcontents);
			fw.close();

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
