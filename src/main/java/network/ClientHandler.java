package network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import core.DEBUG;
import javaMiniSockets.clientSide.ClientMessageHandler;
import javaMiniSockets.clientSide.ServerInfo;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;

public class ClientHandler implements ClientMessageHandler {

	PropertyChangeMessenger support;
	ArrayBlockingQueue<WriteMessage> processBuffer = new ArrayBlockingQueue<WriteMessage>(100);

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

		support = PropertyChangeMessenger.getInstance();
		Thread t = new Thread(()-> processMessage());
		t.start(); 


	}

	@Override
	public void onMessageSent(Serializable message, ServerInfo serverInfo) {
		// TODO Auto-generated method stub

		DEBUG.debugmessage(message.toString());

		DEBUG.debugmessage("HA LLEGADO UN MENSAJE DEL SERVER");
		WriteMessage incoming = (WriteMessage) message;


		try {
				
			processBuffer.put(incoming);
		} catch (Exception e) {

			e.printStackTrace();

		} 



	}

	@Override
	public void onServerConnect(ServerInfo server) {
		// TODO Auto-generated method stub

		DEBUG.debugmessage("HANDSHAKE");

	}

	@Override
	public void onConnect() {
		DEBUG.debugmessage("CONNECTED TO SERVER");
		// TODO Auto-generated method stub

	}

	@Override
	public void onServerDisconnect(ServerInfo server) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnect() {
		// TODO Auto-generated method stub

	}

}
