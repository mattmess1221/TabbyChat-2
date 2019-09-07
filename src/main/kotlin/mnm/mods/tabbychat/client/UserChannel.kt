package mnm.mods.tabbychat.client

class UserChannel internal constructor(override val name: String) : AbstractChannel(name) {

    override val displayName: String = "@$name"

    init {
        prefix = "/msg $name"
    }

    override fun hashCode(): Int {
        return toString().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as UserChannel

        if (name != other.name) return false

        return true
    }

    override fun toString(): String {
        return displayName
    }

}
