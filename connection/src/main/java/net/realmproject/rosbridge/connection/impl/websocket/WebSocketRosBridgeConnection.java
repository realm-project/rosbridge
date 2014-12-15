package net.realmproject.rosbridge.connection.impl.websocket;


import java.io.IOException;
import java.net.URI;
import java.util.concurrent.Semaphore;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;

import net.realmproject.rosbridge.connection.RosBridgeMessage;
import net.realmproject.rosbridge.connection.impl.AbstractRosBridgeConnection;
import net.realmproject.rosbridge.connection.impl.IRosBridgeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


@ClientEndpoint
public class WebSocketRosBridgeConnection extends AbstractRosBridgeConnection {

    private Log log = LogFactory.getLog(getClass());

    protected Session session;
    private long idCounter = 0;

    // lock used for sending messages, which does not
    // become available until a connection has been
    // made and a Session object is available
    private Semaphore sendSemaphore = new Semaphore(1);


    public WebSocketRosBridgeConnection(String server, int bufferSize) throws InterruptedException, IOException {
        connect(server, bufferSize);
    }

    
    @OnOpen
    public void onOpen(Session session) throws InterruptedException {
        this.session = session;
    }

    @OnMessage
    public void onMessage(String message) {

        try {
            RosBridgeMessage deserailzed = new IRosBridgeMessage(message);
            notifyMessageListeners(deserailzed);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClose
    public void onClose(Session s, CloseReason reason) {
        log.info("Closed Session " + s.toString());
        log.info(reason.getReasonPhrase());
    }

    @OnError
    public void onError(Throwable t) {
        log.error("Received Throwble in onError", t);
    }

    @Override
    public String getNewMessageID() {
        return "" + idCounter++;
    }

    @Override
    public synchronized void sendMessage(String message) throws InterruptedException, IOException {

        if (session == null) throw new IOException("Not Connected");

        sendSemaphore.acquire();
        session.getBasicRemote().sendText(message);
        sendSemaphore.release();

    }



    // creates a connection synchronously
    private synchronized void connect(final String uri, int bufferSize) throws IOException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setDefaultMaxBinaryMessageBufferSize(bufferSize * 1024);
        container.setDefaultMaxTextMessageBufferSize(bufferSize * 1024);
        try {
            container.connectToServer(this, URI.create(uri));
        }
        catch (DeploymentException e) {
            throw new IOException(e);
        }
    }

    @Override
    public synchronized void close() throws IOException {
        if (session == null) return;
        session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "Done"));
        session = null;
    }


}
