package network; 

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JOptionPane;

import core.DEBUG;
import core.DeveloperComponent;
import fileManagement.FILE_PROPERTIES;
import fileManagement.WorkSpaceManager;
import javaMiniSockets.clientSide.ClientMessageHandler;
import javaMiniSockets.clientSide.ServerInfo;
import userInterface.DeveloperMainFrameWrapper;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;
import userInterface.UIController;
import userInterface.networkManagement.acceptSyncDialog;

public class ClientHandler implements ClientMessageHandler {

	PropertyChangeMessenger support;
	ArrayBlockingQueue<WriteMessage> processBuffer = new ArrayBlockingQueue<WriteMessage>(100);
	UIController controller;
	DeveloperComponent component;

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
	public ClientHandler() {

		controller = UIController.getInstance();
		component = controller.getDeveloperComponent();
		support = PropertyChangeMessenger.getInstance();
		Thread t = new Thread(()-> processMessage());
		t.start(); 


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
				
			processBuffer.put(incoming);
		} catch (Exception e) {

			e.printStackTrace();

		} 
		break;
	
		case "class network.ResponseCreateFileMessage":
			ResponseCreateFileMessage incoming1 = (ResponseCreateFileMessage) message;
			
			if(incoming1.type.equals(FILE_PROPERTIES.projectProperty.toString())) {
		
			 component.createWorkSpace(incoming1.path);
			}
				
			
			
			
			break;
		default:
			break;
		}

	}

	@Override
	public void onServerConnect(ServerInfo server) {

		DEBUG.debugmessage("HANDSHAKE");
		JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
			    "Success! You have connected to a session.");
		
		//acceptSyncDialog d = new acceptSyncDialog(); 
		

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

}
