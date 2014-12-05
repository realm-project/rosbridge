package net.realmproject.rosbridge.client.connection.impl.debug;

import java.io.IOException;

import javax.websocket.DeploymentException;

import net.realmproject.rosbridge.client.connection.RosBridgeConnection;
import net.realmproject.rosbridge.client.connection.impl.AbstractRosBridgeConnection;

public class StringBuilderRosBridgeConnection extends AbstractRosBridgeConnection {


	private StringBuilder sb = new StringBuilder();
	
	private int messageID = 0;
	private boolean closed = false;
	
	@Override
	public void sendMessage(String message) {
		if (closed) { throw new IllegalStateException("Attempted to send message after closing connection"); }
		sb.append(message + "\n");
	}

	@Override
	public String getNewMessageID() {
		return "" + messageID++;
	}

	@Override
	public void close() throws IOException {
		closed = true;
	}
	
	public String toString() {
		return sb.toString();
	}

}
