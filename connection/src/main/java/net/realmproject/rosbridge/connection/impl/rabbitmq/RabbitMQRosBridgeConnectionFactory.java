package net.realmproject.rosbridge.connection.impl.rabbitmq;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

import net.realmproject.rosbridge.connection.RosBridgeConnection;
import net.realmproject.rosbridge.connection.RosBridgeConnectionException;
import net.realmproject.rosbridge.connection.RosBridgeConnectionFactory;


public class RabbitMQRosBridgeConnectionFactory implements RosBridgeConnectionFactory {

    private String server;

    public RabbitMQRosBridgeConnectionFactory(String server, int messageSize) {
        this.server = server;
    }

    @Override
    public RosBridgeConnection connect() throws RosBridgeConnectionException {
        try {
            return new RabbitMQRosBridgeConnection(server);
        }
        catch (IOException | TimeoutException e) {
            throw new RosBridgeConnectionException(e);
        }
    }

}
