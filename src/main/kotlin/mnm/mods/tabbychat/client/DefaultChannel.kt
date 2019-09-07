package mnm.mods.tabbychat.client

object DefaultChannel : ChatChannel("*") {

    override val displayName: String
        get() = name

    // Don't mess with this channel
    override var prefix: String
        get() = super.prefix
        set(_) {}

    override var alias: String
        get() = super.alias
        set(_) {}

    override var isPrefixHidden: Boolean
        get() = super.isPrefixHidden
        set(_) {}

    override var command: String
        get() = super.command
        set(_) {}

}
