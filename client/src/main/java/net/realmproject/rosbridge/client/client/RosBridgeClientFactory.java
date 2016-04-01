package net.realmproject.rosbridge.client.client;


import net.realmproject.rosbridge.connection.RosBridgeConnectionException;


public interface RosBridgeClientFactory {

    RosBridgeClient connect() throws RosBridgeConnectionException;
}
