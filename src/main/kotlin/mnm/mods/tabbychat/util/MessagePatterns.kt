package mnm.mods.tabbychat.util;

import static mnm.mods.tabbychat.util.Translation.*;
import net.minecraft.client.resources.I18n;

public enum MessagePatterns {

    ARROW(FORMAT_MESSAGE_ARROW,
            "^\\[%s[ ]?-\\>[ ]?me\\]",
            "^\\[me[ ]?-\\>[ ]?%s\\]"),
    TO_FROM(FORMAT_MESSAGE_TO_FROM,
            "^From %s ?:",
            "^To %s ?:"),
    WHISPERS(FORMAT_MESSAGE_WHISPER,
            "^%s whispers to you:",
            "^You whisper to %s:"),
    DISABLED(FORMAT_MESSAGE_DISABLED, "a^", "a^");

    private static final String PLAYER_PATTERN = "(.{3,30})";

    private final String translation;
    private final String incoming;
    private final String outgoing;

    MessagePatterns(String translation, String incoming, String outgoing) {
        this.translation = translation;
        this.incoming = String.format(incoming, PLAYER_PATTERN);
        this.outgoing = String.format(outgoing, PLAYER_PATTERN);
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
