package net.realmproject.rosbridge.actionlibclient.impl;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.Future;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.realmproject.rosbridge.actionlibclient.Goal;
import net.realmproject.rosbridge.actionlibclient.datatypes.Feedback;
import net.realmproject.rosbridge.actionlibclient.datatypes.GoalID;
import net.realmproject.rosbridge.actionlibclient.datatypes.GoalStatus;
import net.realmproject.rosbridge.actionlibclient.datatypes.Result;
import net.realmproject.rosbridge.actionlibclient.datatypes.GoalStatus.GoalState;
import net.realmproject.rosbridge.actionlibclient.impl.datatypes.InternalFeedbackMessage;
import net.realmproject.rosbridge.actionlibclient.impl.datatypes.InternalGoalMessage;
import net.realmproject.rosbridge.actionlibclient.impl.datatypes.InternalResultMessage;
import net.realmproject.rosbridge.actionlibclient.impl.datatypes.InternalStatusMessage;
import net.realmproject.rosbridge.client.client.RosBridgeClient;
import net.realmproject.rosbridge.client.client.subscriber.RosBridgeSubscriber;
import net.realmproject.util.RealmLog;
import net.realmproject.util.RealmSerialize;
import net.realmproject.util.RealmThread;

import org.apache.commons.logging.Log;


public class IGoal<T> implements Goal<T> {

    protected final Log log = RealmLog.getLog();

    RosBridgeClient client;
    String idString;

    private String baseTopic;
    private ActionMessageFormats types = new ActionMessageFormats();
    private Consumer<Goal<T>> completedCommand;


    private RosBridgeSubscriber<InternalResultMessage> resultSub;
    private RosBridgeSubscriber<InternalStatusMessage> statusSub;
    private RosBridgeSubscriber<InternalFeedbackMessage> feedbackSub;

    private Consumer<GoalStatus> userStatusHandler;
    private Consumer<Feedback<T>> userFeedbackHandler;
    private Consumer<Result<T>> userResultHandler;
    private Runnable userCompletionHandler;

    private Class<T> clazz;
    private boolean closed = false;


    // private ClientGoalState state;
    private GoalState status;
    private Optional<Result<T>> result = Optional.empty();
    private boolean handledCompletion = false;

    // generate unique goal ids
    // TODO: Do these have to be unique accross all actionlib clients?
    private final static Supplier<String> goalIDGenerator = new Supplier<String>() {

        private int counter = 0;

        @Override
        public String get() {
            return "GoalID_" + counter++ + "_unique_" + UUID.randomUUID().toString() + "_";
        }
    };


    public IGoal(RosBridgeClient client, String baseTopic, ActionMessageFormats types, Class<T> clazz,
            Consumer<Goal<T>> completedCommand) throws InterruptedException {

        this.client = client;
        this.types = types;
        this.baseTopic = baseTopic;
        this.completedCommand = completedCommand;
        this.clazz = clazz;

        idString = getGoalID();

    }


    @Override
    public String getId() {
        return idString;
    }

    @Override
    public GoalState getState() {
        return status;
    }

    @Override
    public Future<Optional<Result<T>>> getResult() {

        return RealmThread.getThreadPool().submit(() -> {

            // this and handleResult/handleStatus are synchronized; it will
            // notify when anything happens
                synchronized (IGoal.this) {
                    while (true) {
                        if (isComplete()) return result;
                        wait();
                    }
                }

            });

    }

    @Override
    public void submit(T goalData) throws InterruptedException, IOException {
        submit(null, goalData);
    }

    @Override
    public void submit(String field, T goalData) throws InterruptedException, IOException {


        // /////////////////////////////////////
        status = null;
        // /////////////////////////////////////


        // start listening to feedback/status for this goalid. It is important
        // to do this *before* we
        // send the goal, so that we don't miss a fast status change
        feedbackSub = client.subscribe(baseTopic + "feedback", types.feedback, InternalFeedbackMessage.class, msg -> {
            Feedback<T> feedback = unpackFeedback(msg);
            if (!isFeedbackForUs(feedback)) return;
            handleFeedback(feedback);
        });

        statusSub = client.subscribe(baseTopic + "status", InternalStatusMessage.class, msg -> {
            GoalStatus status = findOurStatus(msg);
            if (status == null) return;
            handleStatus(status);
        });

        resultSub = client.subscribe(baseTopic + "result", types.result, InternalResultMessage.class, msg -> {
            Result<T> result = unpackResult(msg);
            if (!isResultForUs(result)) return;
            handleResult(result);
        });


        // send the goal
        if (field != null) {
            Map<String, T> wrap = new HashMap<>();
            wrap.put(field, goalData);
            client.publishOnce(baseTopic + "goal", types.goal, new InternalGoalMessage(wrap, new GoalID(getId())));
        } else {
            client.publishOnce(baseTopic + "goal", types.goal, new InternalGoalMessage(goalData, new GoalID(getId())));
        }

    }

    @Override
    public void cancel() throws InterruptedException, IOException {
        client.publishOnce(baseTopic + "cancel", "actionlib/GoalID", new GoalID(getId()));
        // TODO: make this a synchronous call returning boolean on
        // success/failure to abort?
    }



    @Override
    public void setStatusHandler(Consumer<GoalStatus> statusHandler) {
        userStatusHandler = statusHandler;
    }

    @Override
    public void setResultHandler(Consumer<Result<T>> resultHandler) {
        userResultHandler = resultHandler;
    }

    @Override
    public void setFeedbackHandler(Consumer<Feedback<T>> feedbackHandler) {
        userFeedbackHandler = feedbackHandler;
    }

    @Override
    public void setCompletionHandler(Runnable completionHandler) {
        userCompletionHandler = completionHandler;
    }





    /**
     * There are a couple of conditions we're waiting on in order to be
     * complete. After each of those conditions has been satisfied, their
     * completion handlers should call this method. We check to see if all of
     * the conditions have been met, and if so, handle the completion event.
     */
    private synchronized void handleIfCompleted() {
        // TODO: This assumes we will have a result, will we always? Can we be
        // sure the
        // result will arive before the state becomes a completion state?
        if (!isComplete()) return;

        // make sure we don't run this more than once
        if (handledCompletion) return;
        handledCompletion = true;

        // run the user completion handler
        if (userCompletionHandler != null) {
            userCompletionHandler.run();
        }

        // clean up anything related to this goal, now that we're done
        completedCommand.accept(this);
        closeSubscriptions();


        // wakes any threads waiting on this object if the goal is complete and
        // we have the result.
        notifyAll();

    }

    @Override
    public boolean isComplete() {
        // return isGoalStateCompleted() && result.isPresent();
        return (isGoalStateSucceeded() && result.isPresent()) || isGoalStateFailed();
    }


    @Override
    public boolean isGoalStateFailed() {

        if (status == GoalState.REJECTED) return true;
        if (status == GoalState.ABORTED) return true;

        return false;
    }

    @Override
    public boolean isGoalStateSucceeded() {

        if (status == GoalState.PREEMPTED) return true;
        if (status == GoalState.SUCCEEDED) return true;
        if (status == GoalState.RECALLED) return true;
        return false;
    }





    @Override
    public void close() throws IOException {
        closeSubscriptions();
        setCompletionHandler(null);
        setFeedbackHandler(null);
        setResultHandler(null);
        setStatusHandler(null);
        client.close();
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    private void closeSubscriptions() {
        try {
            feedbackSub.close();
            statusSub.close();
            resultSub.close();
        }
        catch (IOException e) {}
    }





    private synchronized void handleFeedback(Feedback<T> feedbackMessage) {

        if (userFeedbackHandler != null) userFeedbackHandler.accept(feedbackMessage);
    }

    private synchronized void handleStatus(GoalStatus statusMessage) {

        status = statusMessage.goalStatus();

        handleIfCompleted();
        if (userStatusHandler != null) userStatusHandler.accept(statusMessage);
    }

    private synchronized void handleResult(Result<T> resultMessage) {

        log.debug(RealmSerialize.serialize(resultMessage));

        result = Optional.ofNullable(resultMessage);
        // TODO: change state manually, or will this occur as a separate
        // message?

        handleIfCompleted();
        if (userResultHandler != null) userResultHandler.accept(resultMessage);
    }





    @SuppressWarnings("unchecked")
    private T breakoutData(Object input) {

        Map<String, Object> map = (Map<String, Object>) input;
        String key = new ArrayList<>(map.keySet()).get(0);
        boolean singleKey = map.keySet().size() == 1;

        // if there is more than one key, we're not dealing with a single nested
        // structure.
        if (!singleKey) return RealmSerialize.convertMessage(input, clazz);

        // otherwise, we'll need to 'hoist' the data up one level
        Object value = map.get(key);
        return RealmSerialize.convertMessage(value, clazz);


    }


    // Get a feedback object from a feedback message
    private Feedback<T> unpackFeedback(InternalFeedbackMessage feedbackMessage) {

        Feedback<T> feedback = new Feedback<T>();
        feedback.status = feedbackMessage.status;
        feedback.header = feedbackMessage.header;
        feedback.feedback = breakoutData(feedbackMessage.feedback);

        return feedback;

    }


    private Result<T> unpackResult(InternalResultMessage resultMessage) {
        // TODO: Implement me!

        Result<T> result = new Result<>();
        result.goal_id = resultMessage.goal_id;
        result.result = breakoutData(resultMessage.result);

        return result;
    }

    // Get our Status object from a status message
    private GoalStatus findOurStatus(InternalStatusMessage statusMessage) {

        for (GoalStatus status : statusMessage.status_list) {
            if (getId().equals(status.goal_id.id)) return status;
        }
        return null;

    }


    private boolean isFeedbackForUs(Feedback<T> feedback) {
        GoalID feedbackGoalID = feedback.status.goal_id;
        if (!getId().equals(feedbackGoalID.id)) return false;
        return true;
    }

    private boolean isResultForUs(Result<T> resultMessage) {
        // return idString.equals(resultMessage.goal_id.id);
        // TODO: Implement me!
        return true;
    }

    private String getGoalID() {
        return goalIDGenerator.get();
    }





}
