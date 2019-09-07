package mnm.mods.tabbychat.client.settings;

import mnm.mods.tabbychat.util.config.SettingsFile;

import java.nio.file.Path;

public class TabbySettings extends SettingsFile {

    public GeneralSettings general = new GeneralSettings();
    public AdvancedSettings advanced = new AdvancedSettings();

    public TabbySettings(Path parent) {
        super(parent.resolve("tabbychat.json"));
    }
}
