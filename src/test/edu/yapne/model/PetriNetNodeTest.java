package edu.yapne.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PetriNetNodeTest {

    private class FakePetriNode extends PetriNetNode {

        public FakePetriNode(String id) {
            super(id);
        }

        @Override
        public String toXml() {
            return null;
        }
    }


    private PetriNetNode node1, node2;

    @Before
    public void setUp() {
        node1 = new FakePetriNode("node1");
        node2 = new FakePetriNode("node2");
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

}