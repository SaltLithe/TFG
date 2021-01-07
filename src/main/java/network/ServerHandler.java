package network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

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
	UIController controller;
	DeveloperComponent component;
	private HashSet<String> sessionNames = new HashSet<String>();
	private serverAwaitSync sync;
	private int nClients;
	private Semaphore syncSem;

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

		syncSem.release();
	}

	public void closeServer() {
		controller.run(() -> component.closeServer());

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

	public ServerHandler(String chosenName, int nClients) {

		syncSem = new Semaphore(1);
		this.nClients = nClients;
		sessionNames.add(chosenName);
		support = PropertyChangeMessenger.getInstance();
		Thread t = new Thread(() -> processMessage());
		t.start();
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

			controller.runOnThread(() -> component.sendSyncToClient(response, client.clientID));

			break;

		case "class network.ImageDataMessage" :
			ImageDataMessage imageData = (ImageDataMessage) message;
			controller.runOnThread(()-> component.addProfilePicture (imageData.image , imageData.color , imageData.name));
			
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
	public void onClientDisconnect(ClientInfo client) {

		if (sync != null) {
			sync.updateSyncCount(-1);
			sync.updateConnectCount(-1);
		}

	}

}
