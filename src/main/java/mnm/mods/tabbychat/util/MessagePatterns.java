package mnm.mods.tabbychat.util;

import mnm.mods.tabbychat.TabbyChat;

public enum MessagePatterns {
    ESSENTIALS("^\\[([\\p{L}\\p{N}_]{3,16})[ ]?\\-\\>[ ]?me\\]",
            "^\\[me[ ]?\\-\\>[ ]?([\\p{L}\\p{N}_]{3,16})\\]"),

    HEROCHAT("^From ([\\p{L}\\p{N}_]{3,16})[ ]?:", "^To ([\\p{L}\\p{N}_]{3,16})[ ]?:"),
    VANILLA("^([\\p{L}\\p{N}_]{3,16}) whispers to you:", "^You whisper to ([\\p{L}\\p{N}_]{3,16}):"),
    CUSTOM(null, null) {
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

    private final String toMe;
    private final String fromMe;

    private MessagePatterns(String toMe, String fromMe) {
        this.toMe = toMe;
        this.fromMe = fromMe;
    }

    public String getToMe() {
        return toMe;
    }

    public String getFromMe() {
        return fromMe;
    }

}
