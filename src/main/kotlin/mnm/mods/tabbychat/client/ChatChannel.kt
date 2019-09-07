package mnm.mods.tabbychat.client

open class ChatChannel internal constructor(
        override val name: String) : AbstractChannel(name) {

    override val displayName: String
        get() = "#" + alias!!

    override fun hashCode(): Int {
        return toString().hashCode()
    }

    override fun toString(): String {
        return "#$name"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ChatChannel

        if (name != other.name) return false

        return true
    }

}
