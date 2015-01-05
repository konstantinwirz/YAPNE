package de.kwirz.yapne.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PetriNetNodeTest {

    private class FakePetriNode1 extends PetriNetNode {

        public FakePetriNode1(String id) {
            super(id);
        }

        @Override
        public String toPNML() {
            return "";
        }
    }

    private class FakePetriNode2 extends PetriNetNode {

        public FakePetriNode2(String id) {
            super(id);
        }

        @Override
        public String toPNML() {
            return "";
        }
    }


    private PetriNetNode node1, node2;

    @Before
    public void setUp() {
        node1 = new FakePetriNode1("node1");
        node2 = new FakePetriNode2("node2");
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullInputArc() {
        node1.addInputArc(null);
    }

    @Test(expected = NullPointerException.class)
    public void testAddNullOutputArc() {
        node1.addOutputArc(null);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullInputArc() {
        node1.removeInputArc(null);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullOutputArc() {
        node1.removeOutputArc(null);
    }

    @Test
    public void testAddInputArc() {
        PetriNetArc arc = new PetriNetArc("arc1");
        node1.addInputArc(arc);
        assertEquals(arc.getTarget(), node1);
    }

    @Test
    public void testAddOutputArc() {
        PetriNetArc arc = new PetriNetArc("arc1");
        node1.addOutputArc(arc);
        assertEquals(arc.getSource(), node1);
    }

    @Test
    public void testRemoveOutputArc() {
        PetriNetArc arc = new PetriNetArc("arc1");
        node1.addOutputArc(arc);
        assertEquals(node1, arc.getSource());
        node1.removeOutputArc(arc);
        assertEquals(null, arc.getSource());
    }

    @Test
    public void testRemoveInputArc() {
        PetriNetArc arc = new PetriNetArc("arc1");
        node1.addInputArc(arc);
        assertEquals(node1, arc.getTarget());
        node1.removeInputArc(arc);
        assertEquals(null, arc.getTarget());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultipleArcsBetweenSameNodes() {
        PetriNetArc arc1 = new PetriNetArc("arc1");
        PetriNetArc arc2 = new PetriNetArc("arc2");

        node1.addOutputArc(arc1);
        node2.addInputArc(arc1);

        node1.addOutputArc(arc2);
        node2.addInputArc(arc2);
    }

    @Test
    public void testConnectToNode() {
        assertTrue(node1.outputArcs.isEmpty());
        assertTrue(node2.inputArcs.isEmpty());

        node1.connectToNode(node2);

        assertEquals(node1.outputArcs.size(), 1);
        assertEquals(node2.inputArcs.size(), 1);
    }

}