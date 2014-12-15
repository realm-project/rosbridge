package net.realmproject.rosbridge.connection.impl;

import java.util.ArrayList;
import java.util.List;

import net.realmproject.rosbridge.connection.RosBridgeConnection;
import net.realmproject.rosbridge.connection.RosBridgeMessage;
import net.realmproject.rosbridge.connection.RosBridgeMessageListener;

public abstract class AbstractRosBridgeConnection implements RosBridgeConnection
{
	
	private List<RosBridgeMessageListener> listeners = new ArrayList<>();

	@Override
	public void addMessageListener(RosBridgeMessageListener listener) {
		listeners.add(listener);
	}

	@Override
	public void clearMessageListeners() {
		listeners.clear();
	}

	@Override
	public void removeMessageListener(RosBridgeMessageListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void notifyMessageListeners(RosBridgeMessage message) {
		for (RosBridgeMessageListener listener : listeners) {
			listener.handleMessage(message);
		}
	}
	


	
	
}
