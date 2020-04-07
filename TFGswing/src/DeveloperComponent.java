
//Julio no te fijes mucho en esta clase que es un monstruo 
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
	@SuppressWarnings("unused")
	private Socket socket;
	private ServerSocket server;

	public DeveloperComponent(DeveloperMainFrame dpmf, Socket socket, ServerSocket server)

	{
		this.socket = socket;
		this.server = server;

		// Creo los componentes que me hacen falta para gestionar compilación y archivos
		developerMainFrame = dpmf;
		interpreter = new PersonalInterpreter();
		compiler = new PersonalCompiler();

		// Aqui esta el famoso threadpool cache , un peligro
		executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

	}

	public void runServer() {
		if (server != null) {

			try {
				server.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {// Esto de momento no tocarlo porque es para hacer gestiones posteriores y no se
				// como
			// las voy a hacer

			// session = new SessionClientComponent(socket);
		}
	}

	private Runnable runServer(ServerSocket serverSocket) {

		// boolean listening = true;

		try {
			new MultiServerThread(serverSocket.accept()).start();
		} catch (IOException e) {
			e.printStackTrace();

		}

		return null;
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
		return compiler.run(code, className, fileManager.getCurrentFolder());

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

	}

	public void getFile(String name) {
		fileManager.getFile(name);
	}

	public File[] getAllFiles() {

		return fileManager.returnAllFiles();

	}

	public String openFile(String name, String contents) {
		return fileManager.openFile(name, contents);

	}

	public void saveAllFiles() throws IOException {
		fileManager.saveAll();
	}

	public void saveCurrentFile() throws IOException {
		fileManager.saveCurrentFile();

	}

	public String runLocal() {

		try {
			fileManager.saveCurrentFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Future<String> future = executor.submit(() -> run());
		try {
			return future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		return null;

	}

	public void setTextEditorToolbar(TextEditorToolbar textEditorToolbar) {
		fileManager = new FileManager(textEditorToolbar);

	}

}
