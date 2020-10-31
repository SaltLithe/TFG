package core;

//Julio no te fijes mucho en esta clase que es un monstruo 
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.SwingUtilities;

import fileManagement.FileManager;
import fileManagement.FileType;
import fileManagement.TextFile;
import fileManagement.WorkSpace;
import javaMiniSockets.clientSide.AsynchronousClient;
import javaMiniSockets.serverSide.AsynchronousServer;
import network.ClientHandler;
import network.ServerHandler;
import network.WriteMessage;
import userInterface.DeveloperMainFrame;
import userInterface.DeveloperMainFrameWrapper;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;
import userInterface.UIController;

public class DeveloperComponent extends Observable {

	private PersonalInterpreter interpreter;
	private PersonalCompiler compiler;
	private FileManager fileManager;
	public DeveloperMainFrame developerMainFrame;
	private PropertyChangeMessenger support;
	private ThreadPoolExecutor executor;
	private AsynchronousServer server;
	private AsynchronousClient client;
	private int defaultQueueSize = 100;
	
	private WorkSpace workSpace; 

	public void setAsClient(String serverAddress, String ownAddress, int serverPort, int clientPort,
			boolean autoConnect) {

		ClientHandler handler = new ClientHandler();
		client = new AsynchronousClient(serverAddress, ownAddress, serverPort, clientPort, handler);
		if (ownAddress == null) {

			client.setAutomaticIP();

		}
		if (autoConnect) {

			try {
				DEBUG.debugmessage("SET AS CLIENT");
				client.Connect();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setAsServer(String name, String ip, int maxClients, int port, int clientPort, int queueSize,
			boolean autoConnect) {
		ServerHandler handler = new ServerHandler();
		if (queueSize == -1) {
			queueSize = defaultQueueSize;
		}

		server = new AsynchronousServer(name, handler, maxClients, port, clientPort, ip, queueSize);
		if (ip == null) {
			server.setAutomaticIP();
		}

		if (autoConnect) {
			server.Start();
		}
	}

	public DeveloperComponent(WorkSpace workSpace)

	{
		DEBUG.debugmessage("SE HA CREADO UNA INSTANCIA DE DEVELOPERCOMPONENT");

		support = PropertyChangeMessenger.getInstance();
		this.workSpace = workSpace; 
		UIController controller = UIController.getInstance();
		controller.setDeveloperComponent(this);
		fileManager = new FileManager();

		try {
			SwingUtilities.invokeAndWait(DeveloperMainFrameWrapper.getSingleInstance());
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		interpreter = new PersonalInterpreter();
		compiler = new PersonalCompiler();
		// Aqui esta el famoso threadpool cache , un peligro
		executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

		support.addPropertyChangeListener(DeveloperMainFrameWrapper.getFileExplorerToolbar());
		support.addPropertyChangeListener(DeveloperMainFrameWrapper.getTextEditorToolbar());
		support.addPropertyChangeListener(DeveloperMainFrameWrapper.getTextEditorPanel());
		support.addPropertyChangeListener(DeveloperMainFrameWrapper.getConsolePanel());
		
		fileManager.scanWorkSpace(workSpace);	}

	// Metodo publico para ejecutar código que maneja solo si ejecutar clase o
	// script
	public String run() throws IOException {

		DEBUG.debugmessage("SE HA LLAMADO A RUN EN DEVELOPERCOMPONENT");

		TextFile file = fileManager.getCurrentTextFile();
		String name = fileManager.getCurrentFocus();

		if (file.getType() == FileType.CLASS) {
			String results = compile(file.getContent(), name);
			ArrayList<Object> observations = new ArrayList<Object>();
			observations.add(ObserverActions.CONSOLE_PANEL_CONTENTS);
			observations.add(results);
			notifyObservers(observations);
			return results;
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
		
		//fileManager.openFolder()
		
		
		  
		
	//	fileManager.updateAllFiles();

	}

	// Metodo que gestiona la creación de una nueva clase , incluida la acualización
	// de los archivos de la aplicación
	/**
	 * 
	 * @param args args[0] : String Name , args[1] : String Contents
	 */
	public void createNewClassFile(String name, String contents, String path) {
		DEBUG.debugmessage("SE HA LLAMADO A CREATENEWCLASSFILE EN DEVELOPERCOMPONENT");

		fileManager.createClassFile(name, path , contents, true);
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
		DEBUG.debugmessage("SE HA LLAMADO A OPENFILE EN DEVELOPERCOMPONENT CON UNA LAMBDA DE MIERDA");

		return fileManager.openTextFile(name, contents);

	}

	// Metodo que gestiona el guardao de todos los archivos de la aplicacion
	// actuales
	public void saveAllFiles(String editorContents) throws IOException {
		DEBUG.debugmessage("SE HA LLAMADO A SAVEALLFILES EN DEVELOPERCOMPONENT");

		fileManager.saveAll(editorContents);
	}

	// Metodo que gestiona el guardado del archivo abierto actualmente en la
	// aplicacion
	public void saveCurrentFile(String editorContents) throws IOException {
		DEBUG.debugmessage("SE HA LLAMADO A SAVECURRENTFILE EN DEVELOPERCOMPONENT");

		fileManager.saveCurrentFile(editorContents);

	}

	// Metodo que gestiona una unica ejecucion local del archivo abierto actualmente
	// en el editor
	public String runLocal(String editorContents) {

		DEBUG.debugmessage("SE HA LLAMADO A RUNLOCAL EN DEVELOPERCOMPONENT CON CONTENIDOS " + editorContents);

		try {
			fileManager.saveCurrentFile(editorContents);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		/*
		 * Usamos un future para que devuelva el resultado del codigo de una forma mas
		 * limpia , ademas de que la ejecucion se hace en otro hilo
		 * 
		 * Future<String> future = executor.submit(() -> run()); try { return
		 * future.get(); } catch (InterruptedException e) { e.printStackTrace(); } catch
		 * (ExecutionException e) { e.printStackTrace(); }
		 */
		Thread t = new Thread(() -> executor.submit(() -> run()));
		t.start();

		return null;

	}

	// Este metodo esta aquí porque file manager necesita tener una referencia al
	// componente
	// TextEditorToolbar , y textEditorToolbar necesita una referencia a
	// DeveloperComponent
	// La solución más facil a este cruce era inicializar filemanager despues

	public void sendMessageToEveryone(WriteMessage message) {
		DEBUG.debugmessage("SENDING MESSAGE TO CLIENTS");
		if (server != null) {
			Serializable[] messages = { message };
			try {
				server.broadcastAllMessage(messages);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (client != null) {
			DEBUG.debugmessage("SENDING MESSAGES TO THE SERVER");
			try {
				client.send(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		// TODO Auto-generated method stub

	}

	public void reactivateRunningProccess(String retrieved) {

		compiler.reactivateRunningProccess(retrieved);
		// TODO Auto-generated method stub

	}

	public void closeTab(String path) {
		// TODO Auto-generated method stub
		
	}

}
