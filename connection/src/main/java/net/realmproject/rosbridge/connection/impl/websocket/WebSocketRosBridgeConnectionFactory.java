package net.realmproject.rosbridge.connection.impl.websocket;


import java.net.URI;

import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.realmproject.rosbridge.connection.RosBridgeConnection;
import net.realmproject.rosbridge.connection.RosBridgeConnectionException;
import net.realmproject.rosbridge.connection.RosBridgeConnectionFactory;


public class WebSocketRosBridgeConnectionFactory implements RosBridgeConnectionFactory {

    private String server;
    private int buffer;

    private Log log = LogFactory.getLog(getClass());

    public WebSocketRosBridgeConnectionFactory(String server, int bufferSize) {
        this.server = server;
        this.buffer = bufferSize;
    }

    @Override
    public RosBridgeConnection connect() throws RosBridgeConnectionException {

        WebSocketRosBridgeConnection conn;

        try {
            conn = new WebSocketRosBridgeConnection();

            log.warn(server);
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.setDefaultMaxBinaryMessageBufferSize(buffer * 1024);
            container.setDefaultMaxTextMessageBufferSize(buffer * 1024);
            container.setDefaultMaxSessionIdleTimeout(0);
            // container.setAsyncSendTimeout(5000);
            container.connectToServer(conn, URI.create(server));
        }
        catch (Exception e) {
            throw new RosBridgeConnectionException(e);
        }

        return conn;

    }

}
