package de.kwirz.yapne.model;

import de.kwirz.yapne.model.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;


public class PetriNetTest {

    private PetriNet net;

    @Before
    public void setUp() {
        net = new PetriNet();

        PetriNetTransition transition1 = new PetriNetTransition("transition1");
        transition1.setName("a");
        transition1.setPosition(new PetriNetNode.Position(200, 100));

        PetriNetTransition transition2 = new PetriNetTransition("transition2");
        transition2.setName("b");
        transition2.setPosition(new PetriNetNode.Position(200, 300));

        PetriNetPlace place1 = new PetriNetPlace("place1");
        place1.setName("p1");
        place1.setMarking(0);
        place1.setPosition(new PetriNetNode.Position(400, 250));

        PetriNetArc arc1 = new PetriNetArc("arc1");
        arc1.setSource(transition1);
        arc1.setTarget(place1);

        PetriNetArc arc2 = new PetriNetArc("arc2");
        arc2.setSource(place1);
        arc2.setTarget(transition2);

        net.addElement(transition1);
        net.addElement(transition2);
        net.addElement(place1);
        net.addElement(arc1);
        net.addElement(arc2);
    }

    @Test
    public void testGetIds() {
        List<String> ids = net.getIds();
        assertEquals(ids.size(), 5);
        assertTrue(ids.contains("transition1"));
        assertTrue(ids.contains("transition2"));
        assertTrue(ids.contains("place1"));
        assertTrue(ids.contains("arc1"));
        assertTrue(ids.contains("arc2"));
    }

    @Test
    public void testClear() {
        assertFalse(net.getIds().isEmpty());
        net.clear();
        assertTrue(net.getIds().isEmpty());
    }

    @Test
    public void testHasElementById() {
        assertTrue(net.hasElementById("transition1"));
        assertTrue(net.hasElementById("transition2"));
        assertTrue(net.hasElementById("place1"));
        assertTrue(net.hasElementById("arc1"));
        assertTrue(net.hasElementById("arc2"));

        assertFalse(net.hasElementById("not_exist"));
    }

    @Test
    public void testGetElementById() {
        assertEquals(net.getElementById("transition1").getId(), "transition1");
        assertEquals(net.getElementById("transition2").getId(), "transition2");
        assertEquals(net.getElementById("place1").getId(), "place1");
        assertEquals(net.getElementById("arc1").getId(), "arc1");
        assertEquals(net.getElementById("arc2").getId(), "arc2");

        assertEquals(net.getElementById("not_exist"), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddElementWithoutId() {
        PetriNetPlace place = new PetriNetPlace("");
        net.addElement(place);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddArcWithoutSource() {
        PetriNetArc arc = new PetriNetArc("empty");
        arc.setTarget(new PetriNetPlace("place32324"));
        net.addElement(arc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddArcWithoutTarget() {
        PetriNetArc arc = new PetriNetArc("arc1202");
        arc.setTarget(new PetriNetTransition("trans131231"));
        net.addElement(arc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddElementWithExistingId() {
        final String id = "place43627";
        PetriNetPlace place = new PetriNetPlace(id);
        PetriNetTransition transition = new PetriNetTransition(id);

        net.addElement(place);
        assertTrue(net.hasElementById(id));

        net.addElement(transition);
    }

    @Test
    public void testRemoveElementById() {
        assertTrue(net.hasElementById("place1"));
        net.removeElementById("place1");
        assertFalse(net.hasElementById("place1"));
    }

    @Test
    public void testRemoveElementWithIOArcs() {
        assertTrue(net.hasElementById("arc1"));
        assertTrue(net.hasElementById("arc2"));
        net.removeElementById("place1");
        assertFalse(net.hasElementById("arc1"));
        assertFalse(net.hasElementById("arc2"));
    }

    @Test
    public void testRemoveArc() {
        assertTrue(net.hasElementById("place1"));
        assertTrue(net.hasElementById("arc1"));
        assertEquals(((PetriNetNode) net.getElementById("place1")).inputArcs.size(), 1);
        assertEquals(((PetriNetNode) net.getElementById("transition1")).outputArcs.size(), 1);

        net.removeElementById("arc1");
        assertFalse(net.hasElementById("arc1"));
        assertTrue(((PetriNetNode) net.getElementById("place1")).inputArcs.isEmpty());
        //assertTrue(((PetriNetNode) net.getElementById("transition1")).outputArcs.isEmpty());
    }

    @Test
    public void testToXml() {
        assertEquals(net.toXml(), "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\n" +
                "<pnml>\n" +
                "<net>\n" +
                "<transition id=\"transition1\">\n" +
                "<name>\n" +
                "<value>a</value>\n" +
                "</name>\n" +
                "<graphics>\n" +
                "<position x=\"200\" y=\"100\" />\n" +
                "</graphics>\n" +
                "</transition>\n" +
                "<transition id=\"transition2\">\n" +
                "<name>\n" +
                "<value>b</value>\n" +
                "</name>\n" +
                "<graphics>\n" +
                "<position x=\"200\" y=\"300\" />\n" +
                "</graphics>\n" +
                "</transition>\n" +
                "<place id=\"place1\">\n" +
                "<name>\n" +
                "<value>p1</value>\n" +
                "</name>\n" +
                "<initialMarking>\n" +
                "<token>\n" +
                "<value>0</value>\n" +
                "</token>\n" +
                "</initialMarking>\n" +
                "<graphics>\n" +
                "<position x=\"400\" y=\"250\" />\n" +
                "</graphics>\n" +
                "</place>\n" +
                "<arc id=\"arc1\" source=\"transition1\" target=\"place1\"> </arc>\n" +
                "<arc id=\"arc2\" source=\"place1\" target=\"transition2\"> </arc>\n" +
                "</net>\n" +
                "</pnml>" );
    }

}