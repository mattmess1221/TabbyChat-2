package mnm.mods.tabbychat.util

interface ILocation {

    val xPos: Int
    val yPos: Int
    val width: Int
    val height: Int

    val xWidth: Int get() = xPos + width
    val yHeight: Int get() = yPos + height
    val xCenter: Int get() = xPos + width / 2
    val yCenter: Int get() = yPos + height / 2

    val point: Vec2i get() = Vec2i(xPos, yPos)
    val size: Dim get() = Dim(this.width, this.height)

    fun asImmutable(): ILocation

    fun copy(): Location = Location(this)

    operator fun contains(r: ILocation): Boolean {
        return (this.xPos < r.xWidth
                && this.xWidth > r.xPos
                && this.yPos < r.yHeight
                && this.yHeight > r.yPos)
    }

    fun contains(x: Number, y: Number) = x.toInt() in xPos..xWidth && y.toInt() in yPos..yHeight
}
