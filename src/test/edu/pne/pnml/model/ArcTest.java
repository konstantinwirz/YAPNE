package edu.pne.pnml.model;

import org.junit.Test;
import static org.junit.Assert.*;


public class ArcTest {

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSource() {
        Arc arc = new Arc("arc");
        arc.setSource(arc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTarget() {
        Arc arc = new Arc("arc");
        arc.setTarget(arc);
    }

    @Test
    public void testToXML() {
        Arc arc = new Arc("arc1");
        arc.setSource(new Transition("transition1"));
        arc.setTarget(new Place("place1"));
        assertEquals(arc.toXML(), "<arc id=\"arc1\" source=\"transition1\" target=\"place1\"> </ arc>");
    }
}