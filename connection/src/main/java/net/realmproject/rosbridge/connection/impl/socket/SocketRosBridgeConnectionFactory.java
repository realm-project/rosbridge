package net.realmproject.rosbridge.connection.impl.socket;


import net.realmproject.rosbridge.connection.RosBridgeConnection;
import net.realmproject.rosbridge.connection.RosBridgeConnectionException;
import net.realmproject.rosbridge.connection.RosBridgeConnectionFactory;


public class SocketRosBridgeConnectionFactory implements RosBridgeConnectionFactory {

    private String address;
    private int port;

    public SocketRosBridgeConnectionFactory(String address, int port) {
        this.address = address;
        this.port = port;
    }

    @Override
    public RosBridgeConnection connect() throws RosBridgeConnectionException {
        SocketRosBridgeConnection socket = new SocketRosBridgeConnection();
        socket.connect(address, port);
        return socket;
    }

}
