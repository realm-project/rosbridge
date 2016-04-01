package net.realmproject.rosbridge.connection;

public interface RosBridgeConnectionFactory {

    RosBridgeConnection connect() throws RosBridgeConnectionException;
}
