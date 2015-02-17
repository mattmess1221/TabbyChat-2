package mnm.mods.tabbychat.settings;

import java.net.InetSocketAddress;

import com.google.gson.JsonElement;

public class ChannelSettings extends AbstractServerSettings {

    public ChannelSettings(InetSocketAddress url) {
        super(url, "channels");
    }

    @Override
    protected void saveSettings() {}

    @Override
    protected void loadSetting(String setting, JsonElement value) {}

}
