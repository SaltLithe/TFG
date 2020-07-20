package TFG;

import java.io.Serializable;

import miniSockets.ClientMessageHandler;
import miniSockets.ServerInfo;

public class ClientHandler extends ClientMessageHandler {

	TextEditorToolbar panel;

	public void setTextEditorPanel(TextEditorToolbar panel) {

		this.panel = panel;
	}

	@Override
	public void onMessageSent(Serializable message, ServerInfo serverInfo) {
		// TODO Auto-generated method stub

		System.out.println("Mensajito");
		WriteMessage incoming = (WriteMessage) message;
		try {

			if (incoming.adding) {
				panel.updateContents(incoming.caret, incoming.added);
			}
		} catch (Exception e) {
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
