package edu.yapne.model;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class PetriNetTransitionTest {
    PetriNetTransition transition = null;

    @Before
    public void setUp() {
        transition = new PetriNetTransition("transition1");
    }

    @Test
    public void testToXML() {
        transition.setName("a");
        transition.setPosition(new PetriNetNode.Position(200, 100));

        assertEquals(transition.toXml(), "<transition id=\"transition1\">\n" +
                "<name>\n" +
                "<value>a</value>\n" +
                "</name>\n" +
                "<graphics>\n" +
                "<position x=\"200\" y=\"100\" />\n" +
                "</graphics>\n" +
                "</transition>" );
    }

    @Test
    public void testIsActiveWithoutInputPlaces() {
        assertTrue(transition.isEnabled());
    }

    @Test
    public void testIstActiveWithInputPlaceNullMarking() {
        PetriNetPlace place1 = new PetriNetPlace("place1");
        place1.setMarking(0);
        PetriNetArc arc = new PetriNetArc("arc1");
        arc.setSource(place1);
        arc.setTarget(transition);
        assertFalse(transition.isEnabled());
    }

    @Test
    public void testIsActive() {
        PetriNetPlace place1 = new PetriNetPlace("place1");
        PetriNetPlace place2 = new PetriNetPlace("place2");

        PetriNetArc arc1 = new PetriNetArc("arc1");
        PetriNetArc arc2 = new PetriNetArc("arc2");

        arc1.setSource(place1);
        arc1.setTarget(transition);

        arc2.setSource(place2);
        arc2.setTarget(transition);

        assertFalse(transition.isEnabled());

        place1.setMarking(1);
        assertFalse(transition.isEnabled());

        place2.setMarking(1);
        assertTrue(transition.isEnabled());
    }
}