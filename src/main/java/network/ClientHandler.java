package network;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import com.google.common.util.concurrent.MoreExecutors;

import core.DEBUG;
import fileManagement.FILE_PROPERTIES;
import fileManagement.FILE_TYPE;
import javaMiniSockets.clientSide.ClientMessageHandler;
import javaMiniSockets.clientSide.ServerInfo;
import userInterface.DeveloperMainFrameWrapper;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;
import userInterface.UIController;
import userInterface.networkManagement.AcceptGlobalDialog;
import userInterface.networkManagement.acceptSyncDialog;
import userInterface.networkManagement.awaitSyncDialog;

/**
 * Class implementing the Client Message Handler it receives the calls from the
 * client when certain events happen Events like receiving messages or
 * connection notifications
 * 
 * @author Carmen Gómez Moreno
 *
 */
public class ClientHandler implements ClientMessageHandler {

	public awaitSyncDialog sync;
	public String chosenName;

	PropertyChangeMessenger support;
	ArrayBlockingQueue<WriteMessage> processBuffer = new ArrayBlockingQueue<WriteMessage>(100);
	ArrayBlockingQueue<HighLightMessage> highlightBuffer = new ArrayBlockingQueue<HighLightMessage>(100);
	ArrayBlockingQueue<WriteToConsoleMessage> consoleBuffer = new ArrayBlockingQueue<WriteToConsoleMessage>(100);
	boolean unFlag = false;
	String chosenImage;
	Color chosenColor;
	LinkedList<ImageDataMessage> otherClients;
	private HashMap<String, Integer> colorData;
	private HashMap<String, ImageDataMessage> images;
	public boolean blocked = false; 
	private Semaphore messageSem; 
	
	
	
	ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
	ExecutorService executorService = MoreExecutors.getExitingExecutorService(executor, 100, TimeUnit.MILLISECONDS);
    

	/**
	 * 
	 * @param chosenName    : Name of this user as client
	 * @param imageByteData : Data that describes this users profile icon's image
	 * @param chosenColor   : Object representing the color of this users profile
	 *                      icon's color
	 */
	public ClientHandler(String chosenName, String imageByteData, Color chosenColor) {

		
		messageSem = new Semaphore(1);
		images = new HashMap<>();
		colorData = new HashMap<>();
		

		otherClients = new LinkedList<>();
		this.chosenImage = imageByteData;
		this.chosenColor = chosenColor;
		this.chosenName = chosenName;
		support = PropertyChangeMessenger.getInstance();
		
		executorService.submit(() -> processMessage());
		executorService.submit(() -> processHighLights());
	
		executorService.submit(()->this.processConsole());

	}

	/**
	 * Method a thread accesses to process the write petitions from other users
	 */
	public void processMessage() {

		boolean loopAround = true;
		while (loopAround) {
			WriteMessage incoming = null;
			try {
				
				
				incoming = processBuffer.take();
			} catch (InterruptedException e) {
				loopAround = false;
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}

			Object[] message = { incoming.path, incoming };
			support.notify(ObserverActions.UPDATE_PANEL_CONTENTS, message);

		}

	}

	/**
	 * Method a thread accesses to process the highlight information other users
	 * send
	 */
	public void processHighLights() {

		boolean loopAround = true; 
		while (loopAround) {
			HighLightMessage incoming = null;
			try {
				incoming = highlightBuffer.take();
			} catch (InterruptedException e) {
				loopAround = false;
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
			DEBUG.debugmessage("Message name is " + incoming.name);
			DEBUG.debugmessage("Own name is " + chosenName);

			if (!incoming.name.equals(chosenName)) {

				DEBUG.debugmessage("PAINTING A HIGHLIGHT");
				Object[] message = { incoming.linestart, incoming.lineend, colorData.get(incoming.name), incoming.name,
						incoming.keypath };
				support.notify(ObserverActions.UPDATE_HIGHLIGHT, message);

			}
		}
	}
	
	public void processConsole() {
		boolean loopAround = true ;
		while(loopAround) {
		WriteToConsoleMessage incoming = null;
		
		try {
			incoming = consoleBuffer.take();
		}catch(InterruptedException e) {
			loopAround = false;
			Thread.currentThread();
			e.printStackTrace();
		}
		
		Object[] message = {incoming.line};
		support.notify(ObserverActions.CONSOLE_PANEL_CONTENTS,message);
		}
	}

	/**
	 * Implementation of the method that sends messages as they are received Behaves
	 * differently depending on the type of message received
	 */
	@Override
	public void onMessageSent(Serializable message, ServerInfo serverInfo) {
		
	
		try {

			messageSem.acquire(); 
		// Get the class name
		String messageclass = message.getClass().toString();

		// Message is a write order
		switch (messageclass) {
		case "class network.WriteMessage":
			WriteMessage incoming = (WriteMessage) message;

			// Do not process this message if it is a broadcast from the server that is
			// echoing
			// your previous message
			try {
				if (!incoming.ownerName.equals(chosenName)) {
					processBuffer.put(incoming);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			break;

		// Message is indicating the end of a sync operation
		case "class network.SyncEndedMessage":

			if (sync != null) {

				sync.dispose();
			}
			// If the flag indicating a change of name is up, alert the user
			if (unFlag) {
				JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
						"Your chosen name is already in use by somebody else in this session, your username has been changed to : "
								+ chosenName);
			
			}

			// Set new name , reload workspace after sync and send the server image data for
			// this client
			support.notify(ObserverActions.ENABLE_TEXT_EDITOR,null);
			support.notify(ObserverActions.ENABLE_GLOBAL_RUN, null);
			UIController.developerComponent.setNewName(chosenName);
			UIController.developerComponent.reloadWorkSpace();
			ImageDataMessage imageMessage = new ImageDataMessage(chosenImage, chosenColor.getRGB(), chosenName, false);
			UIController.developerComponent.sendMessageToServer(imageMessage);

			break;

		// Message is indicating the client to create a file
		case "class network.ResponseCreateFileMessage":
			ResponseCreateFileMessage responseCreateFile = (ResponseCreateFileMessage) message;

			// Behaviour changes if this message is indicating that the client should create
			// a workspace
			if (responseCreateFile.type.equals(FILE_PROPERTIES.workspaceProperty)) {

				ResponseCreateFileMessage response = (ResponseCreateFileMessage) message;
				// Here the client checks if the server has sent a new name it should change
				// it's name to
				if (response.newname != null) {
					chosenName = response.newname;
					
					unFlag = true;

				}
				UIController.developerComponent.createWorkSpace(responseCreateFile.path);
				support.notify(ObserverActions.DISABLE_TEXT_EDITOR,null);
				sync = new awaitSyncDialog();
			}

			// If this message does not have to create a workspace , it checks now for the
			// rest of the file types
			// the user can create

			else if (responseCreateFile.type.equals(FILE_PROPERTIES.projectProperty)) {
				String name = responseCreateFile.path.substring(responseCreateFile.path.lastIndexOf("\\"),
						responseCreateFile.path.length());
				UIController.developerComponent.createNewProject(name, false, false);
			}

			else if (responseCreateFile.type.equals(FILE_PROPERTIES.srcProperty)) {
				UIController.developerComponent.writeFolder(responseCreateFile.path, FILE_TYPE.SRC_FOLDER);
			} else if (responseCreateFile.type.equals(FILE_PROPERTIES.binProperty)) {
				UIController.developerComponent.writeFolder(responseCreateFile.path, FILE_TYPE.BIN_FOLDER);

			}

			else if (responseCreateFile.type.equals(FILE_TYPE.JAVA_FILE.toString())) {
				UIController.developerComponent.writeFile(responseCreateFile.path, responseCreateFile.contents,
						FILE_TYPE.JAVA_FILE);

			} else if (responseCreateFile.type.equals(FILE_TYPE.ANY_FILE.toString())) {
				UIController.developerComponent.writeFile(responseCreateFile.path, responseCreateFile.contents,
						FILE_TYPE.ANY_FILE);

			}

			break;
		// The message contains image data from other users
		case "class network.ImageDataMessage":
			ImageDataMessage imageData = (ImageDataMessage) message;
			DEBUG.debugmessage("LISTEN HERE YOU LITTLE SHIT NAME IS : " + imageData.name);

			if (imageData.isServer) {
				UIController.runOnThread(() -> UIController.developerComponent.addProfilePicture(imageData.image,
						imageData.color, imageData.name, imageData.isServer, -1));
				if(!images.containsKey(imageData.name)) {
					colorData.put(imageData.name, imageData.color);

				}

			} else {
				if (!images.containsKey(imageData.name)) {
					images.put(imageData.name, imageData);
					colorData.put(imageData.name, imageData.color);
					DEBUG.debugmessage("GOT AN IMAGE FROM : " + imageData.name);
					UIController.developerComponent.addProfilePicture(imageData.image, imageData.color, imageData.name,
							imageData.isServer, imageData.clientID);
				}

			}
			break;
		/**
		 * Message indicates that another user acting as client has disconnected
		 */
		case "class network.ClientDisconnectedMessage":
			ClientDisconnectedMessage disconnected = (ClientDisconnectedMessage) message;
			UIController.developerComponent.removeProfilePicture(disconnected.clientID);

			break;

		/**
		 * Message contains orders to paint a highlight
		 */
		case "class network.HighLightMessage":

			HighLightMessage highlightData = (HighLightMessage) message;
			try {

				highlightBuffer.put(highlightData);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			break;
		case "class network.GlobalRunRequestMessage":
			
			GlobalRunRequestMessage requestMessage = (GlobalRunRequestMessage) message;
			if(!(requestMessage.name.equals(chosenName)) && !blocked) {
			
			disableAll();
			new AcceptGlobalDialog(requestMessage.name, this);
			
			}
			break;
		case "class network.WriteToConsoleMessage":
			DEBUG.debugmessage("GOT WRITE TO CONSOLE");
			WriteToConsoleMessage consoleMessage = (WriteToConsoleMessage) message;
			try {
				consoleBuffer.put(consoleMessage);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			break;
		
		case "class network.GlobalRunRequestResponse":
			GlobalRunRequestResponse runResponse = (GlobalRunRequestResponse)message;
			if(!runResponse.ok) {
				
				
				if (runResponse.noProject) {
					
					JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
						    runResponse.name + " does not have an available project to run from.",
						    "Run was canceled",
						    JOptionPane.ERROR_MESSAGE);
					
				}else {
				JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
					    runResponse.name + " did not agree to a global Run.",
					    "Run was canceled",
					    JOptionPane.ERROR_MESSAGE);
				}
			}
		
			activateAll(); 
			
			
			break;
		case "class network.GlobalRunDone":
			
			activateAll();

			break;

		default:
			break;
		}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			messageSem.release(); 
		}
	}

	/**
	 * Implements method that is called when the server has connected back to a
	 * client and a bidirectional connection has been established
	 */
	@Override
	public void onServerConnect(ServerInfo server) {

		UIController.developerComponent.saveAllFull();
		UIController.developerComponent.closeAllTabs();

		JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
				"Success! You have connected to a session.");

		support.notify(ObserverActions.DISABLE_LOCAL_RUN, null);
		new acceptSyncDialog(this, chosenName);

	}

	@Override
	public void onConnect() {
		//We dont need to implement this method this time
	}

	/**
	 * Implements method that is called when the server has disconnected from the
	 * client and will no longer be able to send messages to it
	 */
	@Override
	public void onServerDisconnect(ServerInfo server) {

		JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
				"You have been disconnected! The server may have failed or kicked you out of the session.",
				"Disconnected warning", JOptionPane.WARNING_MESSAGE);
		
		UIController.developerComponent.disconnect();
		

	}

	/**
	 * Implements method that is called when the client has disconnected from the
	 * server and will no longer be able to send messages to it
	 */
	@Override
	public void onDisconnect() {
		JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
				"You have disconnected from the current session.", "Disconnected warning", JOptionPane.WARNING_MESSAGE);
		UIController.developerComponent.disconnect(); 
		support.notify(ObserverActions.ENABLE_LOCAL_RUN, null);
		support.notify(ObserverActions.DISABLE_GLOBAL_RUN, null);
		support.notify(ObserverActions.CLEAR_ALL_ICON,null);

		
		

	}

	/**
	 * IMplements method that is called when the client attempts to connect to the
	 * server but fails to do so
	 */
	@Override
	public void onConnectFail() {
		JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
				"Unable to connect to server, the server you are trying to connect is unreachable.", "Connection error",
				JOptionPane.ERROR_MESSAGE);
		UIController.developerComponent.disconnect(); 
	}

	/**
	 * Support method that opens a blocking dialog when a sync operation is in
	 * progress
	 */
	public void startAwaitSync() {
		sync = new awaitSyncDialog();
	}



	/**
	 * Called in order to send responses to the server when asked permission to run the process globally
	 * @param decision
	 */
	public void decideRun(boolean decision) {
		if(!decision) {
			activateAll();
		}
		GlobalRunRequestResponse response = new GlobalRunRequestResponse();
		response.name = chosenName;
		response.ok = decision;
		UIController.developerComponent.sendMessageToServer(response);
		

	}
	/**
	 * Turns off the threads that process messages 
	 */
	public void shutdownProcessors() {

		executorService.shutdown();
		
	}
	
	/**
	 * Returns functionality to edition in this program
	 */
	private void activateAll() {
		blocked = false; 
		support.notify(ObserverActions.ENABLE_TEXT_EDITOR,null);
		support.notify(ObserverActions.ENABLE_GLOBAL_RUN,null);
		support.notify(ObserverActions.ENABLE_SAVE_BUTTONS,null);
	}
	
	/**
	 * Disables functionality to edition in this program
	 */
	private void disableAll() {
		support.notify(ObserverActions.DISABLE_TEXT_EDITOR,null);
		support.notify(ObserverActions.DISABLE_GLOBAL_RUN,null);
		support.notify(ObserverActions.DISABLE_SAVE_BUTTONS,null);
	}


}
