package TFG;

//Julio no te fijes mucho en esta clase que es un monstruo 
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import miniSockets.AsynchronousClient;
import miniSockets.AsynchronousServer;

public class DeveloperComponent {

	private PersonalInterpreter interpreter;
	private PersonalCompiler compiler;
	private FileManager fileManager;
	private DeveloperMainFrame developerMainFrame;
	private ThreadPoolExecutor executor;
	@SuppressWarnings("unused")
	private Socket socket;
	private AsynchronousServer server;

	public DeveloperComponent(DeveloperMainFrame dpmf, AsynchronousClient socket, AsynchronousServer server2)

	{
		DEBUG.debugmessage("SE HA CREADO UNA INSTANCIA DE DEVELOPERCOMPONENT");

		developerMainFrame = dpmf;
		interpreter = new PersonalInterpreter();
		compiler = new PersonalCompiler();

		if (server2 != null) {
			server = server2;
		}
		// Aqui esta el famoso threadpool cache , un peligro
		executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

	}

	// Metodo publico para ejecutar código que maneja solo si ejecutar clase o
	// script
	public String run() throws IOException {

		DEBUG.debugmessage("SE HA LLAMADO A RUN EN DEVELOPERCOMPONENT");

		TextFile file = fileManager.getCurrentTextFile();
		String name = fileManager.getCurrentFocus();

		if (file.getType() == FileType.CLASS) {
			return compile(file.getContent(), name);
		} else if (file.getType() == FileType.SCRIPT) {
			return interpret(file.getContent());

		} else {
			return null;
		}

	}

	// Metodo privado para llamar al compilador
	private String compile(String code, String className) {
		DEBUG.debugmessage("SE HA LLAMADO A COMPILE EN DEVELOPERCOMPONENT");

		return compiler.run(code, className, fileManager.getCurrentFolder());

	}

	// Metodo privado para llamar al interpete
	private String interpret(String code) {
		DEBUG.debugmessage("SE HA LLAMADO A INTERPET EN DEVELOPERCOMPONENT");

		interpreter.run(code);
		return null;

	}

	// Metodo que es llamado para seleccionar y abrir la carpeta donde se va a
	// trabajar con la aplicacion
	public void selectFocusedFolder() {
		DEBUG.debugmessage("SE HA LLAMADO A SELECTFOCUSEDFOLDER EN DEVELOPERCOMPONENT");
		fileManager.openFolder(developerMainFrame);
		fileManager.updateAllFiles();

	}

	// Metodo que gestiona la creación de una nueva clase , incluida la acualización
	// de los archivos de la aplicación
	public void createNewClassFile(String Name, String contents) {
		DEBUG.debugmessage("SE HA LLAMADO A CREATENEWCLASSFILE EN DEVELOPERCOMPONENT");

		fileManager.createClassFile(Name, contents, true);
		fileManager.updateAllFiles();

	}

	// Metodo para recuperar un archivo dado su nombre
	public void getFile(String name) {
		DEBUG.debugmessage("SE HA LLAMADO A GETFILE EN DEVELOPERCOMPONENT");

		fileManager.getTextFile(name);
	}

	// Metodo para recuperar todos los archivos de la aplicacion
	public File[] getAllFiles() {
		DEBUG.debugmessage("SE HA LLAMADO A GETALLFILES EN DEVELOPERCOMPONENT");

		return fileManager.returnAllFiles();

	}

	// Metodo que gestiona abrir un archivo en la aplicacion dado su nombre , recibe
	// los contenidos actuales
	// del editor para poder guardar los cambios
	public String openFile(String name, String contents) {
		DEBUG.debugmessage("SE HA LLAMADO A OPENFILE EN DEVELOPERCOMPONENT");

		return fileManager.openTextFile(name, contents);

	}

	// Metodo que gestiona el guardao de todos los archivos de la aplicacion
	// actuales
	public void saveAllFiles() throws IOException {
		DEBUG.debugmessage("SE HA LLAMADO A SAVEALLFILES EN DEVELOPERCOMPONENT");

		fileManager.saveAll();
	}

	// Metodo que gestiona el guardado del archivo abierto actualmente en la
	// aplicacion
	public void saveCurrentFile() throws IOException {
		DEBUG.debugmessage("SE HA LLAMADO A SAVECURRENTFILE EN DEVELOPERCOMPONENT");

		fileManager.saveCurrentFile();

	}

	// Metodo que gestiona una unica ejecucion local del archivo abierto actualmente
	// en el editor
	public String runLocal() {

		DEBUG.debugmessage("SE HA LLAMADO A RUNLOCAL EN DEVELOPERCOMPONENT");

		try {
			fileManager.saveCurrentFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		/*
		 * Usamos un future para que devuelva el resultado del codigo de una forma mas
		 * limpia , ademas de que la ejecucion se hace en otro hilo
		 */
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

	// Este metodo esta aquí porque file manager necesita tener una referencia al
	// componente
	// TextEditorToolbar , y textEditorToolbar necesita una referencia a
	// DeveloperComponent
	// La solución más facil a este cruce era inicializar filemanager despues
	public void setTextEditorToolbar(TextEditorToolbar textEditorToolbar) {
		fileManager = new FileManager(textEditorToolbar);

	}

	public void sendMessageToEveryone(WriteMessage message) {
		if (server != null) {
			Serializable[] messages = { message };
			try {
				server.broadcastAllMessage(messages);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		// TODO Auto-generated method stub

	}

}
