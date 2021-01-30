package network;

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JOptionPane;

import core.DEBUG;
import fileManagement.FILE_PROPERTIES;
import javaMiniSockets.serverSide.ClientInfo;
import javaMiniSockets.serverSide.ServerMessageHandler;
import userInterface.DeveloperMainFrameWrapper;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;
import userInterface.UIController;
import userInterface.networkManagement.AcceptGlobalDialog;
import userInterface.networkManagement.serverAwaitSync;

/**
 * Class implementing the SErver Message Handler it receives the calls from the
 * server when certain events happen Events like receiving messages or
 * connection notifications
 * 
 * @author Carmen Gómez Moreno
 *
 */
public class ServerHandler implements ServerMessageHandler {

	PropertyChangeMessenger support;
	ArrayBlockingQueue<WriteMessage> processBuffer = new ArrayBlockingQueue<WriteMessage>(100);
	ArrayBlockingQueue<HighLightMessage> highlightBuffer = new ArrayBlockingQueue<HighLightMessage>(100);
	String chosenImage;
	Color chosenColor;
	String chosenName;
	private HashSet<String> sessionNames = new HashSet<String>();
	private serverAwaitSync sync;
	public int nClients;
	private Semaphore syncSem;
	private HashSet<ImageDataMessage> connectedClients;
	private HashMap<String, Integer> colorData;
	private AtomicInteger syncCount = new AtomicInteger();
	private ArrayBlockingQueue <WriteMessage> backlog = new ArrayBlockingQueue<WriteMessage>(100);
	private Semaphore messageSem; 
	
	public boolean running = false; 

	/**
	 * 
	 * @param chosenName  : The name of this user
	 * @param nClients    : The maximum number of clients that can connect to this
	 *                    server
	 * @param chosenImage : The data representing this users profile icon image
	 * @param chosenColor : The object representing this users profile icon color
	 */
	public ServerHandler(String chosenName, int nClients, String chosenImage, Color chosenColor) {

		messageSem  = new Semaphore(1);
		connectedClients = new HashSet<ImageDataMessage>();
		colorData = new HashMap<String, Integer>();
		this.chosenImage = chosenImage;
		this.chosenColor = chosenColor;
		this.chosenName = chosenName;
		syncSem = new Semaphore(1);
		this.nClients = nClients;
		sessionNames.add(chosenName);
		support = PropertyChangeMessenger.getInstance();
		 new Thread(() -> processMessage()).start();;
		new Thread(() -> processHighLights()).start();
	

	}

	/**
	 * Method called when a sync operation has finished
	 */
	public void syncComplete() {
		try {
			syncSem.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (sync != null) {
			sync.updateSyncCount(1);
		}

		if (syncCount.incrementAndGet() == nClients) {

			SyncEndedMessage syncEnded = new SyncEndedMessage();
			UIController.developerComponent.sendMessageToEveryone(syncEnded);
		}

		syncSem.release();
	}

	/**
	 * Method called when the user decides to close this server from receiving more
	 * clients
	 */
	public void closeServer(int newclients) {
		this.nClients = newclients; 
		UIController.developerComponent.closeServer(nClients);
		SyncEndedMessage syncEnded = new SyncEndedMessage();
		UIController.developerComponent.sendMessageToEveryone(syncEnded);
		support.notify(ObserverActions.ENABLE_TEXT_EDITOR, null);

	}

	/**
	 * Method a thread accesses to process the write petitions from other users
	 */
	public void processMessage() {

		while (true) {
		
			
			WriteMessage incoming = null;
			
			try {
				if(backlog.size() >0 ) {
					incoming = backlog.take();
					UIController.developerComponent.sendMessageToEveryone(incoming);
				}else {
				incoming = processBuffer.take();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			DEBUG.debugmessage("GOT A WRITEMESSAGE");
			Object[] message = { incoming.path, incoming };
			support.notify(ObserverActions.UPDATE_PANEL_CONTENTS, message);

		}

	}

	/**
	 * Method a thread accesses to process the highlight information other users
	 * send
	 */
	public void processHighLights() {

		while (true) {
			HighLightMessage incoming = null;
			try {
				incoming = highlightBuffer.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (!incoming.name.equals(chosenName)) {

				Object[] message = { incoming.linestart, incoming.lineend, colorData.get(incoming.name), incoming.name,
						incoming.keypath };
				support.notify(ObserverActions.UPDATE_HIGHLIGHT, message);
			}
		}

	}

	/**
	 * Implementation of the method that sends messages as they are received Behaves
	 * differently depending on the type of message received
	 */
	@Override
	public void onMessageSent(Serializable message, ClientInfo client) {

		try {
			
		messageSem.acquire(); 
		String messageclass = message.getClass().toString();

		switch (messageclass) {
		// Message is a write order
		case "class network.WriteMessage":

		
			WriteMessage incoming = (WriteMessage) message;

			
			try {
				if(!running) {
				processBuffer.put(incoming);
				UIController.developerComponent.sendMessageToEveryone(incoming);
				}else {
					backlog.put(incoming);
				}
			} catch (Exception e) {

				e.printStackTrace();

			}
			break;

		// Message is a request from a client to receive wworkspace data
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

			response.path = UIController.developerComponent.getWorkSpaceName();
			response.type = FILE_PROPERTIES.workspaceProperty.toString();

			UIController.runOnThread(() -> UIController.developerComponent.sendSyncToClient(response, client.clientID));

			break;

		// Message contains image data from clients
		case "class network.ImageDataMessage":
			ImageDataMessage imageData = (ImageDataMessage) message;
			imageData.clientID = client.clientID;
			ImageDataMessage returnData = new ImageDataMessage(chosenImage, chosenColor.getRGB(), chosenName, true);
			colorData.put(imageData.name, imageData.color);
			try {
				UIController.developerComponent.sendToClient(returnData, client.clientID);
			
				
				for (ImageDataMessage clientData : connectedClients) {
				
						UIController.developerComponent.sendToClient(clientData, client.clientID);
					
				}
				
				UIController.developerComponent.sendMessageToEveryone(imageData);
			} catch (Exception e) {
			}
			if (!(connectedClients.contains(imageData))) {
				connectedClients.add(imageData);
			}
			UIController.runOnThread(() -> UIController.developerComponent.addProfilePicture(imageData.image,
					imageData.color, imageData.name, imageData.isServer, imageData.clientID));

			break;

		// Message contais highlight data for the server to paint highlights and
		// redistribute
		case "class network.HighLightMessage":
			HighLightMessage highlightData = (HighLightMessage) message;
			try {
				highlightBuffer.put(highlightData);
				UIController.developerComponent.sendMessageToEveryone(highlightData);

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			break;
		
		case "class network.GlobalRunRequestResponse":
			System.out.println("GOT A RUN RESPONSE");
			GlobalRunRequestResponse runResponse = (GlobalRunRequestResponse) message;
			if(!runResponse.ok) {
				UIController.developerComponent.runningThread.interrupt();
				GlobalRunRequestResponse clientResponse = new GlobalRunRequestResponse();
				clientResponse.canceled = true;
				UIController.runOnThread(()-> UIController.developerComponent.sendMessageToEveryone(clientResponse));
				JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
					    runResponse.name + " did not agree to a global Run.",
					    "Run was canceled",
					    JOptionPane.ERROR_MESSAGE);
			}
			else {
				
				DEBUG.debugmessage("COUNTING DOWN");
				UIController.developerComponent.waitingResponses.countDown();
				
				
			}
			
			break;
		case "class network.GlobalRunRequestMessage":
			if(!running) {
			running = true; 
			GlobalRunRequestMessage runRequest = (GlobalRunRequestMessage) message;
			if(UIController.developerComponent.focusedProject == null) {
				GlobalRunRequestResponse serverResponse = new GlobalRunRequestResponse();
				serverResponse.name = chosenName;
				serverResponse.ok = false;
				serverResponse.noProject = true; 
				UIController.developerComponent.sendToClient(serverResponse, client.clientID);
			}
			else {
			 new AcceptGlobalDialog(runRequest.name , this , client.clientID , runRequest);
			}

			}
			break;
		default:
			break;

		}
		}catch(Exception e) {
			
		}finally {
			messageSem.release(); 
		}
	}

	/**
	 * Method called when the server is ready to accept connections , waits for a
	 * sync operation
	 */
	@Override
	public void onReady() {

		// TODO check if this dialog blocks
		JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
				"The server is ready, other users can join your sesion now.");
		UIController.developerComponent.saveAllFull();
		UIController.developerComponent.closeAllTabs();
		support.notify(ObserverActions.DISABLE_TEXT_EDITOR, null);


		sync = new serverAwaitSync(nClients, this);
	}

	/**
	 * Method called when the server disconnects , breaking all connections between
	 * the clients
	 */
	@Override
	public void onDisconnect() {

		JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(), "You have disconnected!.",
				"Server disconnected warning", JOptionPane.WARNING_MESSAGE);

	}

	@Override
	public void onServerConnect(ClientInfo client) {

	}

	/**
	 * Method called when a client has connected
	 */
	@Override
	public void onClientConnect(ClientInfo client) {
		// TODO , MAYBE THIS SHOULD GO IN THE METHOD ABOVE

		if (sync != null) {
			sync.updateConnectCount(1);
		}

	}

	/**
	 * Method called when a client disconnects from this server
	 */
	@Override
	public void onClientDisconnect(int clientID) {

		if (sync != null) {
			sync.updateSyncCount(-1);
			sync.updateConnectCount(-1);
			syncCount.decrementAndGet();
		}

		ClientDisconnectedMessage disconnected = new ClientDisconnectedMessage(clientID);
		UIController.developerComponent.removeProfilePicture(clientID);
		UIController.runOnThread(() -> UIController.developerComponent.sendMessageToEveryone(disconnected));

	}

	public void activateGlobal() {
		support.notify(ObserverActions.DISABLE_LOCAL_RUN, null);
		support.notify(ObserverActions.ENABLE_GLOBAL_RUN,null);
		support.notify(ObserverActions.ENABLE_TEXT_EDITOR,null);
	}

	
	public void decideRun(boolean decision , int invokerID ,String invokerName, GlobalRunRequestMessage sent) {

		if(decision) {
			  
			UIController.developerComponent.startRunGlobal(true);
			UIController.developerComponent.waitingResponses.countDown();
			
		}
		else {
			GlobalRunRequestResponse message = new GlobalRunRequestResponse();
			message.name = chosenName;
			message.canceled = true;
			UIController.developerComponent.sendToClient(message, invokerID);
			running = false; 
			
			
		}
		
	}
	
	

	

}
