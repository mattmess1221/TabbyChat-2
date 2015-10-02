package mnm.mods.tabbychat.util;

import com.google.common.primitives.Doubles;

public final class TabbyRef {

    // Core
    public static final String MOD_ID = "TabbyChat2";
    public static final String MOD_NAME = "TabbyChat 2";
    public static final String MOD_VERSION = "@VERSION@";
    public static final double MOD_REVISION = getRevision();


    private static double getRevision() {
        Double d = Doubles.tryParse("@REVISION@");
        return d != null ? d : 0;
    }

}
