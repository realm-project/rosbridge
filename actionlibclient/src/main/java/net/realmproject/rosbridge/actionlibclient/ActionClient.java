package net.realmproject.rosbridge.actionlibclient;


import java.io.IOException;
import java.util.concurrent.TimeoutException;

import net.realmproject.rosbridge.actionlibclient.impl.IAction;
import net.realmproject.rosbridge.client.client.IRosBridgeClient;
import net.realmproject.rosbridge.client.client.RosBridgeClientFactory;
import net.realmproject.rosbridge.client.datatypes.RosType;
import net.realmproject.rosbridge.client.datatypes.geometry.Point;
import net.realmproject.rosbridge.connection.RosBridgeConnectionException;


public class ActionClient {

    RosBridgeClientFactory factory;

    public ActionClient(RosBridgeClientFactory factory) {
        this.factory = factory;
    }

    /**
     * Create a representation of an {@link Action} where the message types are
     * read from a {@link RosType} annotated class. Passing a {@link Class}
     * allows {@link Goal}s created by this Action to return an object of the
     * correct type, rather than a generic form. The Action accepts a base url
     * which is the common topic prefix (eg /basetopic/goal, /basetopic/result,
     * ...)
     * 
     * @param baseTopic
     *            The base topic for this action
     * @param clazz
     *            a {@link RosType} annocated class of the Java object
     *            representing the message format
     * @throws IOException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public <T> Action<T> create(String baseTopic, Class<T> clazz) throws RosBridgeConnectionException {
        return new IAction<>(factory, baseTopic, IRosBridgeClient.typeFromClass(clazz), clazz);
    }

    /**
     * Create a representation of an {@link Action} where the actionlib message
     * types are not related to the provided class. For example, an action
     * 'foo/set_point' which uses a {@link Point} message as it's payload will
     * communicate over RosBridge using a 'foo_msgs/SetPointActionGoal' message
     * format. In this case, the base type should be 'foo_msgs/SetPoint' <br/>
     * <br/>
     * The Action accepts a base topic which is the common topic prefix (eg
     * /basetopic/goal, /basetopic/result, ...)
     * 
     * @throws IOException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public <T> Action<T> create(String baseTopic, String baseType, Class<T> clazz) throws RosBridgeConnectionException {
        return new IAction<>(factory, baseTopic, baseType, clazz);
    }

    /**
     * Create a representation of an {@link Action} for the given base type. For
     * example, an action 'foo/set_point' which uses a {@link Point} message as
     * it's payload will communicate over RosBridge using a
     * 'foo_msgs/SetPointActionGoal' message format. In this case, the base type
     * should be 'foo_msgs/SetPoint' <br/>
     * <br/>
     * The Action accepts a base topic which is the common topic prefix (eg
     * /basetopic/goal, /basetopic/result, ...)
     * 
     * @throws IOException
     * @throws InterruptedException
     * @throws TimeoutException
     */
    public Action<Object> create(String baseTopic, String baseType) throws RosBridgeConnectionException {
        return new IAction<>(factory, baseTopic, baseType, Object.class);
    }

}
