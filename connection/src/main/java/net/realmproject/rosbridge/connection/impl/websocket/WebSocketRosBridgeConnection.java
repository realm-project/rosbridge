package net.realmproject.rosbridge.connection.impl.websocket;


import java.io.IOException;
import java.util.Base64;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.realmproject.rosbridge.connection.RosBridgeMessage;
import net.realmproject.rosbridge.connection.impl.AbstractRosBridgeConnection;
import net.realmproject.rosbridge.connection.impl.IRosBridgeMessage;


@ClientEndpoint
public class WebSocketRosBridgeConnection extends AbstractRosBridgeConnection {

    private static int socketCount = 0;
    private int socketNumber = 0;

    private Log log = LogFactory.getLog(getClass());

    protected Session session;

    private String server;
    private int bufferSize;

    public WebSocketRosBridgeConnection() throws InterruptedException, IOException {}

    @OnOpen
    public void onOpen(Session session) throws InterruptedException {
        synchronized (WebSocketRosBridgeConnection.class) {
            socketNumber = socketCount++;
        }

        this.session = session;
        if (!session.isOpen()) {
            log.error(this + " Connection Failed. New Websocket is not Open");
        } else {
            log.info(this + " Connection Successful");
        }

    }

    @OnMessage
    public void onBinaryMessage(byte[] message) {
        log.trace(this + " Received Binary:\n" + Base64.getEncoder().encode(message));
    }

    @OnMessage
    public void onMessage(String message) {

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

    @OnClose
    public void onClose(Session s, CloseReason reason) {
        log.info("Closed " + this + " - " + s.toString());
        if (reason.getReasonPhrase() != null && reason.getReasonPhrase().length() > 0) {
            log.info(reason.getReasonPhrase());
        }
    }

    @OnError
    public void onError(Throwable t) {
        log.error("Received Throwble in onError for " + this, t);
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    public synchronized void sendMessage(String message) throws IOException, InterruptedException {

        if (session == null) throw new IOException("Not Connected");

        log.trace(this + " Sending Message:\n" + message);

        synchronized (WebSocketRosBridgeConnection.class) {
            session.getBasicRemote().sendText(message);
        }

    }

    @Override
    public synchronized void close() throws IOException {
        if (session == null) return;
        if (session.isOpen()) {
            session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Done"));
        }
        session = null;
    }

    public String toString() {
        return "WebSocket #" + socketNumber;
    }

}
