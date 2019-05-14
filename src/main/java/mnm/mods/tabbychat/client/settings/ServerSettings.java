package mnm.mods.tabbychat.client.settings;

import io.netty.channel.local.LocalAddress;
import mnm.mods.tabbychat.client.ChatChannel;
import mnm.mods.tabbychat.client.UserChannel;
import mnm.mods.tabbychat.client.extra.filters.UserFilter;
import mnm.mods.tabbychat.util.IPUtils;
import mnm.mods.tabbychat.util.config.SettingsFile;
import mnm.mods.tabbychat.util.config.ValueList;
import mnm.mods.tabbychat.util.config.ValueMap;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerSettings extends SettingsFile {

    public GeneralServerSettings general = new GeneralServerSettings();
    public ValueList<UserFilter> filters = list();
    public ValueMap<ChatChannel> channels = map();
    public ValueMap<UserChannel> pms = map();

    private transient final SocketAddress ip;

    public ServerSettings(Path parent, SocketAddress url) {
        super(parent.resolve(getIPForFileName(url)).resolve("server.json"));
        this.ip = url;
    }

    private static Path getIPForFileName(SocketAddress addr) {
        if (addr instanceof LocalAddress) {
            return Paths.get("singleplayer");
        }
        return Paths.get("multiplayer", IPUtils.getFileSafeAddress((InetSocketAddress) addr));
    }

    public SocketAddress getSocket() {
        return this.ip;
    }

}
