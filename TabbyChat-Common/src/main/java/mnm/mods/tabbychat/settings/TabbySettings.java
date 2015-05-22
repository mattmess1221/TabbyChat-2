package mnm.mods.tabbychat.settings;

import java.io.File;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.util.Settings;

public abstract class TabbySettings extends Settings {

    private static final File DIR = TabbyChat.getInstance().getDataFolder();

    protected TabbySettings(String name) {
        super(DIR, name);
    }

    protected TabbySettings(String file, String name) {
        super(new File(DIR, file), name);
    }
}
