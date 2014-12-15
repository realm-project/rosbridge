package net.realmproject.rosbridge.client.client;


import java.io.IOException;

import net.realmproject.rosbridge.connection.RosBridgeConnectionFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class IRosBridgeClientFactory implements RosBridgeClientFactory {

    RosBridgeConnectionFactory factory;
    private Log log = LogFactory.getLog(getClass());

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
