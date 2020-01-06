package mnm.mods.tabbychat.api

interface Channel {

    val name: String

    val type: ChannelType

    var alias: String

    val displayName get() = type.prefix + (alias.takeUnless { it.isBlank() } ?: name)
}

enum class ChannelType(val prefix: String) {
    CHAT("#"),
    USER("@"),
    ROOT("*"),
    OTHER("")
}