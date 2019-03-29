package mnm.mods.tabbychat.settings;

import mnm.mods.util.config.SettingsFile;

import java.nio.file.Path;

public class TabbySettings extends SettingsFile {

    public GeneralSettings general = new GeneralSettings();
    public AdvancedSettings advanced = new AdvancedSettings();

    public TabbySettings(Path parent) {
        super(parent.resolve("tabbychat.json"));
    }
}
