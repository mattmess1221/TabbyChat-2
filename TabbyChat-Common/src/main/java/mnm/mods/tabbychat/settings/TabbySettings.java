package mnm.mods.tabbychat.settings;

import java.io.File;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.tabbychat.util.FormattingSerializer;
import mnm.mods.util.config.Setting;
import mnm.mods.util.config.SettingsFile;
import net.minecraft.util.EnumChatFormatting;

import com.google.gson.GsonBuilder;

public class TabbySettings extends SettingsFile<TabbySettings> {

    protected static final File DIR = TabbyChat.getInstance().getDataFolder();

    @Setting
    public GeneralSettings general = new GeneralSettings();
    @Setting
    public AdvancedSettings advanced = new AdvancedSettings();
    @Setting
    public ColorSettings colors = new ColorSettings();

    public TabbySettings() {
        super(DIR, "tabbychat");
    }

    @Override
    protected void setupGson(GsonBuilder builder) {
        builder.registerTypeAdapter(EnumChatFormatting.class, new FormattingSerializer());
    }
}
