package mnm.mods.tabbychat.util;

import mnm.mods.tabbychat.TabbyChat;
import mnm.mods.util.Translatable;

public enum MessagePatterns implements Translatable {
    ESSENTIALS(Translations.FORMAT_MESSAGE_ESSENTIALS,
            "^\\[([\\p{L}\\p{N}_]{3,16})[ ]?\\-\\>[ ]?me\\]",
            "^\\[me[ ]?\\-\\>[ ]?([\\p{L}\\p{N}_]{3,16})\\]"),
    HEROCHAT(Translations.FORMAT_MESSAGE_HEROCHAT,
            "^From ([\\p{L}\\p{N}_]{3,16})[ ]?:",
            "^To ([\\p{L}\\p{N}_]{3,16})[ ]?:"),
    VANILLA(Translations.FORMAT_MESSAGE_VANILLA,
            "^([\\p{L}\\p{N}_]{3,16}) whispers to you:",
            "^You whisper to ([\\p{L}\\p{N}_]{3,16}):"),
    CUSTOM(Translations.FORMAT_MESSAGE_CUSTOM, null, null) {
        // Custom patterns
        @Override
        public String getFromMe() {
            return TabbyChat.getInstance().channelSettings.customPmFromMe.getValue();
        }

        @Override
        public String getToMe() {
            return TabbyChat.getInstance().channelSettings.customPmToMe.getValue();
        }
    };

    private final String translation;
    private final String toMe;
    private final String fromMe;

    private MessagePatterns(String translation, String toMe, String fromMe) {
        this.translation = translation;
        this.toMe = toMe;
        this.fromMe = fromMe;
    }

    public String getToMe() {
        return toMe;
    }

    public String getFromMe() {
        return fromMe;
    }

    @Override
    public String getUnlocalized() {
        return this.translation;
    }
}
