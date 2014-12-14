package de.kwirz.yapne.utils;

import de.kwirz.yapne.utils.BuilderValue;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class BuilderValueTest {

    private BuilderValue<Integer> builderValue;

    @Before
    public void setUp() {
        builderValue = new BuilderValue<Integer>();
    }

    @Test
    public void testSetValue() throws Exception {
        assertFalse(builderValue.isSet());
        builderValue.setValue(42);
        assertEquals(builderValue.getValue().intValue(), 42);
        assertTrue(builderValue.isSet());
    }

}