package net.realmproject.rosbridge.client.client.publisher;

import java.io.Closeable;
import java.io.IOException;

/**
 * Interface for publishing messages to a topic which has been advertised for
 * @author nathaniel
 *
 */
public interface RosBridgePublisher extends Closeable
{
	String getTopic();
	String getType();
	void publish(Object message) throws InterruptedException, IOException;
}