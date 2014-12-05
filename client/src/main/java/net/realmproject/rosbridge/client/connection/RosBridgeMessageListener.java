package net.realmproject.rosbridge.client.connection;

/**
 * Interface used to listen to {@link RosBridgeConnection}s for
 * inbound {@link RosBridgeMessage}s
 * @author nathaniel
 *
 */
public interface RosBridgeMessageListener
{
	void handleMessage(RosBridgeMessage message);
}
