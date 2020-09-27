package network;

import java.io.Serializable;

import javaMiniSockets.serverSide.ClientInfo;
import javaMiniSockets.serverSide.ServerMessageHandler;

public class ServerHandler implements ServerMessageHandler {

	@Override
	public void onMessageSent(Serializable message, ClientInfo client) {
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
