package mnm.mods.tabbychat.settings;

import com.google.gson.GsonBuilder;
import com.mumfrey.liteloader.modconfig.ConfigStrategy;
import com.mumfrey.liteloader.modconfig.ExposableOptions;

import mnm.mods.tabbychat.util.TabbyRef;
import mnm.mods.util.config.SettingsFile;
import net.minecraft.util.EnumTypeAdapterFactory;

@ExposableOptions(strategy = ConfigStrategy.Unversioned)
public class TabbySettings extends SettingsFile {

    public GeneralSettings general = new GeneralSettings();
    public AdvancedSettings advanced = new AdvancedSettings();

    public TabbySettings() {
        super(TabbyRef.MOD_ID, "tabbychat");
    }

    @Override
    public void setupGsonSerialiser(GsonBuilder gsonBuilder) {
        super.setupGsonSerialiser(gsonBuilder);
        gsonBuilder.registerTypeAdapterFactory(new EnumTypeAdapterFactory());
    }
}
