package mnm.mods.tabbychat.util;

import static mnm.mods.tabbychat.util.Translation.*;
import net.minecraft.client.resources.I18n;

public enum MessagePatterns {

    ARROW(FORMAT_MESSAGE_ARROW,
            "^\\[" + MessagePatterns.PLAYER_PATTERN + "[ ]?\\-\\>[ ]?me\\]",
            "^\\[me[ ]?\\-\\>[ ]?" + MessagePatterns.PLAYER_PATTERN + "\\]"),
    TO_FROM(FORMAT_MESSAGE_TO_FROM,
            "^From " + MessagePatterns.PLAYER_PATTERN + "[ ]?:",
            "^To " + MessagePatterns.PLAYER_PATTERN + "[ ]?:"),
    WHISPERS(FORMAT_MESSAGE_WHISPER,
            "^" + MessagePatterns.PLAYER_PATTERN + " whispers to you:",
            "^You whisper to " + MessagePatterns.PLAYER_PATTERN + ":"),
    DISABLED(FORMAT_MESSAGE_DISABLED, "a^", "a^");

    private static final String PLAYER_PATTERN = "([\\w\\d_]{3,30})";

    private final String translation;
    private final String incoming;
    private final String outgoing;

    private MessagePatterns(String translation, String incoming, String outgoing) {
        this.translation = translation;
        this.incoming = incoming;
        this.outgoing = outgoing;
    }

    public String getIncoming() {
        return incoming;
    }

    public String getOutgoing() {
        return outgoing;
    }

    @Override
    public String toString() {
        return I18n.format(translation);
    }
}
