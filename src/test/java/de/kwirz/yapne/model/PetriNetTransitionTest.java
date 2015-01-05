package de.kwirz.yapne.model;

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

        assertEquals(transition.toPNML(), "<transition id=\"transition1\">\n" +
                "<name>\n" +
                "<value>a</value>\n" +
                "</name>\n" +
                "<graphics>\n" +
                "<position x=\"200\" y=\"100\" />\n" +
                "</graphics>\n" +
                "</transition>" );
    }

    @Test
    public void testIsEnabledWithoutInputPlaces() {
        assertTrue(transition.isEnabled());

        transition.connectToNode(new PetriNetPlace("place1"));
        assertTrue(transition.isEnabled());

        new PetriNetPlace("place2").connectToNode(transition);
        assertFalse(transition.isEnabled());
    }

    @Test
    public void testIsEnabled() {
        PetriNetPlace in1 = new PetriNetPlace("place1");
        PetriNetPlace in2 = new PetriNetPlace("place2");
        PetriNetPlace in3 = new PetriNetPlace("place3");

        PetriNetPlace out = new PetriNetPlace("place4");

        in1.connectToNode(transition);
        in2.connectToNode(transition);
        in3.connectToNode(transition);
        transition.connectToNode(out);

        assertFalse(transition.isEnabled());
        in1.setMarking(1);
        assertFalse(transition.isEnabled());
        in2.setMarking(1);
        assertFalse(transition.isEnabled());
        in3.setMarking(1);
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
    public void testOccurrenceWithoutOutputNodes() {
        PetriNetPlace in = new PetriNetPlace("in");
        in.setMarking(1);
        in.connectToNode(transition);

        assertTrue(transition.isEnabled());

        transition.occur();

        assertTrue(transition.isEnabled());
        assertEquals(in.getMarking(), 1);

    }

    @Test
    public void testOccurrenceByEnabledTransition() {
        PetriNetPlace in1 = new PetriNetPlace("in1");
        PetriNetPlace in2 = new PetriNetPlace("in2");
        PetriNetPlace in3 = new PetriNetPlace("in3");

        in1.setMarking(2);
        in2.setMarking(3);
        in3.setMarking(6);

        PetriNetPlace out1 = new PetriNetPlace("out1");
        PetriNetPlace out2 = new PetriNetPlace("out2");

        transition.connectFromNodes(in1, in2, in3);
        transition.connectToNodes(out1, out2);

        assertTrue(transition.isEnabled());

        transition.occur();

        assertTrue(transition.isEnabled());
        assertEquals(in1.getMarking(), 1);
        assertEquals(in2.getMarking(), 2);
        assertEquals(in3.getMarking(), 5);
        assertEquals(out1.getMarking(), 3);
        assertEquals(out2.getMarking(), 3);

        transition.occur();

        assertFalse(transition.isEnabled());
        assertEquals(in1.getMarking(), 0);
        assertEquals(in2.getMarking(), 1);
        assertEquals(in3.getMarking(), 4);
        assertEquals(out1.getMarking(), 6);
        assertEquals(out2.getMarking(), 6);

        // Transition ist nicht aktiviert,
        // Schaltvorgang darf nichts verändern

        assertFalse(transition.isEnabled());
        assertEquals(in1.getMarking(), 0);
        assertEquals(in2.getMarking(), 1);
        assertEquals(in3.getMarking(), 4);
        assertEquals(out1.getMarking(), 6);
        assertEquals(out2.getMarking(), 6);
    }

}