package mnm.mods.util.test

import org.junit.Assert.*

import org.junit.Test

import mnm.mods.tabbychat.util.Color

class ColorTest {

    @Test
    fun testHex() {
        val c = Color(0x44771200)
        assertEquals(c.red.toLong(), 0x77)
        assertEquals(c.green.toLong(), 0x12)
        assertEquals(c.blue.toLong(), 0x00)
        assertEquals(c.alpha.toLong(), 0x44)

        assertEquals(c.hex.toLong(), 0x44771200)
    }

    @Test
    fun testEquals() {
        assertEquals(Color.RED, Color(-0xaaab))
        assertEquals(Color(-0xaaab), Color(0xff, 0x55, 0x55, 0xff))
    }

}
