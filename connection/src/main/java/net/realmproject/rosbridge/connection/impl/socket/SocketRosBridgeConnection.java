package net.realmproject.rosbridge.connection.impl.socket;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.realmproject.dcm.util.DCMThreadPool;
import net.realmproject.rosbridge.connection.RosBridgeConnectionException;
import net.realmproject.rosbridge.connection.RosBridgeMessage;
import net.realmproject.rosbridge.connection.impl.AbstractRosBridgeConnection;
import net.realmproject.rosbridge.connection.impl.IRosBridgeMessage;
import net.realmproject.rosbridge.connection.impl.websocket.WebSocketRosBridgeConnection;


public class SocketRosBridgeConnection extends AbstractRosBridgeConnection {

    private static int socketCount = 0;
    private int socketNumber = 0;

    private Log log = LogFactory.getLog(getClass());

    Socket socket;
    BufferedReader socketReader;

    public SocketRosBridgeConnection() {}

    public void connect(String address, int port) throws RosBridgeConnectionException {
        try {
            socket = new Socket(address, port);
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (Exception e) {
            throw new RosBridgeConnectionException("Unable to Connect", e);
        }
        listen();
    }

    private void listen() {
        DCMThreadPool.getPool().submit(() -> {
            // char[] buf = new char[65536];

            while (socket != null) {
                try {
                    onMessage(socketReader.readLine());
                }
                catch (Exception e) {
                    log.error("Could not read from socket", e);
                    try {
                        close();
                    }
                    catch (Exception e1) {
                        log.trace(e1);
                    }
                }
            }
        });
    }

    private void onMessage(String message) {
        log.trace(this + " Received Message:\n" + message);

        try {
            RosBridgeMessage deserailzed = new IRosBridgeMessage(message);
            notifyMessageListeners(deserailzed);
        }
        catch (Exception e) {
            log.error(e);
        }

        log.trace(this + " Receipt Completed");
    }

    @Override
    public void sendMessage(String message) throws InterruptedException, IOException {
        if (socket == null) throw new IOException("Not Connected");

        log.trace(this + " Sending Message:\n" + message);

        synchronized (WebSocketRosBridgeConnection.class) {
            socket.getOutputStream().write(message.getBytes());
        }
    }

    @Override
    public void close() throws IOException {
        socket.close();
        socket = null;
        socketReader = null;
    }

}
