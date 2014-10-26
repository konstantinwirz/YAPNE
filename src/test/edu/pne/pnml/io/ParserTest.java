package edu.pne.pnml.io;

import edu.pne.pnml.model.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class ParserTest {

    private Parser parser = null;

    @Before
    public void setUp() {
        parser = new Parser();
    }

    @Test
    public void testParseInvalidInput() {
        Net net = parser.parse("");

        assertTrue(net.getElements().isEmpty());
    }


    @Test
    public void testParseValidInput() {
        final String input = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\n" +
                "<pnml>\n" +
                "  <net>\n" +
                "    <transition id=\"transition1\">\n" +
                "      <name>\n" +
                "        <value>a</value>\n" +
                "      </name>\n" +
                "      <graphics>\n" +
                "        <position x=\"200\" y=\"100\"/>\n" +
                "      </graphics>\n" +
                "    </transition>\n" +
                "    <transition id=\"transition2\">\n" +
                "      <name>\n" +
                "        <value>b</value>\n" +
                "      </name>\n" +
                "      <graphics>\n" +
                "        <position x=\"200\" y=\"300\"/>\n" +
                "      </graphics>\n" +
                "    </transition>\n" +
                "    <place id=\"place1\">\n" +
                "      <name>\n" +
                "        <value>p1</value>\n" +
                "      </name>\n" +
                "      <initialMarking>\n" +
                "        <token>\n" +
                "          <value>0</value>\n" +
                "        </token>\n" +
                "      </initialMarking>\n" +
                "      <graphics>\n" +
                "        <position x=\"400\" y=\"250\"/>\n" +
                "      </graphics>\n" +
                "    </place>\n" +
                "    <arc id=\"arc1\" source=\"transition1\" target=\"place1\">\n" +
                "    </arc>\n" +
                "    <arc id=\"arc2\" source=\"place1\" target=\"transition2\">\n" +
                "    </arc>\n" +
                "  </net>\n" +
                "</pnml>";

        Net net = parser.parse(input);

        assertEquals(net.getElements().size(), 5);

        Transition transition1 = (Transition)net.getElementById("transition1");
        assertEquals(transition1.getName(), "a");
        assertEquals(transition1.getPosition(), new Position(200, 100));

        Transition transition2 = (Transition)net.getElementById("transition2");
        assertEquals(transition2.getName(), "b");
        assertEquals(transition2.getPosition(), new Position(200, 300));

        Place place1 = (Place)net.getElementById("place1");
        assertEquals(place1.getName(), "p1");
        assertEquals(place1.getMarking(), 0);
        assertEquals(place1.getPosition(), new Position(400, 250));

        Arc arc1 = (Arc)net.getElementById("arc1");
        assertEquals(arc1.getSource(), transition1);
        assertEquals(arc1.getTarget(), place1);

        Arc arc2 = (Arc)net.getElementById("arc2");
        assertEquals(arc2.getSource(), place1);
        assertEquals(arc2.getTarget(), transition2);
    }

}