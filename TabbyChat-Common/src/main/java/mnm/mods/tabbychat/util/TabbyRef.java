package mnm.mods.tabbychat.util;

public final class TabbyRef {

    // Core
    public static final String MOD_ID = "TabbyChat2";
    public static final String MOD_NAME = "TabbyChat 2";
    public static final String MOD_VERSION = "@VERSION@";
    public static final double MOD_REVISION = getRevision();

    private static double getRevision() {
        try {
            return Double.parseDouble("@REVISION@");
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
