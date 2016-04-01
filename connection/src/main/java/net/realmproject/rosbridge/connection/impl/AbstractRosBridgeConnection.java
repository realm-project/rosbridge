package net.realmproject.rosbridge.connection.impl;


import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.realmproject.rosbridge.connection.RosBridgeConnection;
import net.realmproject.rosbridge.connection.RosBridgeMessage;
import net.realmproject.rosbridge.connection.RosBridgeMessageListener;


public abstract class AbstractRosBridgeConnection implements RosBridgeConnection {

    private List<RosBridgeMessageListener> listeners = new ArrayList<>();

    private Log log = LogFactory.getLog(getClass());

    private long idCounter = 0;

    @Override
    public void addMessageListener(RosBridgeMessageListener listener) {
        listeners.add(listener);
    }

    @Override
    public void clearMessageListeners() {
        listeners.clear();
    }

    @Override
    public void removeMessageListener(RosBridgeMessageListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void notifyMessageListeners(RosBridgeMessage message) {
        for (RosBridgeMessageListener listener : listeners) {
            log.trace("Event Notification from " + this + " to " + listener);
            listener.handleMessage(message);
            log.trace("Notification of " + listener + " Complete");
        }
    }

    @Override
    public String getNewMessageID() {
        return "" + idCounter++;
    }

}
