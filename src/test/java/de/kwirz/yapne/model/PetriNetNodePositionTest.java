package de.kwirz.yapne.model;


import de.kwirz.yapne.model.PetriNetNode;
import org.junit.Test;
import static org.junit.Assert.*;


public class PetriNetNodePositionTest {

    @Test
    public void testConstructorWithoutArguments() {
        PetriNetNode.Position position = new PetriNetNode.Position();
        assertEquals(position.getX(),0);
        assertEquals(position.getY(), 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorWithNegativeValues() {
        PetriNetNode.Position pos = new PetriNetNode.Position(-1, 0);
    }

    @Test
    public void testConstructorWithValidValues() {
        PetriNetNode.Position pos = new PetriNetNode.Position(25, 30);
        assertEquals(pos.getX(), 25);
        assertEquals(pos.getY(), 30);
    }

    @Test
    public void testEquals() {
        assertEquals(new PetriNetNode.Position(25, 35), new PetriNetNode.Position(25, 35));
        assertNotEquals(new PetriNetNode.Position(25, 35), new PetriNetNode.Position(25, 36));
        assertNotEquals(new PetriNetNode.Position(25, 35), new PetriNetNode.Position(26, 35));
    }

    @Test
    public void testToXML() {
        PetriNetNode.Position position = new PetriNetNode.Position(100, 200);
        assertEquals(position.toXml(), "<position x=\"100\" y=\"200\" />");
    }

}