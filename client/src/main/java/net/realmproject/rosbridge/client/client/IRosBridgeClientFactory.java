package net.realmproject.rosbridge.client.client;


import java.io.IOException;

import net.realmproject.rosbridge.client.connection.RosBridgeConnectionFactory;
import net.realmproject.util.RealmLog;

import org.apache.commons.logging.Log;


public class IRosBridgeClientFactory implements RosBridgeClientFactory {

    RosBridgeConnectionFactory factory;
    private Log log = RealmLog.getLog();

    public IRosBridgeClientFactory(RosBridgeConnectionFactory factory) {
        this.factory = factory;
    }

    @Override
    public RosBridgeClient connect() throws InterruptedException, IOException {
        StackTraceElement ste = Thread.currentThread().getStackTrace()[2];
        log.info("Creating New WebSocket Connection for " + ste.getClassName() + "." + ste.getMethodName() + ":"
                + ste.getLineNumber() + "...");

        return new IRosBridgeClient(factory.connect());
    }

}
