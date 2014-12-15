package net.realmproject.rosbridge.util;



import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import flexjson.transformer.AbstractTransformer;


public class RosBridgeSerialize {



    public static Map<String, Serializable> structToMap(Object o) {
        Object o2 = deserialize(serialize(o));
        if (!(o2 instanceof Map)) o = Collections.singletonMap("value", o);
        return (Map<String, Serializable>) deserialize(serialize(o));
    }

    public static <T> T convertMessage(Object publication, Class<T> clazz) {
        String asString = serialize(publication);
        return deserialize(asString, clazz);
    }

    // (de)serialize methods
    public static Object deserialize(String json) {
        return RosBridgeSerialize.<Object> getDeserializer().deserialize(json);
    }

    public static <T> T deserialize(String json, Class<T> clazz) {
        return RosBridgeSerialize.<T> getDeserializer().deserialize(json, clazz);
    }

    public static String serialize(Object o) {
        return getSerializer().deepSerialize(o);
    }

    // FlexJson Object creation/retrieval
    private static <T> JSONDeserializer<T> getDeserializer() {
        return new JSONDeserializer<>();
    }

    private static JSONSerializer getSerializer() {
        return new JSONSerializer().transform(new ExcludeTransformer(), void.class).exclude("*.class")
                .prettyPrint(true);
    }

}

class ExcludeTransformer extends AbstractTransformer {

    @Override
    public Boolean isInline() {
        return true;
    }

    @Override
    public void transform(Object object) {
        // Do nothing, null objects are not serialized.
        return;
    }
}
