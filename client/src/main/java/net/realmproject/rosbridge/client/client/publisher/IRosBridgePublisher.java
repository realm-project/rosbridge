package net.realmproject.rosbridge.client.client.publisher;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.realmproject.rosbridge.client.client.IRosBridgeClient;
import net.realmproject.rosbridge.util.RosBridgeSerialize;


public class IRosBridgePublisher implements RosBridgePublisher {

    private String topic;
    private String type;
    private boolean closed = false;

    private IRosBridgeClient client;

    public IRosBridgePublisher(IRosBridgeClient client, String topic, String type) {
        this.topic = topic;
        this.type = type;
        this.client = client;
    }

    public void advertise() throws InterruptedException, IOException {
        // send a message advertising our intent to publish
        Map<String, String> message = new HashMap<>();
        String id = client.getNewMessageID();
        message.put("id", id);
        message.put("op", IRosBridgeClient.TOPIC_ADVERTISE);
        message.put("type", type);
        message.put("topic", topic);

        client.sendMessage(RosBridgeSerialize.serialize(message));

    }

    @Override
    public synchronized void publish(Object contents) throws InterruptedException, IOException {
        if (closed) throw new IllegalStateException("Publisher is closed");

        Map<String, Object> messageBody = new HashMap<>();
        String id = client.getNewMessageID();
        messageBody.put("id", id);
        messageBody.put("op", IRosBridgeClient.TOPIC_PUBLISH);
        messageBody.put("topic", topic);
        messageBody.put("msg", contents);

        String strmsg = RosBridgeSerialize.serialize(messageBody);
        client.sendMessage(strmsg);

    }

    @Override
    public synchronized void close() throws IOException {
        closed = true;
        try {
            client.unadvertise(this);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String getType() {
        return type;
    }

}
