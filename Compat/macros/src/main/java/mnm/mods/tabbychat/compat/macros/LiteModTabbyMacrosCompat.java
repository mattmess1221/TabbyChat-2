package mnm.mods.tabbychat.compat.macros;

import java.io.File;

import mnm.mods.tabbychat.api.TabbyAPI;

import org.apache.logging.log4j.LogManager;

import com.mumfrey.liteloader.LiteMod;

public class LiteModTabbyMacrosCompat implements LiteMod {

    @Override
    public String getName() {
        return "TabbyChat Macros Compat";
    }

    @Override
    public String getVersion() {
        return "@VERSION@";
    }

    @Override
    public void init(File configPath) {
        try {
            TabbyAPI.getAPI().getBus().register(new MacrosCompat());
        } catch (Throwable e) {
            LogManager.getLogger().warn("Unable to add macros compatibility. Did something change?", e);
        }
    }

    @Override
    public void upgradeSettings(String version, File configPath, File oldConfigPath) {}

}
