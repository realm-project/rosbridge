package net.realmproject.rosbridge.connection;

import java.io.IOException;


public interface RosBridgeConnectionFactory {
	RosBridgeConnection connect() throws InterruptedException, IOException;
}
