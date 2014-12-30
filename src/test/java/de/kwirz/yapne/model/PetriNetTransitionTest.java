package de.kwirz.yapne.model;

import de.kwirz.yapne.model.PetriNetArc;
import de.kwirz.yapne.model.PetriNetNode;
import de.kwirz.yapne.model.PetriNetPlace;
import de.kwirz.yapne.model.PetriNetTransition;
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
    public void testIsEnabled() {
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

    @Test
    public void testOccurrenceByDisabledTransition() {
        PetriNetPlace in = new PetriNetPlace("in");
        PetriNetPlace out = new PetriNetPlace("out");

        in.connectToNode(transition);
        transition.connectToNode(out);

        assertFalse(transition.isEnabled());
        assertEquals(in.getMarking(), 0);
        assertEquals(out.getMarking(), 0);

        transition.occur();

        assertFalse(transition.isEnabled());
        assertEquals(in.getMarking(), 0);
        assertEquals(out.getMarking(), 0);
    }

    @Test
    public void testOccurrenceByEnabledTransition() {
        PetriNetPlace in1 = new PetriNetPlace("in1");
        in1.setMarking(1);
        PetriNetPlace in2 = new PetriNetPlace("in2");
        in2.setMarking(3);
        PetriNetPlace out1 = new PetriNetPlace("out1");
        PetriNetPlace out2 = new PetriNetPlace("out2");
        out2.setMarking(1);

        in1.connectToNode(transition);
        in2.connectToNode(transition);
        transition.connectToNode(out1);
        transition.connectToNode(out2);

        assertTrue(transition.isEnabled());
        assertEquals(in1.getMarking(), 1);
        assertEquals(in2.getMarking(), 3);
        assertEquals(out1.getMarking(), 0);
        assertEquals(out2.getMarking(), 1);

        transition.occur();

        assertFalse(transition.isEnabled());
        assertEquals(in1.getMarking(), 0);
        assertEquals(in2.getMarking(), 2);
        assertEquals(out1.getMarking(), 2);
        assertEquals(out2.getMarking(), 3);
    }
}