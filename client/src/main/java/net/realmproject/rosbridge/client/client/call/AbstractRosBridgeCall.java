package net.realmproject.rosbridge.client.client.call;


import java.io.Closeable;
import java.util.Map;
import java.util.function.Consumer;

import net.realmproject.rosbridge.connection.RosBridgeMessage;
import net.realmproject.rosbridge.util.RosBridgeSerialize;


public abstract class AbstractRosBridgeCall<T> implements RosBridgeCall<T>, Closeable {

    private String id;
    private String service;
    private Consumer<RosBridgeCall<T>> closeCommand;
    private Class<T> clazz;

    public AbstractRosBridgeCall(String service, String id, Class<T> clazz, Consumer<RosBridgeCall<T>> closeCommand) {
        this.id = id;
        this.service = service;
        this.closeCommand = closeCommand;
        this.clazz = clazz;
    }

    @Override
    public void accept(RosBridgeMessage msg) {
        // make sure id matches
        if (!msg.matchesId(id)) return;
        boolean result = (boolean) msg.getArguments().get("result");
        if (!result) throw new IllegalArgumentException("Service Call #" + id + ": '" + service + "' Failed");

        Map<String, Object> args = msg.getArguments();
        String msgService = (String) args.get("service");

        if (!service.equals(msgService)) return;

        closeCommand.accept(this);

        T t = toT(args.get("values"));
        // handle return values
        setResult(t);
    }

    @SuppressWarnings("unchecked")
    private T toT(Object value) {
        if (clazz != null) {
            return RosBridgeSerialize.convertObject(value, clazz);
        } else {
            return (T) value;
        }
    }

    protected abstract void setResult(T r);

    public void close() {
        closeCommand.accept(this);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Call[");
        sb.append(service);
        sb.append(" | #");
        sb.append(id);
        sb.append("]");

        return sb.toString();
    }

}
