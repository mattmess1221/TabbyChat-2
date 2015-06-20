package mnm.mods.tabbychat.settings;

import java.io.File;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;

import mnm.mods.tabbychat.ChatChannel;
import mnm.mods.tabbychat.api.Channel;
import mnm.mods.tabbychat.extra.filters.ChatFilter;
import mnm.mods.tabbychat.util.FormattingSerializer;
import mnm.mods.util.IPUtils;
import mnm.mods.util.config.Setting;
import mnm.mods.util.config.SettingList;
import mnm.mods.util.config.SettingMap;
import mnm.mods.util.config.SettingsFile;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class ServerSettings extends SettingsFile<ServerSettings> {

    @Setting
    public GeneralServerSettings general = new GeneralServerSettings();
    @Setting
    public SettingList<ChatFilter> filters = list(ChatFilter.class);
    @Setting
    public SettingMap<Channel> channels = map(Channel.class);
    @Setting
    public SettingMap<Channel> pms = map(Channel.class);

    private final InetSocketAddress ip;

    public ServerSettings(InetSocketAddress url) {
        super(new File(TabbySettings.DIR, getIPForFileName(url)), "server");
        this.ip = url;
    }

    private static String getIPForFileName(InetSocketAddress addr) {
        String ip;
        if (addr == null) {
            ip = "singleplayer";
        } else {
            String url = addr.getHostName();
            ip = "multiplayer/" + IPUtils.parse(url).getFileSafeAddress();
        }
        return ip;
    }

    public InetSocketAddress getIP() {
        return this.ip;
    }

    @Override
    protected void setupGson(GsonBuilder builder) {
        builder.registerTypeAdapter(EnumChatFormatting.class, new FormattingSerializer());
        builder.registerTypeAdapter(Channel.class, new ChannelFactory());
    }

    private class ChannelFactory implements JsonSerializer<Channel>,
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

            Channel channel = new ChatChannel(name);
            channel.setAlias(alias);
            channel.setPrefix(prefix);
            channel.setPrefixHidden(prefixHidden);
            return channel;
        }
    }
}
