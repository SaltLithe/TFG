
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import developer.FileType;
import developer.PersonalInterpreter;
import developer.TextFile;

public class DeveloperComponent {

	private PersonalInterpreter interpreter;
	private PersonalCompiler compiler;
	private FileManager fileManager;
	private DeveloperMainFrame developerMainFrame;
	private ThreadPoolExecutor executor;
	private SessionComponent session;

	// private constructor.
	public DeveloperComponent(DeveloperMainFrame dpmf, Socket socket, ServerSocket server)

	{
		// Creo los componentes que me hacen falta para gestionar compilación y archivos
		developerMainFrame = dpmf;
		interpreter = new PersonalInterpreter();
		compiler = new PersonalCompiler();

		executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

		if (server != null) {

			session = new SessionOwnerComponent(server);
		} else {

			session = new SessionClientComponent(socket);
		}

	}

	// Metodo publico para ejecutar código que maneja solo si ejecutar clase o
	// script
	public String run() throws IOException {

		TextFile file = fileManager.getCurrentFile();
		String name = fileManager.getCurrentFocus();

		if (file.getType() == FileType.CLASS) {
			return compile(file.getContent(), name);
		} else if (file.getType() == FileType.SCRIPT) {
			return interpret(file.getContent());

		} else {
			return null;
		}

	}

	private String compile(String code, String className) {
		return compiler.run(code, className);

	}

	private String interpret(String code) {
		interpreter.run(code);
		return null;

	}

	public void selectFocusedFolder() {
		fileManager.openFolder(developerMainFrame);
		fileManager.updateAllFiles();

	}

	public void createNewClassFile(String Name, String contents) {
		fileManager.createClassFile(Name, contents, true);
		fileManager.updateAllFiles();

		// TODO Auto-generated method stub

	}

	public void getFile(String name) {
		// TODO Auto-generated method stub
		fileManager.getFile(name);
	}

	public File[] getAllFiles() {

		return fileManager.returnAllFiles();

	}

	public String openFile(String name, String contents) {
		return fileManager.openFile(name, contents);
		// TODO Auto-generated method stub

	}

	public void saveAllFiles() throws IOException {
		fileManager.saveAll();
	}

	public void saveCurrentFile() throws IOException {
		// TODO Auto-generated method stub
		fileManager.saveCurrentFile();

	}

	public String runLocal() {

		try {
			fileManager.saveCurrentFile();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Future<String> future = executor.submit(() -> run());
		try {
			return future.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return null;

	}

	public void setTextEditorToolbar(TextEditorToolbar textEditorToolbar) {
		fileManager = new FileManager(textEditorToolbar);
		// TODO Auto-generated method stub

	}

}
