package mnm.mods.tabbychat.util;

public final class TabbyRef {

    // Core
    public static final String MOD_ID = "TabbyChat2";
    public static final String MOD_NAME = "TabbyChat 2";
    public static final String MOD_VERSION = "@VERSION@";
    public static final double MOD_REVISION = getRevision();

    // obfuscation
    public static final String[] PERSISTANT_CHAT_GUI = { "l", "field_73840_e", "persistantChatGUI" };
    public static final String[] DEFAULT_INPUT_FIELD_TEXT = { "u", "field_146409_v", "defaultInputFieldText" };
    public static final String[] SND_REGISTRY = { "e", "field_147697_e", "sndRegistry" };

    private static double getRevision() {
        try {
            return Double.parseDouble("@REVISION@");
        } catch (NumberFormatException e) {
            return 0;
        }
    }

}
