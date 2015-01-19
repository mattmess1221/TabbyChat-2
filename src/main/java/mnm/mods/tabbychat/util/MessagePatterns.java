package mnm.mods.tabbychat.util;

import mnm.mods.tabbychat.TabbyChat;
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
    CUSTOM(Translation.FORMAT_MESSAGE_CUSTOM, null, null) {
        // Custom patterns
        @Override
        public String getOutgoing() {
            return TabbyChat.getInstance().channelSettings.customPmOutgoing.getValue();
        }

        @Override
        public String getIncoming() {
            return TabbyChat.getInstance().channelSettings.customPmIncoming.getValue();
        }
    };

    private static final String PLAYER_PATTERN = "([\\p{L}\\p{N}_]{3,16})";

    private final Translation translation;
    private final String incoming;
    private final String outgoing;

    private MessagePatterns(Translation translation, String incoming, String outgoing) {
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

    public Translation getTranslation() {
        return translation;
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
