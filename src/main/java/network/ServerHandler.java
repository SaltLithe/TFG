package network;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

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
import userInterface.networkManagement.serverAwaitSync;

public class ServerHandler implements ServerMessageHandler {
	PropertyChangeMessenger support;
	ArrayBlockingQueue<WriteMessage> processBuffer = new ArrayBlockingQueue<WriteMessage>(100);
	ArrayBlockingQueue<HighLightMessage> highlightBuffer = new ArrayBlockingQueue<HighLightMessage>(100);
	UIController controller;
	DeveloperComponent component;
	private HashSet<String> sessionNames = new HashSet<String>();
	private serverAwaitSync sync;
	private int nClients;
	private Semaphore syncSem;
	String chosenImage;
	Color chosenColor;
	String chosenName;
	private LinkedList<ImageDataMessage> connectedClients;
	private HashMap<String , Integer> colorData;
	private AtomicInteger syncCount = new AtomicInteger(); 


	public void syncComplete() {
		try {
			syncSem.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (sync != null) {
			sync.updateSyncCount(1);
		}
		
		if(syncCount.incrementAndGet() == nClients ) {
			
			SyncEndedMessage syncEnded = new SyncEndedMessage();
			controller.run(()-> component.sendMessageToEveryone(syncEnded));
		}
		
		syncSem.release();
	}

	public void closeServer() {
		controller.run(() -> component.closeServer());
		SyncEndedMessage syncEnded = new SyncEndedMessage();
		controller.run(()-> component.sendMessageToEveryone(syncEnded));

	}

	public void processMessage() {

		ArrayList<Object> messages = new ArrayList<Object>();

		while (true) {
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
			messages.add(incoming.name);
			messages.add(incoming.linestart);
			messages.add(incoming.lineend);
			messages.add(colorData.get(incoming.name));
			messages.add(incoming.keypath);
			support.notify(ObserverActions.UPDATE_HIGHLIGHT, null, messages);
			messages.clear();

		}

		
	}

	public ServerHandler(String chosenName, int nClients , String chosenImage , Color chosenColor) {

		connectedClients = new LinkedList<ImageDataMessage>();
		colorData = new HashMap<String, Integer>(); 
		this.chosenImage = chosenImage;
		this.chosenColor = chosenColor;
		this.chosenName = chosenName;
		syncSem = new Semaphore(1);
		this.nClients = nClients;
		sessionNames.add(chosenName);
		support = PropertyChangeMessenger.getInstance();
		Thread t = new Thread(() -> processMessage());
		t.start();
		
		new Thread(()-> processHighLights()).start();
		controller = UIController.getInstance();
		component = controller.getDeveloperComponent();

	}

	@Override
	public void onMessageSent(Serializable message, ClientInfo client) {

		DEBUG.debugmessage("HA LLEGADO UN MENSAJE DEL CLIENTE");

		String messageclass = message.getClass().toString();

		switch (messageclass) {
		case "class network.WriteMessage":

			WriteMessage incoming = (WriteMessage) message;

			try {

				processBuffer.put(incoming);
				component.sendMessageToEveryone(incoming);
			} catch (Exception e) {

				e.printStackTrace();

			}
			break;

		case "class network.RequestWorkspaceMessage":

			ResponseCreateFileMessage response = new ResponseCreateFileMessage();

			RequestWorkspaceMessage request = (RequestWorkspaceMessage) message;

			if (sessionNames.contains(request.clientName)) {
				String newname = request.clientName;
				int count = 2;
				do {
					newname += Integer.toString(count);
					count++;

				} while (sessionNames.contains(newname));
				sessionNames.add(newname);
				response.newname = newname;

			} else {
				response.newname = null;
			}

			response.path = component.getWorkSpaceName();
			response.type = FILE_PROPERTIES.workspaceProperty.toString();

			controller.runOnThread(()-> component.sendSyncToClient(response, client.clientID));

			break;

		case "class network.ImageDataMessage" :
			ImageDataMessage imageData = (ImageDataMessage) message;
			imageData.clientID = client.clientID;
			ImageDataMessage returnData =  new ImageDataMessage(chosenImage,chosenColor.getRGB(), chosenName, true);
			colorData.put(imageData.name , imageData.color);
			try {
			controller.run(()-> component.sendToClient(returnData, client.clientID));
			controller.run(()-> component.sendMessageToEveryone(imageData));
			for(ImageDataMessage clientData : connectedClients) {
				controller.run(()->component.sendToClient(clientData,client.clientID));
				
			}
			}
			catch(Exception e) {}
			connectedClients.add(imageData);
			controller.runOnThread(()-> component.addProfilePicture (imageData.image , imageData.color , imageData.name, imageData.isServer,imageData.clientID));
			
			break;
			
		case "class network.HighLightMessage" :
			System.out.println("INCOMING HIGHLIGHT");
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
	public void onReady() {

		JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
				"The server is ready, other users can join your sesion now.");
		controller.run(() -> component.saveAllFull());
		controller.run(() -> component.closeAllTabs());
		sync = new serverAwaitSync(nClients, this);
	}

	@Override
	public void onDisconnect() {

		JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(), "You have disconnected!.",
				"Server disconnected warning", JOptionPane.WARNING_MESSAGE);

	}

	@Override
	public void onServerConnect(ClientInfo client) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClientConnect(ClientInfo client) {

		if (sync != null) {
			sync.updateConnectCount(1);
		}

	}

	@Override
	public void onClientDisconnect(int clientID) {

		if (sync != null) {
			sync.updateSyncCount(-1);
			sync.updateConnectCount(-1);
			syncCount.decrementAndGet();
		}
		
		ClientDisconnectedMessage disconnected = new ClientDisconnectedMessage(clientID);
		controller.run(()-> component.removeProfilePicture(clientID));
		controller.runOnThread(()->component.sendMessageToEveryone(disconnected));

	}

}
