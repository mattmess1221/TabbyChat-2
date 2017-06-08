package mnm.mods.util.test;

import static org.junit.Assert.*;

import org.junit.Test;

import mnm.mods.util.ILocation;
import mnm.mods.util.Location;

public class LocationTests {

    @Test
    public void contains() {

        // inside
        ILocation loc1 = new Location(1, 1, 16, 16);
        ILocation loc2 = new Location(2, 2, 15, 15);
        assertTrue(loc1.contains(loc2));
        assertTrue(loc2.contains(loc1));

        // corner intersection
        loc1 = new Location(15, 0, 12, 6);
        loc2 = new Location(17, 4, 12, 6);
        assertTrue(loc1.contains(loc2));
        assertTrue(loc2.contains(loc1));

        // sides
        loc1 = new Location(16, 16, 16, 16);
        loc2 = new Location(18, 16, 16, 16);
        assertTrue(loc1.contains(loc2));
        assertTrue(loc2.contains(loc1));

        loc1 = new Location(5, 5, 10, 2);
        loc2 = new Location(16, 9, 1, 1);
        assertFalse(loc1.contains(loc2));
        assertFalse(loc2.contains(loc1));

        // edge cases
        loc1 = new Location(2, 1, 1, 1);
        loc2 = new Location(1, 1, 1, 1);
        assertFalse(loc1.contains(loc2));
        assertFalse(loc2.contains(loc1));

        loc1 = new Location(16, 16, 16, 16);
        loc2 = new Location(16, 32, 16, 16);
        assertFalse(loc1.contains(loc2));
        assertFalse(loc2.contains(loc1));
    }

}
