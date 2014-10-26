package edu.pne.pnml.model;

import static org.junit.Assert.*;
import org.junit.Test;



/**
 * Created by konstantin on 24/10/14.
 */
public class PlaceTest {

    @Test(expected=IllegalArgumentException.class)
    public void testNegativeMarking() {
        Place place = new Place("place1");
        place.setMarking(-1);
    }

    @Test
    public void testToXML() {
        Place place = new Place("place1");
        place.setName("p1");
        place.setMarking(0);
        place.setPosition(new Position(400, 250));
        assertEquals(place.toXML(), "<place id=\"place1\">\n" +
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
