package mnm.mods.util.test

import mnm.mods.tabbychat.util.*
import org.junit.Assert.assertEquals
import org.junit.Test

class ColorTest {

    @Test
    fun testHex() {

        val c: Color = argb(0x44, 0x77, 0x12, 0)
        assertEquals(c.red.toLong(), 0x77)
        assertEquals(c.green.toLong(), 0x12)
        assertEquals(c.blue.toLong(), 0x00)
        assertEquals(c.alpha.toLong(), 0x44)

        assertEquals(c.asLong, 0x44771200)
    }

    @Test
    fun testEquals() {
        val red = rgb(0xff, 0x55, 0x55)
        assertEquals(Colors.RED, red)
        assertEquals("#ffff5555", red.asString)
        assertEquals(0xffff5555, red.asLong)
    }

    @Test
    fun testParse() {
        val c = color(0xff666666)
        assertEquals(color("#666"), c)
        assertEquals(c.asString, "#ff666666")
    }
}
