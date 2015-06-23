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
import com.google.gson.InstanceCreator;

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
        builder.registerTypeAdapter(Channel.class, new InstanceCreator<Channel>() {
            @Override
            public Channel createInstance(Type type) {
                return new ChatChannel(null);
            }
        });
    }

}
