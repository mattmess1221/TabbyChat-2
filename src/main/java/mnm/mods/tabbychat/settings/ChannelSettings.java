package mnm.mods.tabbychat.settings;

import java.net.InetSocketAddress;

public class ChannelSettings extends AbstractServerSettings {

    public ChannelSettings(InetSocketAddress url) {
        super(url, "channels");
    }
}
