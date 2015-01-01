package de.kwirz.yapne.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void testInRange() {
        assertFalse( Utils.inRange(5, 6, 10) );
        assertFalse( Utils.inRange(11, 6, 10) );

        assertTrue( Utils.inRange(6, 6, 10));
        assertTrue( Utils.inRange(10, 6, 10));

        assertTrue( Utils.inRange(8, 6, 10));
    }

    @Test
    public void testEnsureRange() {
        assertEquals( Utils.<Integer>ensureRange(5, 6, 10), new Integer(6));
        assertEquals( Utils.<Integer>ensureRange(12, 6, 10), new Integer(10));
        assertEquals( Utils.<Integer>ensureRange(8, 6, 10), new Integer(8));
    }
}