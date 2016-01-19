package net.realmproject.rosbridge.connection;


import java.io.Closeable;
import java.io.IOException;


/**
 * A ROS/JSON Connection operates at the level of Strings/JSON formatted
 * messages being sent across some implementation-specific connection. It is not
 * intended to have any understanding of the content of those messages.
 * 
 * @author Nathaniel Sherry
 *
 */
public interface RosBridgeConnection extends Closeable {

    void sendMessage(String message) throws InterruptedException, IOException;

    String getNewMessageID();

    void addMessageListener(RosBridgeMessageListener listener);

    void clearMessageListeners();

    void removeMessageListener(RosBridgeMessageListener listener);

    void notifyMessageListeners(RosBridgeMessage message);

}
