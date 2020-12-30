package network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import javax.swing.JOptionPane;

import core.DEBUG;
import core.DeveloperComponent;
import fileManagement.FILE_PROPERTIES;
import javaMiniSockets.serverSide.ClientInfo;
import javaMiniSockets.serverSide.ServerMessageHandler;
import userInterface.DeveloperMainFrameWrapper;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;
import userInterface.UIController;

public class ServerHandler implements ServerMessageHandler {
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

	public ServerHandler() {

		support = PropertyChangeMessenger.getInstance();
		Thread t = new Thread(()-> processMessage());
		t.start(); 
		controller = UIController.getInstance();
		component = controller.getDeveloperComponent();

	}

	@Override
	public void onMessageSent(Serializable message, ClientInfo client) {

		DEBUG.debugmessage("HA LLEGADO UN MENSAJE DEL CLIENTE");
	

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
		
		case "class network.RequestWorkspaceMessage":
			
		
			ResponseCreateFileMessage response = new ResponseCreateFileMessage(); 
			response.path = component.getWorkSpaceName(); 
			response.type = FILE_PROPERTIES.workspaceProperty.toString();
		
			controller.run(()->component.sendSyncToClient(response, client.clientID));
			
			
		break;
		
		default:
			break;

		}
	}

	@Override
	public void onReady() {

		JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
			    "The server is ready, other users can join your sesion now.");
	}

	@Override
	public void onDisconnect() {
		
		
		JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
			    "You have disconnected!.",
			    "Server disconnected warning",
			    JOptionPane.WARNING_MESSAGE);

	}

	@Override
	public void onServerConnect(ClientInfo client) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClientConnect(ClientInfo client) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClientDisconnect(ClientInfo client) {
		// TODO Auto-generated method stub

	}

}
