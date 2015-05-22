package mnm.mods.tabbychat.settings;

import java.net.InetSocketAddress;

import mnm.mods.util.IPUtils;

public abstract class AbstractServerSettings extends TabbySettings {

    private final InetSocketAddress ip;

    public AbstractServerSettings(InetSocketAddress url, String name) {
        super(getIPForFileName(url), name);
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
