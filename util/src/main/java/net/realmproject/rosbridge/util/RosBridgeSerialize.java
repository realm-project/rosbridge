package net.realmproject.rosbridge.util;


import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;


public class RosBridgeSerialize {

    private final static ObjectMapper SERIALIZE_WITHOUT_CLASSES = new ObjectMapper();

    static {
        SERIALIZE_WITHOUT_CLASSES.enable(SerializationConfig.Feature.INDENT_OUTPUT);
    }

    private final static ObjectMapper SERIALIZE_WITH_CLASSES = new ObjectMapper();

    static {
        SERIALIZE_WITH_CLASSES.enable(SerializationConfig.Feature.INDENT_OUTPUT);
        SERIALIZE_WITH_CLASSES.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    }

    private final static ObjectMapper DESERIALIZE = new ObjectMapper();
    static {
    	DESERIALIZE.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @SuppressWarnings("unchecked")
    public static Map<String, Serializable> structToMap(Object o) {
        Object o2 = deserialize(serialize(o));
        if (!(o2 instanceof Map)) o = Collections.singletonMap("value", o);
        return (Map<String, Serializable>) deserialize(serialize(o));
    }

    @SuppressWarnings("unchecked")
    public static <T> T convertObject(Object object, Class<T> clazz) {
        // If this object is of class 'clazz', then just return it
        if (clazz.isAssignableFrom(object.getClass())) { return (T) object; }
        // Otherwise, use json serialization to try and coerce the data into the
        // desired class
        String asString = serialize(object);
        return deserialize(asString, clazz);
    }

    public static Object deserialize(String json) {
        try {
            return DESERIALIZE.reader(Object.class).readValue(json);
        }
        catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T deserialize(String json, Class<T> clazz) {
        try {
            return (T) DESERIALIZE.reader(clazz).readValue(json);
        }
        catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    public static String serializeWithClassInfo(Object o) {
        try {
            return SERIALIZE_WITH_CLASSES.writeValueAsString(o);
        }
        catch (IOException e) {
            throw new SerializationException(e);
        }
    }

    public static String serialize(Object o) {

        try {
            return SERIALIZE_WITHOUT_CLASSES.writeValueAsString(o);
        }
        catch (IOException e) {
            throw new SerializationException(e);
        }
    }

}
