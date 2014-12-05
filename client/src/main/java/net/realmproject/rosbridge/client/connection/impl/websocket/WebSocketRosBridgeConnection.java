package net.realmproject.rosbridge.client.connection.impl.websocket;


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

import net.realmproject.rosbridge.client.connection.RosBridgeConnection;
import net.realmproject.rosbridge.client.connection.RosBridgeMessage;
import net.realmproject.rosbridge.client.connection.impl.AbstractRosBridgeConnection;
import net.realmproject.rosbridge.client.connection.impl.IRosBridgeMessage;
import net.realmproject.util.RealmLog;

import org.apache.commons.logging.Log;


@ClientEndpoint
public class WebSocketRosBridgeConnection extends AbstractRosBridgeConnection {

    private Log log = RealmLog.getLog();

    protected Session session;
    private long idCounter = 0;
    private String server;

    // lock used for sending messages, which does not
    // become available until a connection has been
    // made and a Session object is available
    private Semaphore sendSemaphore = new Semaphore(1);


    public WebSocketRosBridgeConnection(String server) throws InterruptedException, IOException {
        this.server = server;
        connect(server);
    }

    /*
     * public WebSocketRosBridgeConnection(String server, long delay) throws
     * InterruptedException { this.server = server; connectAsync(server, delay);
     * }
     */
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



    public RosBridgeConnection newConnection() throws InterruptedException, IOException {
        return new WebSocketRosBridgeConnection(server);
    }


    /*
     * //creates a connection asynchronously, delaying first, to allow time for
     * bean/service creation, etc private void connectAsync(final String uri,
     * final long delay) {
     * 
     * RealmThread.getThreadPool().submit(() -> { Thread.sleep(delay);
     * connect(uri); return null; });
     * 
     * }
     */
    // creates a connection synchronously
    private synchronized void connect(final String uri) throws IOException {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        container.setDefaultMaxBinaryMessageBufferSize(32 * 1024 * 1024);
        container.setDefaultMaxTextMessageBufferSize(32 * 1024 * 1024);
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
