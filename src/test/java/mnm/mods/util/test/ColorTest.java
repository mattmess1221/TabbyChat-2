package mnm.mods.util.test;

import static org.junit.Assert.*;

import org.junit.Test;

import mnm.mods.util.Color;

public class ColorTest {

    @Test
    public void testHex() {
        Color c = Color.of(0x44771200);
        assertEquals(c.getRed(), 0x77);
        assertEquals(c.getGreen(), 0x12);
        assertEquals(c.getBlue(), 0x00);
        assertEquals(c.getAlpha(), 0x44);

        assertEquals(c.getHex(), 0x44771200);
    }

    @Test
    public void testEquals() {
        assertEquals(Color.RED, Color.of(0xffff5555));
        assertEquals(Color.of(0xffff5555), Color.of(0xff, 0x55, 0x55, 0xff));
    }

}
