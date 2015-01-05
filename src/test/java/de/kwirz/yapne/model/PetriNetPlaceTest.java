package de.kwirz.yapne.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class PetriNetPlaceTest {
    private PetriNetPlace place;

    @Before
    public void setUp() {
        place = new PetriNetPlace("place1");
    }

    @Test(expected=IllegalArgumentException.class)
    public void testNegativeMarking() {
        place.setMarking(-1);
    }

    @Test
    public void testToXML() {
        place.setName("p1");
        place.setMarking(0);
        place.setPosition(new PetriNetNode.Position(400, 250));
        assertEquals(place.toPNML(), "<place id=\"place1\">\n" +
                "<name>\n" +
                "<value>p1</value>\n" +
                "</name>\n" +
                "<initialMarking>\n" +
                "<token>\n" +
                "<value>0</value>\n" +
                "</token>\n" +
                "</initialMarking>\n" + "" +
                "<graphics>\n" +
                "<position x=\"400\" y=\"250\" />\n" +
                "</graphics>\n" +
                "</place>");
    }

}