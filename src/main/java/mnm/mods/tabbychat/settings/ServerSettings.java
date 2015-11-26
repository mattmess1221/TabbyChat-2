package mnm.mods.tabbychat.settings;

import java.net.InetSocketAddress;

import com.google.gson.annotations.Expose;

import mnm.mods.tabbychat.ChatChannel;
import mnm.mods.tabbychat.extra.filters.ChatFilter;
import mnm.mods.tabbychat.util.TabbyRef;
import mnm.mods.util.IPUtils;
import mnm.mods.util.config.SettingsFile;
import mnm.mods.util.config.ValueList;
import mnm.mods.util.config.ValueMap;

public class ServerSettings extends SettingsFile {

    @Expose
    public GeneralServerSettings general = new GeneralServerSettings();
    @Expose
    public ValueList<ChatFilter> filters = list();
    @Expose
    public ValueMap<ChatChannel> channels = map();
    @Expose
    public ValueMap<ChatChannel> pms = map();

    private final InetSocketAddress ip;

    public ServerSettings(InetSocketAddress url) {
        super(TabbyRef.MOD_ID + "/" + getIPForFileName(url), "server");
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

}
