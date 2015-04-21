package net.realmproject.rosbridge.connection.impl.websocket;


import java.io.IOException;

import net.realmproject.rosbridge.connection.RosBridgeConnection;
import net.realmproject.rosbridge.connection.RosBridgeConnectionFactory;


public class WebSocketRosBridgeConnectionFactory implements RosBridgeConnectionFactory {

    private String server;
    private int buffer;

    public WebSocketRosBridgeConnectionFactory(String server, int bufferSize) {
        this.server = server;
        this.buffer = bufferSize;
    }

    @Override
    public RosBridgeConnection connect() throws InterruptedException, IOException {
        return new WebSocketRosBridgeConnection(server, buffer);
    }

}
