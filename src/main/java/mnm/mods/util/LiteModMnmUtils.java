package mnm.mods.util;

import com.mumfrey.liteloader.LiteMod;

import java.io.File;

public class LiteModMnmUtils implements LiteMod {

    private MnmUtils utils;

    @Override
    public String getName() {
        return "MnmUtils";
    }

    @Override
    public String getVersion() {
        return "@VERSION@";
    }

    @SuppressWarnings("deprecation")
    @Override
    public void init(File arg0) {
        this.utils = new MnmUtils();
    }

    @Override
    public void upgradeSettings(String arg0, File arg1, File arg2) {
    }

    public MnmUtils getUtils() {
        return utils;
    }

}
