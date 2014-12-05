package net.realmproject.rosbridge.client.client.call;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Consumer;

import net.realmproject.rosbridge.client.connection.RosBridgeMessage;

/**
 * A RosBridgeCall is the result of making a ROS service call. It is never seen
 * directly by users, it is presented as a {@link Future}.
 * It waits for a response to the service call, and provides the 'values' 
 * section of the message as a {@link List} of Objects
 * @author nathaniel
 *
 */

public interface RosBridgeCall<T> extends Consumer<RosBridgeMessage>
{

}
