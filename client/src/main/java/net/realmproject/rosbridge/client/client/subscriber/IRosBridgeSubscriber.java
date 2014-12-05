package net.realmproject.rosbridge.client.client.subscriber;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import net.realmproject.rosbridge.client.client.IRosBridgeClient;
import net.realmproject.rosbridge.client.connection.RosBridgeMessage;
import net.realmproject.util.RealmSerialize;

public class IRosBridgeSubscriber<T> implements RosBridgeSubscriber<T>
{
	
	String topic;
	String type;
	String id;
	
	Consumer<T> handler;
	T lastMessage = null;
	Date lastMessageDate = null;
	Class<T> clazz = null;
	
	IRosBridgeClient client;

	public IRosBridgeSubscriber(IRosBridgeClient client, String topic, String type, Class<T> clazz) {
		this(client, topic, type, null, clazz);
	}
	
	public IRosBridgeSubscriber(IRosBridgeClient client, String topic, String type, Consumer<T> handler, Class<T> clazz) {
		this.topic = topic;
		this.handler = handler;
		this.clazz = clazz;
		this.client = client;
		this.type = type;
	}
	
	
	public void subscribe() throws InterruptedException, IOException {
		//build message
		Map<String, String> msg = new HashMap<>();
		id = client.getNewMessageID();
		msg.put("op", IRosBridgeClient.TOPIC_SUBSCRIBE);
		msg.put("id", id);
		msg.put("topic", topic);
		if (type != null) { msg.put("type", type); }
		
		//send message as json string
		client.sendMessage(RealmSerialize.serialize(msg));
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getTopic() {
		return topic;
	}
	
	@Override
	public void close() throws IOException {
		try {
			client.unsubscribe(this);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public void accept(RosBridgeMessage msg) {
		
		switch(msg.getOpcode()) {
			//case "status": onPublish(msg); return;
			case "publish": onPublish(msg); return;
		}
	}
	


	private synchronized void onPublish(RosBridgeMessage msg) {
		String msgTopic = (String)msg.getArguments().get("topic");
		if (!topic.equals(msgTopic)) return;
		
		//pass to optional handler
		Object objMessage = msg.getArguments().get("msg");
		T message;
		
		if (clazz != null) {
			message = RealmSerialize.convertMessage(objMessage, clazz);
		} else {
			message = (T)objMessage;
		}
		
		if (handler != null) handler.accept(message);

		//set as last message
		lastMessage = message;
		lastMessageDate = new Date();
		
		//wake anyone waiting on this object.
		notifyAll();
	}


	@Override
	public T getLastMessage() {
		return lastMessage;
	}

	@Override
	public void setHandler(Consumer<T> handler) {
		this.handler = handler;
	}

	@Override
	public Date getLastMessageDate() {
		return lastMessageDate;
	}
	
}
