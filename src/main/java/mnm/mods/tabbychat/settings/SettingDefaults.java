package mnm.mods.tabbychat.settings;

public enum SettingDefaults {
    CHATBOX_FADE("chatbox.fade", 200),
    CHATBOX_POSITION_X("chatbox.xPos", 5),
    CHATBOX_POSITION_Y("chatbox.yPos", -175),
    CHATBOX_POSITION_WIDTH("chatbox.width", 300),
    CHATBOX_POSITION_HEIGHT("chatbox.height", 160), ;

    public final String key;
    public final Object value;

    private SettingDefaults(String key, Object value) {
        this.key = key;
        this.value = value;
    }

    public static SettingDefaults get(String key) {
        SettingDefaults sett = null;
        for (SettingDefaults def : values()) {
            if (def.key.equals(key)) {
                sett = def;
                break;
            }
        }
        return sett;
    }

    public static Object getDefault(String key) {
        SettingDefaults def = get(key);
        Object val = null;
        if (def != null)
            val = def.value;
        return val;

    }
}
