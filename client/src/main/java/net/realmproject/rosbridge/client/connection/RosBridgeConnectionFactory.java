package net.realmproject.rosbridge.client.connection;

import java.io.IOException;


public interface RosBridgeConnectionFactory {
	RosBridgeConnection connect() throws InterruptedException, IOException;
}
