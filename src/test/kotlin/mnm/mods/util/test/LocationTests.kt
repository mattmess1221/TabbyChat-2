package mnm.mods.util.test

import org.junit.Assert.*

import org.junit.Test

import mnm.mods.tabbychat.util.ILocation
import mnm.mods.tabbychat.util.Location

class LocationTests {

    @Test
    fun contains() {

        // inside
        var loc1: ILocation = Location(1, 1, 16, 16)
        var loc2: ILocation = Location(2, 2, 15, 15)
        assertTrue(loc2 in loc1)
        assertTrue(loc1 in loc2)

        // corner intersection
        loc1 = Location(15, 0, 12, 6)
        loc2 = Location(17, 4, 12, 6)
        assertTrue(loc2 in loc1)
        assertTrue(loc1 in loc2)

        // sides
        loc1 = Location(16, 16, 16, 16)
        loc2 = Location(18, 16, 16, 16)
        assertTrue(loc2 in loc1)
        assertTrue(loc1 in loc2)

        loc1 = Location(5, 5, 10, 2)
        loc2 = Location(16, 9, 1, 1)
        assertFalse(loc2 in loc1)
        assertFalse(loc1 in loc2)

        // edge cases
        loc1 = Location(2, 1, 1, 1)
        loc2 = Location(1, 1, 1, 1)
        assertFalse(loc2 in loc1)
        assertFalse(loc1 in loc2)

        loc1 = Location(16, 16, 16, 16)
        loc2 = Location(16, 32, 16, 16)
        assertFalse(loc2 in loc1)
        assertFalse(loc1 in loc2)
    }

}
