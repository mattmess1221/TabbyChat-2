package mnm.mods.tabbychat.util;

import mnm.mods.util.Translatable;

public enum MessagePatterns implements Translatable {

    ESSENTIALS(Translation.FORMAT_MESSAGE_ESSENTIALS,
            "^\\[" + getPlayerPattern() + "[ ]?\\-\\>[ ]?me\\]",
            "^\\[me[ ]?\\-\\>[ ]?" + getPlayerPattern() + "\\]"),
    HEROCHAT(Translation.FORMAT_MESSAGE_HEROCHAT,
            "^From " + getPlayerPattern() + "[ ]?:",
            "^To " + getPlayerPattern() + "[ ]?:"),
    VANILLA(Translation.FORMAT_MESSAGE_VANILLA,
            "^" + getPlayerPattern() + " whispers to you:",
            "^You whisper to " + getPlayerPattern() + ":"),
    DISABLED(Translation.FORMAT_MESSAGE_DISABLED, "a^", "a^");

    private static final String PLAYER_PATTERN = "([\\p{L}\\p{N}_]{3,16})";

    private final Translatable translation;
    private final String incoming;
    private final String outgoing;

    private MessagePatterns(Translatable translation, String incoming, String outgoing) {
        this.translation = translation;
        this.incoming = incoming;
        this.outgoing = outgoing;
    }

    private static String getPlayerPattern() {
        // Workaround for not being able to use local constants in enums.
        return PLAYER_PATTERN;
    }

    public String getIncoming() {
        return incoming;
    }

    public String getOutgoing() {
        return outgoing;
    }

    @Override
    public String getUnlocalized() {
        return translation.getUnlocalized();
    }

    @Override
    public String translate(Object... params) {
        return translation.translate(params);
    }
}
