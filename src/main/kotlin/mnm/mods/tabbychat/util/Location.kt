package mnm.mods.tabbychat.util

class Location(
        override var xPos: Int = 0,
        override var yPos: Int = 0,
        override var width: Int = 1,
        override var height: Int = 1) : ILocation {

    constructor(location: ILocation) : this(location.xPos, location.yPos, location.width, location.height)

    fun move(x: Int, y: Int): Location {
        this.xPos += x
        this.yPos += y
        return this
    }

    fun scale(scale: Float): Location {
        this.xPos *= scale.toInt()
        this.yPos *= scale.toInt()
        this.width *= scale.toInt()
        this.height *= scale.toInt()
        return this
    }

    override fun asImmutable() = ImmutableLocation(this)
}
