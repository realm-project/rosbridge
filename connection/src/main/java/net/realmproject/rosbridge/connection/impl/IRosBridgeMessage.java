package net.realmproject.rosbridge.connection.impl;


import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import net.realmproject.rosbridge.connection.RosBridgeMessage;
import net.realmproject.rosbridge.util.RosBridgeSerialize;


/**
 * Struct representing a ROSBridge message
 * 
 * @author Nathaniel Sherry
 *
 */
public class IRosBridgeMessage implements RosBridgeMessage {

    private String opcode;
    private Optional<String> id;
    private Map<String, Object> arguments;

    public IRosBridgeMessage() {}

    public IRosBridgeMessage(String source) {

        Map<String, Object> map = (Map<String, Object>) RosBridgeSerialize.deserialize(source);

        setOpcode((String) map.remove("op"));
        if (map.containsKey("id")) {
            setId((String) map.remove("id"));
        } else {
            setId(null);
        }
        setArguments(map);
    }

    public IRosBridgeMessage(RosBridgeMessage source) {
        setOpcode(source.getOpcode());
        setId(source.getId().orElse(null));
        setArguments(new HashMap<>(source.getArguments()));
    }

    @Override
    public String getOpcode() {
        return opcode;
    }

    @Override
    public void setOpcode(String opcode) {
        this.opcode = opcode;
    }

    @Override
    public Map<String, Object> getArguments() {
        return arguments;
    }

    @Override
    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }

    @Override
    public Optional<String> getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = Optional.ofNullable(id);
    }

    /**
     * Returns true iff this message included an id field
     */
    @Override
    public boolean hasId() {
        return id.isPresent();
    }

    /**
     * Returns true iff this message has an id, and it matches the id given
     */
    @Override
    public boolean matchesId(String id) {
        if (!hasId()) return false;
        return this.id.get().equals(id);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("OpCode: " + opcode + "\n");
        sb.append("ID: " + id.orElse("null") + "\n");
        sb.append("Arguments: \n");
        for (String key : arguments.keySet()) {
            sb.append("  " + key + ": " + arguments.get(key) + "\n");
        }
        return sb.toString();
    }

}
