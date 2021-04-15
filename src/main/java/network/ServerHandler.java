package network; 

import java.awt.Color;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JOptionPane;

import com.google.common.util.concurrent.MoreExecutors;

import commandController.CommandController;
import core.DEBUG;
import fileManagement.FILE_PROPERTIES;
import javaMiniSockets.serverSide.ClientInfo;
import javaMiniSockets.serverSide.ServerMessageHandler;
import networkMessages.ClientDisconnectedMessage;
import networkMessages.GlobalRunRequestMessage;
import networkMessages.GlobalRunRequestResponse;
import networkMessages.HighLightMessage;
import networkMessages.ImageDataMessage;
import networkMessages.RequestWorkspaceMessage;
import networkMessages.ResponseCreateFileMessage;
import networkMessages.SyncEndedMessage;
import networkMessages.WriteMessage;
import observerController.ObserverActions;
import observerController.PropertyChangeMessenger;
import userInterface.uiGeneral.DeveloperMainFrameWrapper;
import userInterface.uiNetwork.AcceptGlobalDialog;
import userInterface.uiNetwork.serverAwaitSync;

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
	ArrayBlockingQueue<WriteMessage> processBuffer = new ArrayBlockingQueue<>(100);
	ArrayBlockingQueue<HighLightMessage> highlightBuffer = new ArrayBlockingQueue<>(100);
	String chosenImage;
	Color chosenColor;
	String chosenName;
	private HashSet<String> sessionNames = new HashSet<>();
	private serverAwaitSync sync;
	public int nClients;
	private Semaphore syncSem;
	private HashSet<ImageDataMessage> connectedClients;
	private HashMap<String, Integer> colorData;
	private AtomicInteger syncCount = new AtomicInteger();
	private ArrayBlockingQueue <WriteMessage> backlog = new ArrayBlockingQueue<>(100);
	private Semaphore messageSem; 
	
	public boolean running = false; 
	

	ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
	ExecutorService executorService = MoreExecutors.getExitingExecutorService(executor, 100, TimeUnit.MILLISECONDS);

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
		connectedClients = new HashSet<>();
		colorData = new HashMap<>();
		this.chosenImage = chosenImage;
		this.chosenColor = chosenColor;
		this.chosenName = chosenName;
		syncSem = new Semaphore(1);
		this.nClients = nClients;
		sessionNames.add(chosenName);
		support = PropertyChangeMessenger.getInstance();
		
		
		executorService.submit(() -> processMessage());
		executorService.submit(() -> processHighLights());
	

	}

	/**
	 * Method called when a sync operation has finished
	 */
	public void syncComplete() {
		try {
			syncSem.acquire();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
		if (sync != null) {
			sync.updateSyncCount(1);
		}

		if (syncCount.incrementAndGet() == nClients) {

			SyncEndedMessage syncEnded = new SyncEndedMessage();
			CommandController.developerComponent.sendMessageToEveryone(syncEnded);
		}

		syncSem.release();
	}

	/**
	 * Method called when the user decides to close this server from receiving more
	 * clients
	 */
	public void closeServer(int newclients) {
		this.nClients = newclients; 
		CommandController.developerComponent.closeServer(nClients);
		SyncEndedMessage syncEnded = new SyncEndedMessage();
		CommandController.developerComponent.sendMessageToEveryone(syncEnded);
		support.notify(ObserverActions.ENABLE_TEXT_EDITOR, null);
		support.notify(ObserverActions.ENABLE_USERS_PANEL,null);


	}

	/**
	 * Method a thread accesses to process the write petitions from other users
	 */
	public void processMessage() {

		boolean loopAround = true;
		while (loopAround) {
		
			
			WriteMessage incoming = null;
			
			try {
				if(backlog.size() >0 ) {
					incoming = backlog.take();
					CommandController.developerComponent.sendMessageToEveryone(incoming);
				}else {
				incoming = processBuffer.take();
				}
			} catch (InterruptedException e) {
				Thread.interrupted();
				loopAround = false;
				e.printStackTrace();
			}

			Object[] message = { incoming.path, incoming};
			
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
		int lastindexof = messageclass.lastIndexOf('.');
		
		String rawname = messageclass.substring(lastindexof+1, messageclass.length());
		switch (rawname) {
		// Message is a write order
		case "WriteMessage":

		
			WriteMessage incoming = (WriteMessage) message;

			
			try {
				if(!running) {
				processBuffer.put(incoming);
				CommandController.developerComponent.sendMessageToEveryone(incoming);
				}else {
					backlog.put(incoming);
				}
			} catch (Exception e) {

				e.printStackTrace();

			}
			break;

		// Message is a request from a client to receive wworkspace data
		case "RequestWorkspaceMessage":

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

			response.path = CommandController.developerComponent.getWorkSpaceName();
			response.type = FILE_PROPERTIES.workspaceProperty;

			CommandController.runOnThread(() -> CommandController.developerComponent.sendSyncToClient(response, client.clientID));

			break;

		// Message contains image data from clients
		case "ImageDataMessage":
			ImageDataMessage imageData = (ImageDataMessage) message;
			imageData.clientID = client.clientID;
			ImageDataMessage returnData = new ImageDataMessage(chosenImage, chosenColor.getRGB(), chosenName, true);
			colorData.put(imageData.name, imageData.color);
			try {
				CommandController.developerComponent.sendToClient(returnData, client.clientID);
			
				
				for (ImageDataMessage clientData : connectedClients) {
				
						CommandController.developerComponent.sendToClient(clientData, client.clientID);
					
				}
				
				CommandController.developerComponent.sendMessageToEveryone(imageData);
			} catch (Exception e) {
			}
			if (!(connectedClients.contains(imageData))) {
				connectedClients.add(imageData);
			}
			CommandController.runOnThread(() -> CommandController.developerComponent.addProfilePicture(imageData.image,
					imageData.color, imageData.name, imageData.isServer, imageData.clientID));

			break;

		// Message contais highlight data for the server to paint highlights and
		// redistribute
		case "HighLightMessage":
			HighLightMessage highlightData = (HighLightMessage) message;
			try {
				highlightBuffer.put(highlightData);
				CommandController.developerComponent.sendMessageToEveryone(highlightData);

			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}

			break;
		
		case "GlobalRunRequestResponse":
			GlobalRunRequestResponse runResponse = (GlobalRunRequestResponse) message;
			if(!runResponse.ok) {
				CommandController.developerComponent.runningThread.interrupt();
				GlobalRunRequestResponse clientResponse = new GlobalRunRequestResponse();
				clientResponse.canceled = true;
				CommandController.runOnThread(()-> CommandController.developerComponent.sendMessageToEveryone(clientResponse));
				JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
					    runResponse.name + " did not agree to a global Run.",
					    "Run was canceled",
					    JOptionPane.ERROR_MESSAGE);
			}
			else {
				
				DEBUG.debugmessage("COUNTING DOWN");
				CommandController.developerComponent.waitingResponses.countDown();
				
				
			}
			
			break;
		case "GlobalRunRequestMessage":
			if(!running) {
			running = true; 
			GlobalRunRequestMessage runRequest = (GlobalRunRequestMessage) message;
			if(CommandController.developerComponent.focusedProject == null) {
				GlobalRunRequestResponse serverResponse = new GlobalRunRequestResponse();
				serverResponse.name = chosenName;
				serverResponse.ok = false;
				serverResponse.noProject = true; 
				CommandController.developerComponent.sendToClient(serverResponse, client.clientID);
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
			e.printStackTrace();
			
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

		JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(),
				"The server is ready, other users can join your sesion now.");
		CommandController.developerComponent.saveAllFull();
		CommandController.developerComponent.closeAllTabs();
		support.notify(ObserverActions.DISABLE_TEXT_EDITOR, null);
		support.notify(ObserverActions.DISABLE_USERS_PANEL,null);



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
		CommandController.developerComponent.removeProfilePicture(clientID);
		CommandController.runOnThread(() -> CommandController.developerComponent.sendMessageToEveryone(disconnected));

	}

	public void activateGlobal() {
		support.notify(ObserverActions.DISABLE_LOCAL_RUN, null);
		support.notify(ObserverActions.ENABLE_GLOBAL_RUN,null);
		support.notify(ObserverActions.ENABLE_TEXT_EDITOR,null);
		support.notify(ObserverActions.ENABLE_USERS_PANEL,null);

	}

	
	public void decideRun(boolean decision , int invokerID , GlobalRunRequestMessage sent) {

		if(decision) {
			  
			CommandController.developerComponent.startRunGlobal(true);
			CommandController.developerComponent.waitingResponses.countDown();
			
		}
		else {
			GlobalRunRequestResponse message = new GlobalRunRequestResponse();
			message.name = chosenName;
			message.canceled = true;
			CommandController.developerComponent.sendToClient(message, invokerID);
			running = false; 
			
			
		}
		
	}
	
	public void shutdownProcessors() {
		
		executorService.shutdown();
	}
	
	@Override
	public void onServerConnectionProblem() {
		JOptionPane.showMessageDialog(DeveloperMainFrameWrapper.getInstance(), "There was a problem starting the server, check your ip address, consider using another port and try again ",
				"Server start error", JOptionPane.ERROR_MESSAGE);
		CommandController.developerComponent.disconnect();
	}


	

}
