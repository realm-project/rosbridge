package net.realmproject.rosbridge.client.client;

import java.io.IOException;

public interface RosBridgeClientFactory {
	RosBridgeClient connect() throws InterruptedException, IOException;
}
