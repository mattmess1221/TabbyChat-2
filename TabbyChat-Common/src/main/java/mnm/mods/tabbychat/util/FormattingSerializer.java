package mnm.mods.tabbychat.util;

import java.lang.reflect.Type;

import net.minecraft.util.EnumChatFormatting;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Used to serialize {@link EnumChatFormatting} in an obfuscated environment.
 */
public class FormattingSerializer implements JsonSerializer<EnumChatFormatting>,
        JsonDeserializer<EnumChatFormatting> {

    @Override
    public EnumChatFormatting deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
        return EnumChatFormatting.getValueByName(json.getAsString());
    }

    @Override
    public JsonElement serialize(EnumChatFormatting src, Type typeOfSrc,
            JsonSerializationContext context) {
        return new JsonPrimitive(src.getFriendlyName());
    }
}
