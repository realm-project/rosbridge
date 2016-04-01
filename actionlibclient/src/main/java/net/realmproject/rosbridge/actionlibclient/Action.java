package net.realmproject.rosbridge.actionlibclient;


import java.io.Closeable;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeoutException;

import net.realmproject.rosbridge.connection.RosBridgeConnectionException;


public interface Action<T> extends Closeable {

    /**
     * Create a new {@link Goal} for this Action
     * 
     * @return
     * @throws InterruptedException
     * @throws IOException
     * @throws TimeoutException
     * @throws RosBridgeConnectionException
     */
    Goal<T> createGoal() throws RosBridgeConnectionException;

    /**
     * Cancels all goals for this action
     * 
     * @throws InterruptedException
     * @throws IOException
     */
    void cancelAll() throws InterruptedException, IOException;

    /**
     * Cancels all goals for this action which were submitted before a given
     * time, as time-stamped by the ActionServer.
     * 
     * @throws InterruptedException
     * @throws IOException
     */
    void cancelBefore(Date before) throws InterruptedException, IOException;

}