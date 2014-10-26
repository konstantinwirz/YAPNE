package edu.pne.model;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * Created by konstantin on 23/10/14.
 */
public class TestPosition {

    @Test
    public void testConstructorWithoutArguments() {
        Position position = new Position();
        assertEquals(position.getX(),0);
        assertEquals(position.getY(), 0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorWithNegativeValues() {
        Position pos = new Position(-1, 0);
    }

    @Test
    public void testConstructorWithValidValues() {
        Position pos = new Position(25, 30);
        assertEquals(pos.getX(), 25);
        assertEquals(pos.getY(), 30);
    }

    @Test
    public void testToXML() {
        Position position = new Position(100, 200);
        assertEquals(position.toXML(), "<position x=\"100\" y=\"200\" />");
    }

}
