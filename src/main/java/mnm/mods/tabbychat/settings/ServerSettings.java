package mnm.mods.tabbychat.settings;

import com.mumfrey.liteloader.modconfig.ConfigStrategy;
import com.mumfrey.liteloader.modconfig.ExposableOptions;
import mnm.mods.tabbychat.ChatChannel;
import mnm.mods.tabbychat.extra.filters.UserFilter;
import mnm.mods.tabbychat.util.TabbyRef;
import mnm.mods.util.IPUtils;
import mnm.mods.util.config.SettingsFile;
import mnm.mods.util.config.ValueList;
import mnm.mods.util.config.ValueMap;

import java.net.InetSocketAddress;

@ExposableOptions(strategy = ConfigStrategy.Unversioned)
public class ServerSettings extends SettingsFile {

    public GeneralServerSettings general = new GeneralServerSettings();
    public ValueList<UserFilter> filters = list();
    public ValueMap<ChatChannel> channels = map();
    public ValueMap<ChatChannel> pms = map();

    private transient final InetSocketAddress ip;

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
