
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

//
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
		File dir = new File(currentPath);

		return dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String filename) {
				return filename.endsWith(extension);
			}
		});

	}

	public File returnSingleFile(String filename) {

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

	public TextFile getFile(String filename) {

		return editorFiles.get(filename);
	}

	// Metodo que refresca los archivos a los que puede acceder el editor
	// De la carpeta seleccionada , si un archivo no se encuentra en el mapa lo crea
	public void updateAllFiles() {
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

	public void createClassFile(String Name, String contents, Boolean isfromeditor) {

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
			// TODO Auto-generated catch block
			System.err.println("No se ha podido crear la clase ");
			e.printStackTrace();
		}

	}

	// Metodo que abre un dialogo para elegir la carpeta actual
	public void openFolder(DeveloperMainFrame developerMainFrame) {

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

	// Metodo que devuelve el contenido de un fichero
	public String getFileContent(String filename) {

		TextFile file = editorFiles.get(filename);
		return file.getContent();

	}

	public String openFile(String name, String contents) {
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

	public void saveAll() throws IOException {
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

	public void saveCurrentFile() throws IOException {
		File file = this.returnSingleFile(currentFocus);
		String editorcontents = textEditorToolbar.getContents();
		if (file != null) {
			editorFiles.get(currentFocus).setContent(editorcontents);
			FileWriter fw = new FileWriter(file);
			fw.write(editorcontents);
			fw.close();

		}
		// FileWriter fw = new FileWriter()
		// TODO Auto-generated method stub

	}

	public TextFile getCurrentFile() {
		TextFile returning = editorFiles.get(currentFocus);
		// TODO Auto-generated method stub
		return returning;
	}

	public String getCurrentFocus() {
		// TODO Auto-generated method stub
		return currentFocus;
	}

}
