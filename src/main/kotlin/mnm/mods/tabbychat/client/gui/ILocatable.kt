package mnm.mods.tabbychat.client.gui

interface ILocatable {
    val xPos: Int
    val yPos: Int
    val width: Int
    val height: Int

    val x1 get() = xPos
    val y1 get() = yPos
    val x2 get() = xPos + width
    val y2 get() = yPos + height

    val xCenter get() = xPos + width / 2
    val yCenter get() = yPos + height / 2

    fun contains(x: Int, y: Int): Boolean {
        return x in x1..x2 && y in y1..y2
    }

    fun contains(x: Double, y: Double): Boolean {
        return x1 < x && x <= x2 && y1 < y && y <= y2
    }
}