package mnm.mods.tabbychat.settings;

import java.lang.reflect.Type;
import java.net.InetSocketAddress;

import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.util.ChatChannel;
import mnm.mods.tabbychat.util.ChatChannel.ChannelMap;
import mnm.mods.util.SettingValue;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ChannelSettings extends AbstractServerSettings {

    public SettingValue<ChannelMap> channels = new SettingValue<ChannelMap>(new ChannelMap());

    public ChannelSettings(InetSocketAddress url) {
        super(url, "channels");

        registerSetting("channels", channels);
    }

    @Override
    protected void setupGson(GsonBuilder builder) {
        builder.registerTypeAdapter(Channel.class, new ChannelFactory());
    }

    public void addChannel(Channel channel) {
        channels.getValue().put(channel.getName(), channel);
        saveSettingsFile();
    }

    public static class ChannelFactory implements JsonSerializer<Channel>,
            JsonDeserializer<Channel> {

        @Override
        public JsonElement serialize(Channel src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty("name", src.getName());
            obj.addProperty("alias", src.getAlias());
            obj.addProperty("prefix", src.getPrefix());
            obj.addProperty("prefixHidden", src.isPrefixHidden());
            return obj;
        }

        @Override
        public Channel deserialize(JsonElement json, Type typeOfT,
                JsonDeserializationContext context) throws JsonParseException {
            JsonObject obj = json.getAsJsonObject();
            String name = obj.get("name").getAsString();
            String alias = obj.get("alias").getAsString();
            String prefix = obj.get("prefix").getAsString();
            boolean prefixHidden = obj.get("prefixHidden").getAsBoolean();
            int pos = obj.get("position").getAsInt();

            Channel channel = new ChatChannel(name);
            channel.setAlias(alias);
            channel.setPrefix(prefix);
            channel.setPrefixHidden(prefixHidden);
            return channel;
        }
    }
}
