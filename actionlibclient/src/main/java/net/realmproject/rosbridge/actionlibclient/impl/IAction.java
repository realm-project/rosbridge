package net.realmproject.rosbridge.actionlibclient.impl;


import java.io.IOException;
import java.util.Date;
import java.util.function.Consumer;

import net.realmproject.rosbridge.actionlibclient.Action;
import net.realmproject.rosbridge.actionlibclient.Goal;
import net.realmproject.rosbridge.actionlibclient.datatypes.GoalID;
import net.realmproject.rosbridge.client.client.RosBridgeClient;
import net.realmproject.rosbridge.client.client.RosBridgeClientFactory;
import net.realmproject.rosbridge.client.datatypes.standard.Timestamp;
import net.realmproject.rosbridge.connection.RosBridgeConnectionException;


public class IAction<T> implements Action<T> {

    private String baseTopic;
    private ActionMessageFormats types = new ActionMessageFormats();
    private RosBridgeClientFactory factory;
    private RosBridgeClient internalClient;

    private Class<T> clazz;

    public IAction(RosBridgeClientFactory factory, String baseTopic, String baseType, Class<T> clazz)
            throws RosBridgeConnectionException {

        if (!baseTopic.endsWith("/")) baseTopic += "/";

        this.factory = factory;
        this.internalClient = factory.connect();
        this.baseTopic = baseTopic;
        this.clazz = clazz;

        types.goal = baseType + "ActionGoal";
        types.feedback = baseType + "ActionFeedback";
        types.result = baseType + "ActionResult";
    }

    @Override
    public synchronized Goal<T> createGoal() throws RosBridgeConnectionException {

        Goal<T> goal = new IGoal<T>(factory.connect(), baseTopic, types, clazz, goalCompletedCommand());
        // goals.add(goal);
        return goal;

    }

    @Override
    public void cancelAll() throws InterruptedException, IOException {
        internalClient.publishOnce(baseTopic + "cancel", "actionlib/GoalID", new GoalID());
    }

    @Override
    public void cancelBefore(Date before) throws InterruptedException, IOException {
        GoalID goalid = new GoalID();
        goalid.stamp = Timestamp.fromDate(before);
        internalClient.publishOnce(baseTopic + "cancel", "actionlib/GoalID", goalid);
    }

    private Consumer<Goal<T>> goalCompletedCommand() {
        return goal -> {}; // goals.remove(goal);
    }

    @Override
    public void close() throws IOException {
        internalClient.close();
    }

}
