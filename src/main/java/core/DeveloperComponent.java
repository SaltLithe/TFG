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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import fileManagement.FileManager;
import fileManagement.FileType;
import fileManagement.Project;
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
import userInterface.runConfigDialog;
import userInterface.fileNavigation.CustomTreeNode;

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
	
	
	private String focusedProject;
	private HashMap<String , String> projectFocusPairs;
	
	
	
	private WorkSpace workSpace; 
	private HashMap<String,ClassPath> classpaths; 
	
	
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
		
		DEBUG.debugmessage("Setting as server");
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

		focusedProject = null; 
		projectFocusPairs = new HashMap<String,String>(); 
		
		classpaths = new HashMap<String,ClassPath>();
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

	
	private boolean stillExists(String name) {
		
		File f = new File(name);
		
		
		return f.exists();
	}
	
	// Metodo publico para ejecutar código que maneja solo si ejecutar clase o
	// script
	public void run( String focusedClassName) throws IOException {

		DEBUG.debugmessage("SE HA LLAMADO A RUN EN DEVELOPERCOMPONENT");

		if(focusedProject == null ||focusedProject == "" || !stillExists(focusedProject)) {
			JOptionPane.showMessageDialog(this.developerMainFrame,
				    "There is no project selected or the project you are trying to run does not exist, choose a tab from a project to run.",
				    "Run error",
				    JOptionPane.ERROR_MESSAGE);
		}else {

			URLData[] files = classpaths.get(focusedProject).getClassPath();

	if(focusedClassName != null && focusedClassName != "" && stillExists(focusedClassName)) {
		
			String results = compile(files , focusedClassName);
			ArrayList<Object> observations = new ArrayList<Object>();
			observations.add(ObserverActions.CONSOLE_PANEL_CONTENTS);
			observations.add(results);
			notifyObservers(observations);
		}else {
			
			runConfigDialog d = new runConfigDialog(this,files);
			
		}
		}

	}

	// Metodo privado para llamar al compilador
	private String compile(URLData[] files, String className) {
		DEBUG.debugmessage("SE HA LLAMADO A COMPILE EN DEVELOPERCOMPONENT");

		return compiler.run(className,files);

		
	}

	// Metodo privado para llamar al interpete
	private String interpret(String code) {
		DEBUG.debugmessage("SE HA LLAMADO A INTERPET EN DEVELOPERCOMPONENT");

		interpreter.run(code);
		return null;

	}
	
	public void loadClassPath(String[] classes, String project) {
		
		ClassPath newclasspath = new ClassPath(project, classes);
		classpaths.put(project, newclasspath);
		
		
	}
	
	
	public void editClassPath(String[] adding , String[] removing , String project) {
		
		classpaths.get(project).edit(adding , removing);
		
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
	

	
	public void createNewClassFile(String name, String path , String project) {
		DEBUG.debugmessage("SE HA LLAMADO A CREATENEWCLASSFILE EN DEVELOPERCOMPONENT");

		fileManager.createClassFile(name, path ,project,  true);
		//fileManager.updateAllFiles();

	}
	
	
	public void createNewProject(String name) {
		fileManager.newProject(name, workSpace);
		
	}
	// Metodo para recuperar un archivo dado su nombre
	public void getFile(String name) {
		DEBUG.debugmessage("SE HA LLAMADO A GETFILE EN DEVELOPERCOMPONENT");

		fileManager.getTextFile(name);
	}

	// Metodo para recuperar todos los archivos de la aplicacion
	
	
	public File[] getAllFiles() {
		DEBUG.debugmessage("SE HA LLAMADO A GETALLFILES EN DEVELOPERCOMPONENT");

		return null;
	}

	// Metodo que gestiona abrir un archivo en la aplicacion dado su nombre , recibe
	// los contenidos actuales
	// del editor para poder guardar los cambios
	public String openFile(String name, String path , String contents , String project) {
		DEBUG.debugmessage("SE HA LLAMADO A OPENFILE EN DEVELOPERCOMPONENT CON UNA LAMBDA DE MIERDA");

		return fileManager.openTextFile(name, path , contents, project );

	}

	// Metodo que gestiona el guardao de todos los archivos de la aplicacion
	// actuales
	public void saveAllFiles(String editorContents) throws IOException {
		DEBUG.debugmessage("SE HA LLAMADO A SAVEALLFILES EN DEVELOPERCOMPONENT");

		fileManager.saveAll(editorContents);
	}

	// Metodo que gestiona el guardado del archivo abierto actualmente en la
	// aplicacion
	public void saveCurrentFile(String editorContents , String path) throws IOException {
		DEBUG.debugmessage("SE HA LLAMADO A SAVECURRENTFILE EN DEVELOPERCOMPONENT");

		fileManager.saveCurrentFile(editorContents,path);

	}

	// Metodo que gestiona una unica ejecucion local del archivo abierto actualmente
	// en el editor
	public String runLocal(String editorContents) {

		DEBUG.debugmessage("SE HA LLAMADO A RUNLOCAL EN DEVELOPERCOMPONENT CON CONTENIDOS " + editorContents);

		try {
			fileManager.saveCurrentFile(editorContents , null);
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
	//	Thread t = new Thread(() -> executor.submit(() -> run()));
		//t.start();

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

	public void closeTab(String path, boolean unsavedChanges, String contents) {
		
		
		if(unsavedChanges) {
			

			
			Object[] options = {"Save and close",
                    "Close without saving" , "Cancel"};
int n = JOptionPane.showOptionDialog(this.developerMainFrame,
 "The file at : " + path + " has unsaved changes.",
    "Unsaved changes",
    JOptionPane.YES_NO_CANCEL_OPTION,
    JOptionPane.QUESTION_MESSAGE,
    null,
    options,
    options[1]);
if(n == JOptionPane.OK_OPTION) {
	
	try {
		this.fileManager.saveCurrentFile(contents,path);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} 

	
}if (n != JOptionPane.CANCEL_OPTION ) {
	
	ArrayList<Object> list = new ArrayList<Object>();
	list.add(path);
	support.notify(ObserverActions.CLOSE_TAB, null , list);
}
			
		}else {
			ArrayList<Object> list = new ArrayList<Object>();
			list.add(path);
			support.notify(ObserverActions.CLOSE_TAB, null , list);
			
		}
	
		// TODO Auto-generated method stub
		
	}

	public Object deleteFile(String name, String path , boolean isFolder , String project , CustomTreeNode node) {
		fileManager.deleteFile(name, path, isFolder, project,node);
		return null;
	}

	public void setProjectFocus(String project) {

		DEBUG.debugmessage("SETTING PROJECT FOCUS TO" + project);
		this.focusedProject = project; 
	}

	public void updateFocusedPair(String name) {
		try {
		projectFocusPairs.put(this.focusedProject, name);
		}
		catch(Exception e) {
			
		}
		
	}

	


}
