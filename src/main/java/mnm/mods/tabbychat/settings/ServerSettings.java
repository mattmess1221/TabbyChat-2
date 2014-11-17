package mnm.mods.tabbychat.settings;

import java.net.SocketAddress;

import mnm.mods.util.IPUtils;

public abstract class ServerSettings extends TabbySettings {

    private final SocketAddress ip;

    public ServerSettings(SocketAddress url, String name) {
        super(getIPForFileName(url), name);
        this.ip = url;
    }

    private static String getIPForFileName(SocketAddress addr) {
        String ip;
        if (addr == null) {
            ip = "singleplayer";
        } else {
            ip = "multiplayer/" + IPUtils.parse(addr.toString()).getFileSafeAddress();
        }
        return ip;
    }

    public SocketAddress getIP() {
        return this.ip;
    }
}
