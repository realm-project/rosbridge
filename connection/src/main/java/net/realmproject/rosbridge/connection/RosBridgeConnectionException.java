package net.realmproject.rosbridge.connection;


public class RosBridgeConnectionException extends Exception {

    public RosBridgeConnectionException(String message, Throwable exception) {
        super(message, exception);
    }

    public RosBridgeConnectionException(Throwable exception) {
        super(exception);
    }

}
