package mnm.mods.tabbychat.util.config

import com.google.common.reflect.TypeParameter
import com.google.common.reflect.TypeToken
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

internal class ValueSerializer : JsonSerializer<AbstractValue<*>>, JsonDeserializer<AbstractValue<*>> {

    @Throws(JsonParseException::class)
    override fun deserialize(json: JsonElement, type: Type, context: JsonDeserializationContext): AbstractValue<*>? {
        val arg = (type as ParameterizedType).actualTypeArguments[0]
        val rawType = type.rawType
        if (AbstractValue::class.java == rawType) {
            return value<Any>(json, arg, context)
        }
        if (ValueList::class.java == rawType) {
            return list<Any>(json, arg, context)
        }
        return if (ValueMap::class.java == rawType) {
            map<Any>(json, arg, context)
        } else null
    }

    private fun <T> value(json: JsonElement, type: Type, context: JsonDeserializationContext): AbstractValue<T> {

        return Value(context.deserialize(json, type))
    }

    private fun <T> list(json: JsonElement, type: Type, context: JsonDeserializationContext): ValueList<T> {
        return ValueList(context.deserialize(json, listType<T>(type).type))
    }

    private fun <T> map(json: JsonElement, type: Type, context: JsonDeserializationContext): ValueMap<T> {
        return ValueMap(context.deserialize(json, mapType<T>(type).type))
    }

    override fun serialize(src: AbstractValue<*>, typeOfSrc: Type, context: JsonSerializationContext): JsonElement {
        return context.serialize(src.value)
    }

    private fun <T> listType(type: Type): TypeToken<List<T>> {
        return object : TypeToken<List<T>>() {}
                .where(object : TypeParameter<T>() {}, type.token())
    }

    private fun <T> mapType(type: Type): TypeToken<Map<String, T>> {
        return object : TypeToken<Map<String, T>>() {}
                .where(object : TypeParameter<T>() {}, type.token())
    }
}

fun <T> Type.token() = TypeToken.of(this) as TypeToken<T>
