package network;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

import core.DEBUG;
import javaMiniSockets.serverSide.ClientInfo;
import javaMiniSockets.serverSide.ServerMessageHandler;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;

public class ServerHandler implements ServerMessageHandler {
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

	public ServerHandler() {

		support = PropertyChangeMessenger.getInstance();
		Thread t = new Thread(()-> processMessage());
		t.start(); 

	}

	@Override
	public void onMessageSent(Serializable message, ClientInfo client) {

		DEBUG.debugmessage("HA LLEGADO UN MENSAJE DEL CLIENTE");
		DEBUG.debugmessage(message.getClass().toString());

		WriteMessage incoming = (WriteMessage) message;

		try {
				
			processBuffer.put(incoming);
		} catch (Exception e) {

			e.printStackTrace();

		} 


	}

	@Override
	public void onReady() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisconnect() {
		// TODO Auto-generated method stub

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
