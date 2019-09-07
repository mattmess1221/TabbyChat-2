package mnm.mods.tabbychat.util

import javax.annotation.concurrent.Immutable

@Immutable
data class ImmutableLocation(
        override val xPos: Int,
        override val yPos: Int,
        override val width: Int,
        override val height: Int) : ILocation {

    constructor(location: ILocation) : this(location.xPos, location.yPos, location.width, location.height)

    override fun asImmutable() = this

}
