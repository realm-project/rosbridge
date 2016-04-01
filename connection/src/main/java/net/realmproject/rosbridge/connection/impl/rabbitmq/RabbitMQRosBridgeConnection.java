package net.realmproject.rosbridge.connection.impl.rabbitmq;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import net.realmproject.dcm.util.DCMInterrupt;
import net.realmproject.dcm.util.DCMThreadPool;
import net.realmproject.rosbridge.connection.RosBridgeMessage;
import net.realmproject.rosbridge.connection.impl.AbstractRosBridgeConnection;
import net.realmproject.rosbridge.connection.impl.IRosBridgeMessage;


public class RabbitMQRosBridgeConnection extends AbstractRosBridgeConnection {

    private Connection connection;
    private Channel channel;
    private boolean closed = false;
    private Consumer consumer;
    private Log log = LogFactory.getLog(getClass());

    public RabbitMQRosBridgeConnection(String server) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("guest");
        factory.setPassword("feliscatus");
        factory.setHost(server);
        connection = factory.newConnection();
        channel = connection.createChannel();

        channel.queueDeclare("ros-out", false, false, true, null);

        consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,
                    byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                onMessage(message);
            }
        };

        listen();

    }

    private void listen() {

        DCMThreadPool.getPool().submit(() -> {
            while (!closed) {
                consume();
            }
        });
    }

    private synchronized void consume() {
        DCMInterrupt.handle(() -> {
            channel.basicConsume("ros-out", true, consumer);
        });
    }

    private void onMessage(String message) {
        log.trace(this + " Received Message:\n" + message);

        try {
            RosBridgeMessage deserailzed = new IRosBridgeMessage(message);
            notifyMessageListeners(deserailzed);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        log.trace(this + " Receipt Completed");
    }

    @Override
    public synchronized void close() throws IOException {
        closed = true;
        try {
            channel.close();
        }
        catch (TimeoutException e) {
            e.printStackTrace();
        }
        connection.close();
    }

    @Override
    public synchronized void sendMessage(String message) throws InterruptedException, IOException {
        channel.basicPublish("", "ros-in", null, message.getBytes());
    }

}
