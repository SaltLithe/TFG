package core;

import java.awt.Color;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import fileManagement.FILE_TYPE;
import fileManagement.FileManager;
import fileManagement.WorkSpace;
import fileManagement.WorkSpaceManager;
import javaMiniSockets.clientSide.AsynchronousClient;
import javaMiniSockets.serverSide.AsynchronousServer;
import network.ClientHandler;
import network.ResponseCreateFileMessage;
import network.ServerHandler;
import network.SyncEndedMessage;
import network.WriteMessage;
import userInterface.DeveloperMainFrame;
import userInterface.DeveloperMainFrameWrapper;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;
import userInterface.UIController;
import userInterface.runConfigDialog;
import userInterface.fileNavigation.CustomTreeNode;

public class DeveloperComponent implements PropertyChangeListener {

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
	public String expectedWorkSpaceLocation = null; 
	private ServerHandler handler; 
	
	private String focusedProject;
	private HashMap<String, String> projectFocusPairs;

	private WorkSpace workSpace;
	private HashMap<String, ClassPath> classpaths;
	public boolean isConnected;
	
	
	public void setNewName(String newname) {
		ArrayList<Object> message = new ArrayList<Object>();
		message.add(newname);
		support.notify(ObserverActions.SET_CHOSEN_NAME, null, message);
		
	}

	public void setAsClient(String serverAddress, String ownAddress, int serverPort, boolean autoConnect , String chosenName ,String imageByteData, Color chosenColor) {

		ClientHandler handler = new ClientHandler(chosenName,  imageByteData, chosenColor);
		client = new AsynchronousClient(serverAddress, ownAddress, serverPort, handler);
		if (ownAddress == null) {

			client.setAutomaticIP();

		}
		if (autoConnect) {

			try {
				DEBUG.debugmessage("SET AS CLIENT");
				support.notify(ObserverActions.DISABLE_NEW_PROJECT,null,null);
				support.notify(ObserverActions.DISABLE_JOIN_BUTTON,null , null );
				client.Connect();
				isConnected = true ; 
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		setNewName(chosenName);
	}

	public void setAsServer(String name, String ip, int maxClients, int port,  int queueSize,
			boolean autoConnect) {

		DEBUG.debugmessage("Setting as server");
		 handler = new ServerHandler(name , maxClients);
		if (queueSize == -1) {
			queueSize = defaultQueueSize;
		}

		server = new AsynchronousServer(name, handler, maxClients, port, ip, queueSize);
		if (ip == null) {
			server.setAutomaticIP();
		}

		if (autoConnect) {
			support.notify(ObserverActions.DISABLE_NEW_PROJECT,null,null);
			support.notify(ObserverActions.DISABLE_JOIN_BUTTON,null , null );
			server.Start();
			isConnected = true; 
		}
		
		setNewName(name);
		
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
		support.addPropertyChangeListener(DeveloperMainFrameWrapper.getUsersPanel());
		support.addPropertyChangeListener(compiler);
		support.addPropertyChangeListener(this);
		support.addPropertyChangeListener(DeveloperMainFrameWrapper.getInstance());

		if(workSpace != null) {
		fileManager.scanWorkSpace(workSpace);
		}
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

	public void createNewProject(String name , boolean includeHelpFolders , boolean updateEditor) {
		fileManager.newProject(name, workSpace, includeHelpFolders , updateEditor);

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
	
	public void closeAllTabs()	{

		support.notify(ObserverActions.CLOSE_ALL_TABS , null , null );
		
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
public void saveAllFull() {
	
	fileManager.saveAllFull();
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
			saveAllFull(); 
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

	public void setIcon(Color color, String imagepath , String name) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(color);
		list.add(imagepath);
		list.add(name);
		support.notify(ObserverActions.SET_SELF_ICON, null , list);
		
		
	}

	public void sendMessageToServer(Serializable message) {
	
		
		if(this.client != null) {
			try {
				this.client.send(message);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void disconnect() {

		try {
			if(client != null) {
			client.disconnect();
			client = null;
			}
			else if(server != null) {
				server.Stop();
				server = null; 
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	
	}

	public String getWorkSpaceName() {
		return workSpace.getName();
	
	}

	public void sendToClient(Serializable response , int clientID) {
		if(server != null) {
			Serializable[] messages = {response};
			int[] clientIDS = {clientID};
			try {
				server.sendMessage(clientIDS, messages);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void scanAndSend(int clientID) {
		
		LinkedList<ResponseCreateFileMessage> responses = fileManager.scanAndReturn(workSpace.getPath(), workSpace.getName()); 
		
		System.out.println("TOTAL IS : " + responses.size());
		int count =0 ; 
		
		for (ResponseCreateFileMessage message : responses) {
			
			count++;
			
			
			
			
			this.sendToClient(message, clientID);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		SyncEndedMessage ended = new SyncEndedMessage(); 
		this.sendToClient(ended, clientID);
		handler.syncComplete();
		
		
		
	}
	
	
	public void sendSyncToClient(ResponseCreateFileMessage message  , int clientID) {
		
		sendToClient(message, clientID);
		
		scanAndSend(clientID); 
	}

	public void createWorkSpace(String path) {

		WorkSpace ws = new WorkSpace();
		ws.setName(path);
		ws.setPath(expectedWorkSpaceLocation + "\\" + path);
		File wslocation = new File(ws.getPath());
		
		if (!wslocation.exists()) {
			wslocation.mkdir();
		}
		
		WorkSpaceManager wsm = WorkSpaceManager.getInstance();
		wsm.addWorkSpace(ws);
		workSpace = ws;
		
		
		
		
	}

	public void writeFolder(String path, FILE_TYPE property) {
		
		
		String writingname = path.substring(path.lastIndexOf("\\")+1 , path.length());
		String writingpath = workSpace.getPath() + path;
		String firsthalf = writingpath.replace(workSpace.getPath(), "");
		firsthalf = firsthalf.substring(1,firsthalf.length());
		firsthalf = firsthalf.substring(0,firsthalf.indexOf("\\"));
		String projectpath = workSpace.getPath() + "\\" + firsthalf;
		fileManager.writeFolder(writingpath, property, false , writingname, projectpath);
		
	}

	public void writeFile(String path, String contents, FILE_TYPE type) {
		path = workSpace.getPath() + path;
		fileManager.writeFile(path , contents , type);
		
	}

	public void  reloadWorkSpace() {
		
		support.notify(ObserverActions.CLEAR_PANEL , null , null);
		fileManager.scanWorkSpace(workSpace);

		
		
	}

	public void changeUserName() {
		// TODO  que cambie el nombre de usuario en el panel de users 
		
	}

	public void closeServer() {
		if(server != null) {
			server.close(); 
		}
	}

	public void addProfilePicture(String image, int color, String name) {
		
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(image);
		list.add(color);
		list.add(name);
		support.notify(ObserverActions.SET_CLIENT_ICON,null,list);
	}


	

}
