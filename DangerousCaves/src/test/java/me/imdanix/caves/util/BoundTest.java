package me.imdanix.caves.util;

import me.imdanix.caves.util.bound.Bound;
import me.imdanix.caves.util.bound.DualBound;
import me.imdanix.caves.util.bound.SingularBound;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BoundTest {

    @Test
    void fromStringParsesSingularBound() {
        Bound bound = Bound.fromString("10,20");
        SingularBound singular = assertInstanceOf(SingularBound.class, bound);
        assertEquals(10, singular.x());
        assertEquals(20, singular.z());
        assertTrue(bound.isInside(10, 20));
        assertFalse(bound.isInside(10, 21));
        assertFalse(bound.isInside(9, 20));
    }

    @Test
    void fromStringParsesDualBound() {
        Bound bound = Bound.fromString("10,20 30,40");
        DualBound dual = assertInstanceOf(DualBound.class, bound);
        assertEquals(10, dual.xMin());
        assertEquals(30, dual.xMax());
        assertEquals(20, dual.zMin());
        assertEquals(40, dual.zMax());
        assertTrue(bound.isInside(10, 20));
        assertTrue(bound.isInside(30, 40));
        assertTrue(bound.isInside(20, 30));
        assertFalse(bound.isInside(9, 20));
        assertFalse(bound.isInside(31, 40));
        assertFalse(bound.isInside(10, 19));
        assertFalse(bound.isInside(30, 41));
    }

    @Test
    void fromStringReturnsNullOnGarbage() {
        assertNull(Bound.fromString("abc"));
        assertNull(Bound.fromString("abc def"));
        assertNull(Bound.fromString(""));
        assertNull(Bound.fromString("10,20 abc,def"));
    }

    @Test
    void dualBoundSwapsReversedCoordinates() {
        DualBound dual = new DualBound(30, 10, 40, 20);
        assertEquals(10, dual.xMin());
        assertEquals(30, dual.xMax());
        assertEquals(20, dual.zMin());
        assertEquals(40, dual.zMax());
        assertTrue(dual.isInside(10, 20));
        assertTrue(dual.isInside(30, 40));
        assertFalse(dual.isInside(9, 19));
        assertFalse(dual.isInside(31, 41));
    }
}
