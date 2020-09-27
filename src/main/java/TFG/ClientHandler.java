package TFG;

import java.io.Serializable;
import java.util.ArrayList;

import miniSockets.ClientMessageHandler;
import miniSockets.ServerInfo;

public class ClientHandler implements ClientMessageHandler {

	PropertyChangeMessenger support;
	ArrayList<Object> messages;

	public ClientHandler() {

		support = PropertyChangeMessenger.getInstance();
		messages = new ArrayList<Object>();

	}

	@Override
	public void onMessageSent(Serializable message, ServerInfo serverInfo) {
		// TODO Auto-generated method stub

		System.out.println("Mensajito");
		WriteMessage incoming = (WriteMessage) message;

		try {

			if (incoming.adding) {
				messages.add(incoming.caret);
				messages.add(incoming.added);
				messages.add(incoming.adding);
				support.notify(ObserverActions.UPDATE_PANEL_CONTENTS, null, messages);
				// panel.updateContents(incoming.caret, incoming.added);
			} else if (!incoming.adding) {

				messages.add(incoming.caret);
				messages.add(incoming.lenght);
				messages.add(incoming.adding);
				support.notify(ObserverActions.UPDATE_PANEL_CONTENTS, null, messages);

			}

		} catch (Exception e) {
		} finally {
			messages.clear();
		}

	}

	@Override
	public void onServerConnect(ServerInfo server) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnect() {
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
