package edu.pne.pnml.model;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;


public class NetTest {

    private Net net = null;

    @Before
    public void setUp() {
        net = new Net();

        Transition transition1 = new Transition("transition1");
        transition1.setName("a");
        transition1.setPosition(new Position(200, 100));

        Transition transition2 = new Transition("transition2");
        transition2.setName("b");
        transition2.setPosition(new Position(200, 300));

        Place place1 = new Place("place1");
        place1.setName("p1");
        place1.setMarking(0);
        place1.setPosition(new Position(400, 250));

        Arc arc1 = new Arc("arc1");
        arc1.setSource(transition1);
        arc1.setTarget(place1);

        Arc arc2 = new Arc("arc2");
        arc2.setSource(place1);
        arc2.setTarget(transition2);

        net.addElement(transition1);
        net.addElement(transition2);
        net.addElement(place1);
        net.addElement(arc1);
        net.addElement(arc2);
    }

    @Test
    public void testElementsCount() {
        ArrayList<Element> elements = net.getElements();
        assertEquals(elements.size(), 5);
    }

    @Test
    public void testClear() {
        assertFalse(net.getElements().isEmpty());
        net.clear();
        assertTrue(net.getElements().isEmpty());
    }

    @Test
    public void testHasId() {
        assertTrue(net.hasId("transition1"));
        assertTrue(net.hasId("transition2"));
        assertTrue(net.hasId("place1"));
        assertTrue(net.hasId("arc1"));
        assertTrue(net.hasId("arc2"));

        assertFalse(net.hasId("not_exist"));
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

    @Test
    public void testRemoveElementById() {
        assertFalse(net.getElementById("place1") == null);
        net.removeElementById("place1");
        assertTrue(net.getElementById("place1") == null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddElementWithoutId() {
        Place place = new Place("");
        net.addElement(place);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddArcWithoutSource() {
        Arc arc = new Arc("empty");
        arc.setTarget(new Place("place32324"));
        net.addElement(arc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddArcWithoutTarget() {
        Arc arc = new Arc("arc1202");
        arc.setTarget(new Transition("trans131231"));
        net.addElement(arc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddElementWithExistingId() {
        final String id = "place43627";
        Place place = new Place(id);
        Transition transition = new Transition(id);

        net.addElement(place);
        assertTrue(net.hasId(id));

        net.addElement(transition);
    }

    @Test
    public void testToXML() {
        assertEquals(net.toXML(), "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\n" +
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