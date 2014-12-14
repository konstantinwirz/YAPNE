package de.kwirz.yapne.model;


import de.kwirz.yapne.model.PetriNetArc;
import de.kwirz.yapne.model.PetriNetPlace;
import de.kwirz.yapne.model.PetriNetTransition;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class PetriNetArcTest {

    private PetriNetArc arc;

    @Before
    public void setUp() {
        arc = new PetriNetArc("arc1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSourceAndTargetOfSameType() {
        arc.setSource(new PetriNetPlace("place1"));
        arc.setTarget(new PetriNetPlace("place2"));
    }

    @Test
    public void testToXML() {
        arc.setSource(new PetriNetTransition("transition1"));
        arc.setTarget(new PetriNetPlace("place1"));
        assertEquals(arc.toXml(), "<arc id=\"arc1\" source=\"transition1\" target=\"place1\"> </arc>");
    }
}