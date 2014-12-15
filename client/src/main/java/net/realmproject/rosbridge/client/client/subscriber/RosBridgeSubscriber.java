package net.realmproject.rosbridge.client.client.subscriber;

import java.io.Closeable;
import java.util.Date;
import java.util.function.Consumer;

import net.realmproject.rosbridge.connection.RosBridgeMessage;

/**
 * RosJsonSubscribers track messages from a ROS subscription to a single topic
 * @author nathaniel
 *
 */

public interface RosBridgeSubscriber<T> extends Consumer<RosBridgeMessage>, Closeable
{
	String getId();
	String getTopic();
	T getLastMessage();
	Date getLastMessageDate();
	void setHandler(Consumer<T> handler);
}
