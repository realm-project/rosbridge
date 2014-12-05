package net.realmproject.rosbridge.client.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import net.realmproject.rosbridge.client.client.call.IRosBridgeCallbackCall;
import net.realmproject.rosbridge.client.client.call.IRosBridgeFutureCall;
import net.realmproject.rosbridge.client.client.call.RosBridgeCall;
import net.realmproject.rosbridge.client.client.publisher.IRosBridgePublisher;
import net.realmproject.rosbridge.client.client.publisher.RosBridgePublisher;
import net.realmproject.rosbridge.client.client.subscriber.IRosBridgeSubscriber;
import net.realmproject.rosbridge.client.client.subscriber.RosBridgeSubscriber;
import net.realmproject.rosbridge.client.connection.RosBridgeConnection;
import net.realmproject.rosbridge.client.connection.RosBridgeMessage;
import net.realmproject.rosbridge.client.connection.RosBridgeMessageListener;
import net.realmproject.rosbridge.client.datatypes.RosType;
import net.realmproject.util.RealmSerialize;
import net.realmproject.util.RealmThread;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

/**
 * This class provides the functionality defined in the 
 * {@link RosBridgeClient} interface by using a system of {@link Consumer} 
 * handlers which can register to listen for specific opcodes and take 
 * action as required. {@link RosBridgeCall}s and 
 * {@link RosBridgeSubscriber}s are forms of {@link Consumer} handlers
 * @author nathaniel
 *
 */

public class IRosBridgeClient implements RosBridgeClient
{
	
	public static final String SERVICE_CALL = "call_service";
	public static final String SERVICE_RETURN = "service_response";
	public static final String TOPIC_SUBSCRIBE = "subscribe";
	public static final String TOPIC_UNSUBSCRIBE = "unsubscribe";
	public static final String TOPIC_PUBLISH = "publish";
	public static final String TOPIC_ADVERTISE = "advertise";
	public static final String TOPIC_UNADVERTISE = "unadvertise";
	
	private RosBridgeConnection connection;
	private Multimap<String, Consumer<RosBridgeMessage>> handlers = HashMultimap.create();
	
	private Set<RosBridgePublisher> publishers = new HashSet<>();
	
	public IRosBridgeClient(RosBridgeConnection connection) {
		this.connection = connection;
		
		//set up a listener for messages from this client
		connection.addMessageListener(this::handleMessage);
	}
	

	public void sendMessage(String message) throws InterruptedException, IOException {
		connection.sendMessage(message);
	}
	
	public String getNewMessageID(){
		return connection.getNewMessageID();
	}
	
	/**
	 * Process messages from the RosJson Server
	 */
	private void handleMessage(RosBridgeMessage message) {
		if (! handlers.containsKey(message.getOpcode())) return;
		
		//get a copy of the handlers, so that if one removes itself, we don't get concurrent mod ex
		List<Consumer<RosBridgeMessage>> handlerList = new ArrayList<>(handlers.get(message.getOpcode()));
		
		for (Consumer<RosBridgeMessage> handler : handlerList){
			handler.accept(message);
		}
	}
	
	
	

	/*********************************************
	 * CUSTOM MESSAGE HANDLERS
	 *********************************************/

	@Override
	public void addCustomHandler(String opcode, Consumer<RosBridgeMessage> handler) {
		handlers.put(opcode, handler);
	}
	
	@Override
	public void removeCustomHandler(String opcode, Consumer<RosBridgeMessage> handler) {
		handlers.remove(opcode, handler);
	}
	
	
	
	
	
	
	/*********************************************
	 * TOPIC CONSUMER - SUBSCRIPTIONS
	 * @throws InterruptedException 
	 * @throws IOException 
	 *********************************************/

	@Override
	public RosBridgeSubscriber<?> subscribe(String topic, Consumer<Object> handler) throws InterruptedException, IOException {
		return subscribe(topic, null, Object.class, handler);		
	}
	
	@Override
	public RosBridgeSubscriber<?> subscribe(String topic, String type, Consumer<Object> handler) throws InterruptedException, IOException {
		return subscribe(topic, type, Object.class, handler);
	}
	
	@Override
	public RosBridgeSubscriber<?> subscribe(String topic) throws InterruptedException, IOException {
		return subscribe(topic, null, Object.class, null);
	}

	@Override
	public RosBridgeSubscriber<?> subscribe(String topic, String type) throws InterruptedException, IOException {
		return subscribe(topic, type, Object.class, null);
	}
	
	

	@Override
	public <T> RosBridgeSubscriber<T> subscribe(String topic, Class<T> clazz, Consumer<T> handler) throws InterruptedException, IOException {
		return subscribe(topic, typeFromClass(clazz), clazz, handler);
	}


	@Override
	public <T> RosBridgeSubscriber<T> subscribe(String topic, Class<T> clazz) throws InterruptedException, IOException {
		return subscribe(topic, typeFromClass(clazz), clazz, null);
	}


	@Override
	public <T> RosBridgeSubscriber<T> subscribe(String topic, String type, Class<T> clazz) throws InterruptedException, IOException {
		return subscribe(topic, type, clazz, null);
	}
	
	
	@Override
	public <T> RosBridgeSubscriber<T> subscribe(String topic, String type, Class<T> clazz, Consumer<T> handler) throws InterruptedException, IOException {
				
		//create new subscription, and add it as a handler for the "publish" opcode
		IRosBridgeSubscriber<T> sub = new IRosBridgeSubscriber<T>(this, topic, type, handler, clazz);
		addCustomHandler(TOPIC_PUBLISH, sub);
		sub.subscribe();
		
		//return subscription
		return sub;
		
	}



	
	

	@Override
	public void unsubscribe(RosBridgeSubscriber<?> sub) throws InterruptedException, IOException {

		//remove subscription from handlers
		removeCustomHandler(TOPIC_PUBLISH, sub);
		
		//build message
		Map<String, String> msg = new HashMap<>();
		msg.put("op", TOPIC_UNSUBSCRIBE);
		msg.put("id", sub.getId());
		msg.put("topic", sub.getTopic());
		
		//send as json string
		connection.sendMessage(RealmSerialize.serialize(msg));
		
	}

	
	
	
	
	
	
	
	
	
	

	/*********************************************
	 * SERVICE CALLS
	 *********************************************/
	
	private Map<String, Object> buildCallMessage(String service, String id, List<?> args) {
		
		if (args == null) args = new ArrayList<>();
		
		//build message
		Map<String, Object> msg = new HashMap<>();
		msg.put("id", id);
		msg.put("op", SERVICE_CALL);
		msg.put("service", service);
		msg.put("args", args);
		
		return msg;
		
	}
	
	@Override
	public Future<Map<String, Object>> call(String service, List<?> args) throws InterruptedException, IOException {
		Map<String, Object> sample = new HashMap<>();
		return (Future<Map<String, Object>>) call(service, args, sample.getClass());

	}

	@Override
	public Future<Map<String, Object>> call(String service) throws InterruptedException, IOException {
		return call(service, new ArrayList<>());
	}

	@Override
	public void call(final String service, final List<?> args, final Consumer<Map<String, Object>> handler) throws InterruptedException, IOException {
		Map<String, Object> sample = new HashMap<>();
		call(service, args, (Class<Map<String, Object>>)sample.getClass(), handler);	
	}

	@Override
	public void call(String service, Consumer<Map<String, Object>> handler) throws InterruptedException, IOException {
		call(service, new ArrayList<Object>(), handler);
	}
	


	@Override
	public <T> Future<T> call(String service, List<?> args, Class<T> clazz) throws InterruptedException, IOException {
		
		//build message
		String id = connection.getNewMessageID();
		Map<String, Object> msg = buildCallMessage(service, id, args);
		
		IRosBridgeFutureCall<T> call = new IRosBridgeFutureCall<T>(service, id, clazz, new Consumer<RosBridgeCall<T>>() {

			@Override
			public void accept(RosBridgeCall<T> t) {
				removeCustomHandler(SERVICE_RETURN, t);
			}});
		
		addCustomHandler(SERVICE_RETURN, call);
		
		connection.sendMessage(RealmSerialize.serialize(msg));
		
		return RealmThread.getThreadPool().submit(call);
	}


	@Override
	public <T> Future<T> call(String service, Class<T> clazz) throws InterruptedException, IOException {
		return call(service, new ArrayList<>(), clazz);
	}


	@Override
	public <T> void call(String service, List<?> args, Class<T> clazz, Consumer<T> handler) throws InterruptedException, IOException {
		
		//build message
		String id = connection.getNewMessageID();
		Map<String, Object> msg = buildCallMessage(service, id, args);
		
		RosBridgeCall<T> call = new IRosBridgeCallbackCall<T>(service, id, clazz, handler, new Consumer<RosBridgeCall<T>>() {

			@Override
			public void accept(RosBridgeCall<T> t) {
				removeCustomHandler(SERVICE_RETURN, t);
			}});
		
		addCustomHandler(SERVICE_RETURN, call);
		
		connection.sendMessage(RealmSerialize.serialize(msg));	}


	@Override
	public <T> void call(String service, Class<T> clazz, Consumer<T> handler) throws InterruptedException, IOException {
		call(service, new ArrayList<>(), clazz, handler);
	}

	
	
	
	
	
	

	/*********************************************
	 * TOPIC PRODUCER - PUBLICATIONS
	 *********************************************/	
	@Override
	public RosBridgePublisher advertise(String topic, String type) throws InterruptedException, IOException {
		
		//make sure we aren't already publishing to this topic
		for (RosBridgePublisher pub : publishers) {
			if (topic.equals(pub.getTopic())) throw new IllegalArgumentException("Topic is already being published to");
		}
		
		//create a new publisher
		IRosBridgePublisher pub = new IRosBridgePublisher(this, topic, type);
		publishers.add(pub);
		pub.advertise();

		return pub;
		
	}
	
	@Override
	public <T> RosBridgePublisher advertise(String topic, Class<T> cls) throws InterruptedException, IOException {
		if (cls.getDeclaredAnnotation(RosType.class) == null) throw new IllegalArgumentException("No RosType annotation found");
		return advertise(topic, typeFromClass(cls));
	}
	
	
	@Override
	public void unadvertise(RosBridgePublisher publisher) throws InterruptedException, IOException {
		
		//make sure this is a valid publisher, then remove it from the set of valid publishers
		if (!publishers.contains(publisher)) throw new IllegalArgumentException("No such Publisher");
		publishers.remove(publisher);
		
		//send an unadvertise message
		Map<String, Object> message = new HashMap<>();
		String id = connection.getNewMessageID();
		message.put("id", id);
		message.put("op", TOPIC_UNADVERTISE);
		message.put("topic", publisher.getTopic());
		
		connection.sendMessage(RealmSerialize.serialize(message));
		
	}

	
	@Override
	public void publishOnce(String topic, String type, Object message) throws InterruptedException, IOException {
		RosBridgePublisher pub = advertise(topic, type);
		pub.publish(message);
		pub.close();
	}


	public static String typeFromClass(Class<?> cls) {
		RosType t = cls.getDeclaredAnnotation(RosType.class);
		if (t == null) return null;
		String type = t.value();
		if (type.equals("")) return null;
		return type;
	}



	@Override
	public void close() throws IOException {
		connection.close();
	}


}
