package de.kwirz.yapne.io;

import de.kwirz.yapne.io.PnmlParser;
import de.kwirz.yapne.model.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


public class PnmlParserTest {

    private PnmlParser parser = null;

    @Before
    public void setUp() {
        parser = new PnmlParser();
    }

    @Test(expected = RuntimeException.class)
    public void testParseEmptySource() {
        PetriNet net = parser.parse("");
    }

    @Test(expected = RuntimeException.class)
    public void testParseInvalidSource() {
        final String input = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\n" +
                "<pnml>\n" +
                "  <net>\n" +
                "<pnml>";
        parser.parse(input);
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

        PetriNet net = parser.parse(input);

        assertEquals(net.getIds().size(), 5);

        PetriNetTransition transition1 = (PetriNetTransition)net.getElementById("transition1");
        assertEquals(transition1.getName(), "a");
        assertEquals(transition1.getPosition(), new PetriNetNode.Position(200, 100));

        PetriNetTransition transition2 = (PetriNetTransition)net.getElementById("transition2");
        assertEquals(transition2.getName(), "b");
        assertEquals(transition2.getPosition(), new PetriNetNode.Position(200, 300));

        PetriNetPlace place1 = (PetriNetPlace)net.getElementById("place1");
        assertEquals(place1.getName(), "p1");
        assertEquals(place1.getMarking(), 0);
        assertEquals(place1.getPosition(), new PetriNetNode.Position(400, 250));

        PetriNetArc arc1 = (PetriNetArc)net.getElementById("arc1");
        assertEquals(arc1.getSource(), transition1);
        assertEquals(arc1.getTarget(), place1);

        PetriNetArc arc2 = (PetriNetArc)net.getElementById("arc2");
        assertEquals(arc2.getSource(), place1);
        assertEquals(arc2.getTarget(), transition2);
    }

}