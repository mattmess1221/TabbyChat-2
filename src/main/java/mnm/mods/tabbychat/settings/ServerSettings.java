package mnm.mods.tabbychat.settings;

import io.netty.channel.local.LocalAddress;
import mnm.mods.tabbychat.ChatChannel;
import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.extra.filters.UserFilter;
import mnm.mods.util.IPUtils;
import mnm.mods.util.config.SettingsFile;
import mnm.mods.util.config.ValueList;
import mnm.mods.util.config.ValueMap;

import javax.annotation.Nullable;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ServerSettings extends SettingsFile {

    public GeneralServerSettings general = new GeneralServerSettings();
    public ValueList<UserFilter> filters = list();
    public ValueMap<ChatChannel> channels = map();
    public ValueMap<ChatChannel> pms = map();

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
