package net.realmproject.rosbridge.client.connection.impl.debug;

import java.io.IOException;

import javax.websocket.DeploymentException;

import net.realmproject.rosbridge.client.connection.RosBridgeConnection;
import net.realmproject.rosbridge.client.connection.RosBridgeMessage;
import net.realmproject.rosbridge.client.connection.RosBridgeMessageListener;

public class DummyRosBridgeConnection implements RosBridgeConnection{

	@Override
	public void close() throws IOException {}

	@Override
	public void sendMessage(String message) throws InterruptedException, IOException {}

	@Override
	public String getNewMessageID() {
		return "";
	}

	@Override
	public void addMessageListener(RosBridgeMessageListener listener) {}

	@Override
	public void clearMessageListeners() {}

	@Override
	public void removeMessageListener(RosBridgeMessageListener listener) {}

	@Override
	public void notifyMessageListeners(RosBridgeMessage message) {}



}
