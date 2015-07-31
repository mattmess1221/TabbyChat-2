package mnm.mods.tabbychat.settings;

import java.io.File;
import java.net.InetSocketAddress;

import com.google.gson.GsonBuilder;

import mnm.mods.tabbychat.ChatChannel;
import mnm.mods.tabbychat.extra.filters.ChatFilter;
import mnm.mods.util.IPUtils;
import mnm.mods.util.config.Setting;
import mnm.mods.util.config.SettingList;
import mnm.mods.util.config.SettingMap;
import mnm.mods.util.config.SettingsFile;
import net.minecraft.util.EnumTypeAdapterFactory;

public class ServerSettings extends SettingsFile<ServerSettings> {

    @Setting
    public GeneralServerSettings general = new GeneralServerSettings();
    @Setting
    public SettingList<ChatFilter> filters = list(ChatFilter.class);
    @Setting
    public SettingMap<ChatChannel> channels = map(ChatChannel.class);
    @Setting
    public SettingMap<ChatChannel> pms = map(ChatChannel.class);

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
        builder.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
    }

}
