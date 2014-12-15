package net.realmproject.rosbridge.connection.impl.debug;

import java.io.IOException;
import java.io.Writer;

import javax.websocket.DeploymentException;

import net.realmproject.rosbridge.connection.RosBridgeConnection;
import net.realmproject.rosbridge.connection.RosBridgeMessage;
import net.realmproject.rosbridge.connection.RosBridgeMessageListener;
import net.realmproject.rosbridge.util.RosBridgeSerialize;

public class LoggingRosBridgeConnection implements RosBridgeConnection {

	private RosBridgeConnection backer;
	private Writer log;
	private boolean logInput = true;
	private boolean logOutput = true;
	
	public LoggingRosBridgeConnection(RosBridgeConnection backer, final Writer log) {
		this(backer, log, true, true);
	}
	
	public LoggingRosBridgeConnection(RosBridgeConnection backer, final Writer log, final boolean logInput, final boolean logOutput) {
		this.backer = backer;
		this.log = log;
		this.logInput = logInput;
		this.logOutput = logOutput;
		
		backer.addMessageListener(message -> {
			try {
				if (logInput) { 
					log.write("Receiving: " + RosBridgeSerialize.serialize(message) + "\n");
					log.flush();
				}
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
		
	}
	
	
	public void sendMessage(String message) throws InterruptedException, IOException {
		try {
			if (logOutput) { 
				log.write("Sending: " + message + "\n");
				log.flush();
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		backer.sendMessage(message);
	}

	public void addMessageListener(RosBridgeMessageListener listener) {
		backer.addMessageListener(listener);
	}

	public void clearMessageListeners() {
		backer.clearMessageListeners();
	}

	public void close() throws IOException {
		backer.close();
	}

	public String getNewMessageID() {
		return backer.getNewMessageID();
	}

	public void removeMessageListener(RosBridgeMessageListener listener) {
		backer.removeMessageListener(listener);
	}

	public void notifyMessageListeners(RosBridgeMessage message) {
		backer.notifyMessageListeners(message);
	}

	
}
