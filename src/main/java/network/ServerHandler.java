package network;

import java.io.Serializable;
import java.util.ArrayList;

import core.DEBUG;
import javaMiniSockets.serverSide.ClientInfo;
import javaMiniSockets.serverSide.ServerMessageHandler;
import userInterface.ObserverActions;
import userInterface.PropertyChangeMessenger;

public class ServerHandler implements ServerMessageHandler {
	PropertyChangeMessenger support;
	ArrayList<Object> messages;

	public ServerHandler() {

		support = PropertyChangeMessenger.getInstance();
		messages = new ArrayList<Object>();

	}

	@Override
	public void onMessageSent(Serializable message, ClientInfo client) {

		DEBUG.debugmessage("HA LLEGADO UN MENSAJE DEL CLIENTE");
		DEBUG.debugmessage(message.getClass().toString());

		WriteMessage incoming = (WriteMessage) message;

		try {

			if (incoming.adding) {
				messages.add(incoming.caret);
				messages.add(incoming.added);
				messages.add(incoming.adding);
				support.notify(ObserverActions.UPDATE_PANEL_CONTENTS, null, messages);
			} else if (!incoming.adding) {

				messages.add(incoming.caret);
				messages.add(incoming.lenght);
				messages.add(incoming.adding);
				support.notify(ObserverActions.UPDATE_PANEL_CONTENTS, null, messages);

			}
		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			messages.clear();
		}

		// TODO Auto-generated method stub

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
