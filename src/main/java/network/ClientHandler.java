package network; 

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JOptionPane;

import core.DEBUG;
import core.DeveloperComponent;
import fileManagement.FILE_PROPERTIES;
import fileManagement.FILE_TYPE;
import javaMiniSockets.clientSide.ClientMessageHandler;
import javaMiniSockets.clientSide.ServerInfo;
import userInterface.DeveloperMainFrameWrapper;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;
import userInterface.UIController;
import userInterface.networkManagement.acceptSyncDialog;
import userInterface.networkManagement.awaitSyncDialog;

public class ClientHandler implements ClientMessageHandler {

	PropertyChangeMessenger support;
	ArrayBlockingQueue<WriteMessage> processBuffer = new ArrayBlockingQueue<WriteMessage>(100);
	ArrayBlockingQueue<HighLightMessage> highlightBuffer = new ArrayBlockingQueue<HighLightMessage>(100);
	
	private HashMap<String , Integer> colorData;


	UIController controller;
	DeveloperComponent component;
	public awaitSyncDialog sync;
	public String chosenName;
	boolean unFlag = false; 
	String chosenImage;
	Color chosenColor;
	LinkedList<ImageDataMessage> otherClients;
	
	
	public void processMessage() {
		

		ArrayList<Object> messages = new ArrayList<Object>(); 

		while(true) {
		WriteMessage incoming = null;
		try {
			incoming = processBuffer.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		messages.add(incoming.path);
		messages.add(incoming);
		support.notify(ObserverActions.UPDATE_PANEL_CONTENTS, null, messages);
		messages.clear(); 

		}
		
	}
	
	public void processHighLights() {

		ArrayList<Object> messages = new ArrayList<Object>();

		while (true) {
			HighLightMessage incoming = null;
			try {
				incoming = highlightBuffer.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(incoming.name != chosenName) {

				messages.add(incoming.linestart);
				messages.add(incoming.lineend);
				messages.add(colorData.get(incoming.name));
				messages.add(incoming.name);
				messages.add(incoming.keypath);
			support.notify(ObserverActions.UPDATE_HIGHLIGHT, null, messages);
			messages.clear();

			}
		}
	}
	public ClientHandler(String chosenName ,  String imageByteData , Color chosenColor) {

		
		colorData = new HashMap<String, Integer>(); 

		otherClients = new LinkedList<ImageDataMessage>(); 
		this.chosenImage = imageByteData;
		this.chosenColor = chosenColor;
		this.chosenName = chosenName;
		controller = UIController.getInstance();
		component = controller.getDeveloperComponent();
		support = PropertyChangeMessenger.getInstance();
		new Thread(()-> processMessage()).start();
		new Thread(()-> processHighLights()).start();


	}

	@Override
	public void onMessageSent(Serializable message, ServerInfo serverInfo) {
		// TODO Auto-generated method stub

	

		DEBUG.debugmessage("HA LLEGADO UN MENSAJE DEL SERVER");

		String messageclass = message.getClass().toString();
		
		switch (messageclass) {
		case "class network.WriteMessage" : 
		WriteMessage incoming = (WriteMessage) message;


		try {			
			if (!incoming.ownerName.equals(chosenName)) {
			processBuffer.put(incoming);
			}
		} catch (Exception e) {

			e.printStackTrace();
		
		} 
		
		break;
		
		case "class network.SyncEndedMessage":

			if(sync!= null) {

				sync.dispose();
			}
			
			if(unFlag) {
				JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
					    "Your chosen name is already in use by somebody else in this session, your username has been changed to : " + chosenName);
			}
			
			component.setNewName(chosenName);
			controller.run(()-> component.reloadWorkSpace());
			ImageDataMessage imageMessage = new ImageDataMessage(chosenImage , chosenColor.getRGB() , chosenName , false );
			
			
			controller.run(()-> component.sendMessageToServer(imageMessage));
			
			break;
	
		case "class network.ResponseCreateFileMessage":
			ResponseCreateFileMessage incoming1 = (ResponseCreateFileMessage) message;
			

			if(incoming1.type.equals(FILE_PROPERTIES.workspaceProperty.toString())) {
				
				 ResponseCreateFileMessage response = (ResponseCreateFileMessage) message;
				 if (response.newname != null) {
					 chosenName = response.newname;
					 component.changeUserName(); 
					 unFlag = true; 
					 
				 }
				 component.createWorkSpace(incoming1.path);
				 sync = new awaitSyncDialog(); 
			}
			
			else if(incoming1.type.equals(FILE_PROPERTIES.projectProperty.toString())) {
				String name = incoming1.path.substring(incoming1.path.lastIndexOf("\\"),incoming1.path.length());
				component.createNewProject(name, false , false);
			}
			
			else if (incoming1.type.equals(FILE_PROPERTIES.srcProperty.toString())) {
				component.writeFolder(incoming1.path,FILE_TYPE.SRC_FOLDER);
			}
			else if (incoming1.type.equals(FILE_PROPERTIES.binProperty.toString())) {
				component.writeFolder(incoming1.path,FILE_TYPE.BIN_FOLDER);

			}
			
			else if (incoming1.type.equals(FILE_TYPE.JAVA_FILE.toString())){
				component.writeFile(incoming1.path , incoming1.contents , FILE_TYPE.JAVA_FILE);
				
			}
			else if (incoming1.type.equals(FILE_TYPE.ANY_FILE.toString())){
				component.writeFile(incoming1.path , incoming1.contents , FILE_TYPE.ANY_FILE);


			}
			
			
			
			
			break;
		case "class network.ImageDataMessage" :
			ImageDataMessage imageData = (ImageDataMessage) message;
			if(!(imageData.name.equals(chosenName))) {
				colorData.put(imageData.name , imageData.color);

			
				if(imageData.isServer) {
			controller.runOnThread(()-> component.addProfilePicture (imageData.image , imageData.color , imageData.name , imageData.isServer,-1));
			
				}else {
					controller.run(()-> component.addProfilePicture(imageData.image,imageData.color, imageData.name, imageData.isServer,imageData.clientID));
				}
				

			}
			break;
		case "class network.ClientDisconnectedMessage":
				ClientDisconnectedMessage disconnected = (ClientDisconnectedMessage) message;
				controller.run(()-> component.removeProfilePicture(disconnected.clientID));
				
			break;
			
		case "class network.HighLightMessage" :

			DEBUG.clientmessage("Client got a highlight");
			HighLightMessage highlightData = (HighLightMessage) message;
			try {
				highlightBuffer.put(highlightData);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			break;
		default:
			break;
		}

	}

	@Override
	public void onServerConnect(ServerInfo server) {

		DEBUG.debugmessage("HANDSHAKE");
		controller.run(()-> component.saveAllFull());
		controller.run(()-> component.closeAllTabs());

		
		JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
			    "Success! You have connected to a session.");
		
		
		new acceptSyncDialog(this, chosenName);
		
		

	}

	@Override
	public void onConnect() {
		DEBUG.debugmessage("CONNECTED TO SERVER");

	}

	@Override
	public void onServerDisconnect(ServerInfo server) {
		

		JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
			    "You have been disconnected! The server may have failed or kicked you out of the session.",
			    "Disconnected warning",
			    JOptionPane.WARNING_MESSAGE);
		UIController.getInstance().getDeveloperComponent().client = null; 

		
	}

	@Override
	public void onDisconnect() {
		JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
			    "You have disconnected from the current session.",
			    "Disconnected warning",
			    JOptionPane.WARNING_MESSAGE);
		UIController.getInstance().getDeveloperComponent().client = null; 
		
	}
	@Override
	public void onConnectFail() {
		JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
			    "Unable to connect to server, the server you are trying to connect is unreachable.",
			    "Connection error",
			    JOptionPane.ERROR_MESSAGE);
		UIController.getInstance().getDeveloperComponent().client = null; 
	}
	public void startAwaitSync() {

		sync = new awaitSyncDialog(); 
	}

}
