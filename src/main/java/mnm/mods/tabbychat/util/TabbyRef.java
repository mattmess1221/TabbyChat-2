package mnm.mods.tabbychat.util;

import com.google.common.primitives.Doubles;
import mnm.mods.util.update.VersionData;

public final class TabbyRef {

    // Core
    public static final String MOD_ID = "TabbyChat2";
    public static final String MOD_NAME = "TabbyChat 2";
    public static final String MOD_VERSION = "@VERSION@";
    public static final double MOD_REVISION = getRevision();
    public static final String UPDATE_URL = "https://raw.githubusercontent.com/killjoy1221/Version/master/TabbyChat2.json";
    public static final String URL = "https://minecraft.curseforge.com/projects/tabbychat-2";

    private static double getRevision() {
        Double d = Doubles.tryParse("@REVISION@");
        return d != null ? d : -1;
    }

    public static VersionData getVersionData() {
        return new VersionData(MOD_NAME, UPDATE_URL, URL, MOD_REVISION);
    }

}
