package net.realmproject.rosbridge.client.client;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import net.realmproject.rosbridge.client.client.publisher.RosBridgePublisher;
import net.realmproject.rosbridge.client.client.subscriber.RosBridgeSubscriber;
import net.realmproject.rosbridge.client.datatypes.RosType;
import net.realmproject.rosbridge.connection.RosBridgeConnection;
import net.realmproject.rosbridge.connection.RosBridgeMessage;

/**
 * High-level client for (un)subscribing to topics, and making system calls via ROSBridge
 * @author Nathaniel Sherry
 *
 */

public interface RosBridgeClient extends Closeable
{
	
	
	/**
	 * Subscribe to a topic in ROS, and handle each event
	 * @param topic the topic to subscribe to
	 * @param handler a handler function to process all messages on this topic
	 * @return a new {@link RosBridgeSubscriber}
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	RosBridgeSubscriber<?> subscribe(String topic, Consumer<Object> handler) throws InterruptedException, IOException;
	
	
	/**
	 * Subscribe to a topic in ROS, and handle each event
	 * @param topic the topic to subscribe to
	 * @param type the data type of this topic
	 * @param handler a handler function to process all messages on this topic
	 * @return a new {@link RosBridgeSubscriber}
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	RosBridgeSubscriber<?> subscribe(String topic, String type, Consumer<Object> handler) throws InterruptedException, IOException;
	
	
	/**
	 * Subscribe to a topic in ROS
	 * @param topic the topic to subscribe to
	 * @return a new {@link RosBridgeSubscriber}
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	RosBridgeSubscriber<?> subscribe(String topic) throws InterruptedException, IOException;
	

	/**
	 * Subscribe to a topic in ROS
	 * @param topic the topic to subscribe to
	 * @param type the data type of this topic
	 * @return a new {@link RosBridgeSubscriber}
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	RosBridgeSubscriber<?> subscribe(String topic, String type) throws InterruptedException, IOException;
	
	
	
	/**
	 * Subscribe to a topic in ROS, and handle each event
	 * @param topic the topic to subscribe to
	 * @param handler a handler function to process all messages on this topic
	 * @param clazz the class of the data being received
	 * @return a new {@link RosBridgeSubscriber}
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	<T> RosBridgeSubscriber<T> subscribe(String topic, Class<T> clazz, Consumer<T> handler) throws InterruptedException, IOException;
	
	
	/**
	 * Subscribe to a topic in ROS, and handle each event
	 * @param topic the topic to subscribe to
	 * @param type the data type of this topic
	 * @param handler a handler function to process all messages on this topic
	 * @param clazz the class of the data being received
	 * @return a new {@link RosBridgeSubscriber}
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	<T> RosBridgeSubscriber<T> subscribe(String topic, String type, Class<T> clazz, Consumer<T> handler) throws InterruptedException, IOException;
	
	
	/**
	 * Subscribe to a topic in ROS
	 * @param topic the topic to subscribe to
	 * @param clazz the class of the data being received
	 * @return a new {@link RosBridgeSubscriber}
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	<T> RosBridgeSubscriber<T> subscribe(String topic, Class<T> clazz) throws InterruptedException, IOException;
	

	/**
	 * Subscribe to a topic in ROS
	 * @param topic the topic to subscribe to
	 * @param type the data type of this topic
	 * @param clazz the class of the data being received
	 * @return a new {@link RosBridgeSubscriber}
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	<T> RosBridgeSubscriber<T> subscribe(String topic, String type, Class<T> clazz) throws InterruptedException, IOException;
	
	
	
	
	
	/**
	 * Unsubscribe from a topic in ROS. This is equivalent to calling the 
	 * {@link RosBridgeSubscriber#close()} method.
	 * @param sub the {@link RosBridgeSubscriber} object representing the subscription
	 * @throws IOException 
	 */
	void unsubscribe(RosBridgeSubscriber<?> sub) throws InterruptedException, IOException;

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * Advertises that this client will publish messages on the given topic 
	 * @param topic the name of the topic to publish on
	 * @param type data type that will be published
	 * @return a RosBridgePublisher which can be used to publish messages
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	RosBridgePublisher advertise(String topic, String type) throws InterruptedException, IOException;
	
	/**
	 * Advertises that this client will publish messages on the given topic 
	 * @param topic the name of the topic to publish on
	 * @param cls the {@link RosType} annotated class representing the data type
	 * @return a RosBridgePublisher which can be used to publish messages
	 * @throws InterruptedException 
	 * @throws IOException 
	 * @throws IllegalArgumentException
	 */
	<T> RosBridgePublisher advertise(String topic, Class<T> cls) throws InterruptedException, IOException, IllegalArgumentException;
	
	
	/**
	 * Advertises that this client will no longer publish messages on the given 
	 * topic. This is equivalent to calling the {@link RosBridgePublisher#close()} 
	 * method.
	 * @param publisher the {@link RosBridgePublisher} to stop publishing with.
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	void unadvertise(RosBridgePublisher publisher) throws InterruptedException, IOException;
	
	
	/**
	 * Convenience method for publishing a single method. Advertises, publishes, 
	 * and unadvertises. This should not be used to send repeated messages, as
	 * constant advertising/unadvertising should be avoided.
	 * @param topic the name of the topic to publish on
	 * @param type data type that will be published
	 * @param message the message to publish
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	void publishOnce(String topic, String type, Object message) throws InterruptedException, IOException;
	
	
	
	
	
	
	
	
	
	/**
	 * Perform an asynchronous method call accepting a list of arguments and returning a {@link Future}
	 * @return {@link Future} for a {@link List} of Objects as return values
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	Future<Map<String, Object>> call(String service, List<?> args) throws InterruptedException, IOException;

	/**
	 * Perform an asynchronous method call returning a {@link Future}
	 * @return {@link Future} for a {@link List} of Objects as return values
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	Future<Map<String, Object>> call(String service) throws InterruptedException, IOException;

	
	/**
	 * Perform an asynchronous method call accepting a list of arguments and a response handler
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	void call(String service, List<?> args, Consumer<Map<String, Object>> handler) throws InterruptedException, IOException;

	/**
	 * Perform an asynchronous method call accepting a response handler
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	void call(String service, Consumer<Map<String, Object>> handler) throws InterruptedException, IOException;
	
	
	
	
	/**
	 * Perform an asynchronous method call accepting a list of arguments and returning a {@link Future}
	 * @return {@link Future} for a {@link List} of Objects as return values
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	<T> Future<T> call(String service, List<?> args, Class<T> clazz) throws InterruptedException, IOException;

	/**
	 * Perform an asynchronous method call returning a {@link Future}
	 * @return {@link Future} for a {@link List} of Objects as return values
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	<T> Future<T> call(String service, Class<T> clazz) throws InterruptedException, IOException;

	
	/**
	 * Perform an asynchronous method call accepting a list of arguments and a response handler
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	<T> void call(String service, List<?> args, Class<T> clazz, Consumer<T> handler) throws InterruptedException, IOException;

	/**
	 * Perform an asynchronous method call accepting a response handler
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	<T> void call(String service, Class<T> clazz, Consumer<T> handler) throws InterruptedException, IOException;
	
	
	
	
	
	
	
	
	/**
	 * Add new handler
	 * @param opcode message type this handler should consume
	 * @param handler handler function
	 */
	void addCustomHandler(String opcode, Consumer<RosBridgeMessage> handler);
	
	/**
	 * Remove an existing handler for the given opcode
	 * @param opcode message type this handler was consuming
	 * @param handler handler function
	 */
	void removeCustomHandler(String opcode, Consumer<RosBridgeMessage> handler);
	
	
}
