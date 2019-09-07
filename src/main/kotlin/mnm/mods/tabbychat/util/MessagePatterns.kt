package mnm.mods.tabbychat.util

enum class MessagePatterns(
        translation: Translatable,
        val incoming: String,
        val outgoing: String) : Translatable by translation{

    ARROW(Translation.FORMAT_MESSAGE_ARROW,
            "^\\[(.{3,30})[ ]?-\\>[ ]?me\\]",
            "^\\[me[ ]?-\\>[ ]?(.{3,30})\\]"),
    TO_FROM(Translation.FORMAT_MESSAGE_TO_FROM,
            "^From (.{3,30}) ?:",
            "^To (.{3,30}) ?:"),
    WHISPERS(Translation.FORMAT_MESSAGE_WHISPER,
            "^(.{3,30}) whispers to you:",
            "^You whisper to (.{3,30}):"),
    DISABLED(Translation.FORMAT_MESSAGE_DISABLED, "a^", "a^");

}
