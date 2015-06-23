package mnm.mods.tabbychat.util;

import mnm.mods.util.Translatable;

public enum MessagePatterns {

    ARROW(Translation.FORMAT_MESSAGE_ARROW,
            "^\\[" + getPlayerPattern() + "[ ]?\\-\\>[ ]?me\\]",
            "^\\[me[ ]?\\-\\>[ ]?" + getPlayerPattern() + "\\]"),
    TO_FROM(Translation.FORMAT_MESSAGE_TO_FROM,
            "^From " + getPlayerPattern() + "[ ]?:",
            "^To " + getPlayerPattern() + "[ ]?:"),
    WHISPERS(Translation.FORMAT_MESSAGE_WHISPER,
            "^" + getPlayerPattern() + " whispers to you:",
            "^You whisper to " + getPlayerPattern() + ":"),
    DISABLED(Translation.FORMAT_MESSAGE_DISABLED, "a^", "a^");

    private static final String PLAYER_PATTERN = "([\\p{L}\\p{N}_]{3,30})";

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
    public String toString() {
        return translation.toString();
    }
}
