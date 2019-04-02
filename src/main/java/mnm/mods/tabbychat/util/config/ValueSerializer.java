package mnm.mods.tabbychat.util.config;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang3.reflect.TypeUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

class ValueSerializer implements JsonSerializer<Value<?>>, JsonDeserializer<Value<?>> {

    @Override
    public Value<?> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        Type arg = ((ParameterizedType) type).getActualTypeArguments()[0];
        type = ((ParameterizedType) type).getRawType();
        if (Value.class.equals(type)) {
            return value(json, arg, context);
        }
        if (ValueList.class.equals(type)) {
            return list(json, arg, context);
        }
        if (ValueMap.class.equals(type)) {
            return map(json, arg, context);
        }
        return null;
    }

    private <T> Value<T> value(JsonElement json, Type type, JsonDeserializationContext context) {
        Value<T> s = new Value<>();
        s.set(context.deserialize(json, type));
        return s;
    }

    private <T> ValueList<T> list(JsonElement json, Type type, JsonDeserializationContext context) {
        ValueList<T> l = new ValueList<>();
        List<T> list = context.deserialize(json, TypeUtils.parameterize(List.class, type));
        l.set(list);
        return l;
    }

    private <T> ValueMap<T> map(JsonElement json, Type type, JsonDeserializationContext context) {
        ValueMap<T> m = new ValueMap<>();
        Map<String, T> map = context.deserialize(json, TypeUtils.parameterize(Map.class, String.class, type));
        m.set(map);
        return m;
    }

    @Override
    public JsonElement serialize(Value<?> src, Type typeOfSrc, JsonSerializationContext context) {
        return context.serialize(src.get());
    }
}
