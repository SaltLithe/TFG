package core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
//Julio no te fijes mucho en esta clase que es un monstruo 
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import bin.javaMiniSockets.clientSide.AsynchronousClient;
import bin.javaMiniSockets.serverSide.AsynchronousServer;
import fileManagement.FILE_TYPE;
import fileManagement.FileManager;
import fileManagement.WorkSpace;
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

public class DeveloperComponent extends Observable implements PropertyChangeListener {

	private PersonalInterpreter interpreter;
	private PersonalCompiler compiler;
	private FileManager fileManager;
	public DeveloperMainFrame developerMainFrame;
	private PropertyChangeMessenger support;
	private ThreadPoolExecutor executor;
	public AsynchronousServer server;
	public AsynchronousClient client;
	private int defaultQueueSize = 100;
	public Thread runningThread; 

	private String focusedProject;
	private HashMap<String, String> projectFocusPairs;

	private WorkSpace workSpace;
	private HashMap<String, ClassPath> classpaths;
	public boolean isConnected;

	public void setAsClient(String serverAddress, String ownAddress, int serverPort, int clientPort,
			boolean autoConnect) {

		ClientHandler handler = new ClientHandler();
		client = new AsynchronousClient(serverAddress, ownAddress, serverPort, handler);
		if (ownAddress == null) {

			client.setAutomaticIP();

		}
		if (autoConnect) {

			try {
				DEBUG.debugmessage("SET AS CLIENT");
				client.Connect();
				isConnected = true ; 
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

		server = new AsynchronousServer(name, handler, maxClients, port, ip, queueSize);
		if (ip == null) {
			server.setAutomaticIP();
		}

		if (autoConnect) {
			server.Start();
			isConnected = true; 
		}
		
	}

	public DeveloperComponent(WorkSpace workSpace)

	{
		isConnected = false; 
		DEBUG.debugmessage("SE HA CREADO UNA INSTANCIA DE DEVELOPERCOMPONENT");

		focusedProject = null;
		projectFocusPairs = new HashMap<String, String>();

		classpaths = new HashMap<String, ClassPath>();
		support = PropertyChangeMessenger.getInstance();
		this.workSpace = workSpace;
		UIController controller = UIController.getInstance();
		controller.setDeveloperComponent(this);
		fileManager = new FileManager();

		try {
			SwingUtilities.invokeAndWait(DeveloperMainFrameWrapper.getSingleInstance());
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		interpreter = new PersonalInterpreter();
		compiler = new PersonalCompiler();
		executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();

		support.addPropertyChangeListener(DeveloperMainFrameWrapper.getFileExplorerToolbar());
		support.addPropertyChangeListener(DeveloperMainFrameWrapper.getTextEditorToolbar());
		support.addPropertyChangeListener(DeveloperMainFrameWrapper.getTextEditorPanel());
		support.addPropertyChangeListener(DeveloperMainFrameWrapper.getConsolePanel());
		support.addPropertyChangeListener(DeveloperMainFrameWrapper.getMenuToolbar());
		support.addPropertyChangeListener(compiler);
		support.addPropertyChangeListener(this);
		support.addPropertyChangeListener(DeveloperMainFrameWrapper.getInstance());

		fileManager.scanWorkSpace(workSpace);
	}

	private boolean stillExists(String name) {

		File f = new File(name);

		return f.exists();
	}


	private void compileAndRun(boolean global) throws IOException {

		DEBUG.debugmessage("SE HA LLAMADO A RUN EN DEVELOPERCOMPONENT");

		if (focusedProject == null || focusedProject == "" || !stillExists(focusedProject)) {
			JOptionPane.showMessageDialog(this.developerMainFrame,
					"There is no project selected or the project you are trying to run does not exist, choose a tab from a project to run.",
					"Run error", JOptionPane.ERROR_MESSAGE);
		} else {

			URLData[] files = classpaths.get(focusedProject).getClassPath();

			String focusedClassName = projectFocusPairs.get(focusedProject);
			if (focusedClassName != null && focusedClassName != "" && stillExists(focusedClassName)) {

				if (!global) {
					support.notify(ObserverActions.DISABLE_LOCAL_RUN, null, null);
				} else {
					support.notify(ObserverActions.DISABLE_GLOBAL_RUN, null, null);

				}
				String subname = focusedClassName.substring(focusedClassName.lastIndexOf("\\") + 1,
						focusedClassName.length());
				compile(files, subname);
				if (!global) {
					support.notify(ObserverActions.ENABLE_LOCAL_RUN, null, null);
				} else {
					support.notify(ObserverActions.ENABLE_GLOBAL_RUN, null, null);

				}
				support.notify(ObserverActions.DISABLE_TERMINATE, null, null);

			} else {

				runConfigDialog d = new runConfigDialog(this, files);

			}
		}

	}

	// Metodo privado para llamar al compilador
	private String compile(URLData[] files, String className) {
		DEBUG.debugmessage("SE HA LLAMADO A COMPILE EN DEVELOPERCOMPONENT");

		return compiler.run(className, files);

	}



	public void writeOnClosed (String path , String contents) {
		
		
		fileManager.writeOnClosed(path, contents);
		
	}

	public void loadClassPath(String[] classes, String project) {

		ClassPath newclasspath = new ClassPath(project, classes);
		classpaths.put(project, newclasspath);

	}

	public void editClassPath(String[] adding, String[] removing, String project) {

		classpaths.get(project).edit(adding, removing);

	}

	// Metodo que es llamado para seleccionar y abrir la carpeta donde se va a
	// trabajar con la aplicacion
	public void selectFocusedFolder() {
		DEBUG.debugmessage("SE HA LLAMADO A SELECTFOCUSEDFOLDER EN DEVELOPERCOMPONENT");

		// fileManager.openFolder()

		// fileManager.updateAllFiles();

	}

	// Metodo que gestiona la creación de una nueva clase , incluida la acualización
	// de los archivos de la aplicación
	/**
	 * 
	 * @param isMain 
	 * @param args args[0] : String Name , args[1] : String Contents
	 */

	public void createNewClassFile(String name, String path, String project, boolean isMain) {
		DEBUG.debugmessage("SE HA LLAMADO A CREATENEWCLASSFILE EN DEVELOPERCOMPONENT");

		fileManager.createClassFile(name, path, project, true,isMain);
		String adding = path+"\\"+name+".java";
		String[] input = {adding};
		classpaths.get(project).edit(input, null);

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
	public String openFile(String name, String path, String contents, String project) {
		DEBUG.debugmessage("SE HA LLAMADO A OPENFILE EN DEVELOPERCOMPONENT CON UNA LAMBDA DE MIERDA");

		return fileManager.openTextFile(name, path, contents, project);

	}

	// Metodo que gestiona el guardao de todos los archivos de la aplicacion
	// actuales
	public void saveAllFiles(String[] names, String[] contents) throws IOException {
		DEBUG.debugmessage("SE HA LLAMADO A SAVEALLFILES EN DEVELOPERCOMPONENT");

		fileManager.saveAllOpen(names, contents);
	}

	// Metodo que gestiona el guardado del archivo abierto actualmente en la
	// aplicacion
	public void saveCurrentFile(String editorContents, String path) throws IOException {
		DEBUG.debugmessage("SE HA LLAMADO A SAVECURRENTFILE EN DEVELOPERCOMPONENT");

		fileManager.saveCurrentFile(editorContents, path);

	}

	// Metodo que gestiona una unica ejecucion local del archivo abierto actualmente
	// en el editor
	

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

		if (unsavedChanges) {

			Object[] options = { "Save and close", "Close without saving", "Cancel" };
			int n = JOptionPane.showOptionDialog(this.developerMainFrame,
					"The file at : " + path + " has unsaved changes.", "Unsaved changes",
					JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
			if (n == JOptionPane.OK_OPTION) {

				try {
					this.fileManager.saveCurrentFile(contents, path);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if (n != JOptionPane.CANCEL_OPTION) {

				ArrayList<Object> list = new ArrayList<Object>();
				list.add(path);
				support.notify(ObserverActions.CLOSE_TAB, null, list);
			}

		} else {
			ArrayList<Object> list = new ArrayList<Object>();
			list.add(path);
			support.notify(ObserverActions.CLOSE_TAB, null, list);

		}

		// TODO Auto-generated method stub

	}
	
	public void deleteClassPath(String path, String project) {
		String[] input = {path}; 
		
		classpaths.get(project).edit(null, input);

	}
	

	public void deleteFile(String name, String path, boolean isFolder, String project, CustomTreeNode node) {
		fileManager.deleteFile(name, path, isFolder, project, node);
		
	}

	public void setProjectFocus(String project) {

		DEBUG.debugmessage("SETTING PROJECT FOCUS TO" + project);
		this.focusedProject = project;
	}

	public void updateFocusedPair(String name) {
		try {
			projectFocusPairs.put(this.focusedProject, name);
		} catch (Exception e) {

		}

	}

	public void terminateProcess() {
		if(runningThread != null) {
			if(runningThread.isAlive()) {
				runningThread.stop();
			}
		}
		
	}

	public void startLocalRunningThread() {
		runningThread = new Thread (()->{
			try {
				compileAndRun(false);
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
		runningThread.start();
	}
	
	

	private String findKeyFromPath(String editingpath) {
		
return null ; 
			
		}

	@SuppressWarnings("unchecked")
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		ObserverActions action = ObserverActions.valueOf(evt.getPropertyName());
		
		ArrayList<Object> results = (ArrayList<Object>) evt.getNewValue();
		switch(action) {
		case UPDATE_PANEL_CONTENTS:
			
				    DEBUG.debugmessage("UPDATING CLOSED");
					results = (ArrayList<Object>) evt.getNewValue();
					String editingpath = (String) results.get(0);
					String partialPath = workSpace.getPath().substring(0,workSpace.getPath().lastIndexOf("\\"));
					fileManager.updatePanelContents(partialPath+editingpath , results); 
					
		break;
		case DELETE_CLASS_PATH:
			String path =  (String) results.get(0);
			String project = (String) results.get(1);
			deleteClassPath(path,project);
			break;
		case DELETE_CLASS_PATH_FOCUSED:
			String pathfocused =  (String) results.get(0);
			deleteClassPath(pathfocused,focusedProject);
			break;
		case SAVE_FULL:
			fileManager.saveAllFull(); 
			break;
		default:
			break;
		}
		
	}

	public void triggerRunConfig() {
		if (focusedProject == null || focusedProject == "" || !stillExists(focusedProject)) {
			JOptionPane.showMessageDialog(this.developerMainFrame,
					"There is no project selected or the project you are trying to run does not exist, choose a tab from a project to run.",
					"Run error", JOptionPane.ERROR_MESSAGE);
		} 
		else {
		runConfigDialog d = new runConfigDialog(this, this.classpaths.get(focusedProject).getClassPath());
		}
	}

	public void createSrcFolder(String path , String name) {
		fileManager.writeFolder(path, FILE_TYPE.SRC_FOLDER, true , name ,focusedProject );
		
	}

}
