package edu.pne.model;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 * Created by konstantin on 24/10/14.
 */
public class TestTransition {


    @Test
    public void testToXML() {
        Transition transition = new Transition("transition1");
        transition.setName("a");
        transition.setPosition(new Position(200, 100));

        assertEquals(transition.toXML(), "<transition id=\"transition1\">\n" +
                                         "<name>\n" +
                                         "<value>a</ value>\n" +
                                         "</name> <graphics>\n" +
                                         "<position x=\"200\" y=\"100\" /> </ graphics>\n" +
                                         "</ transition>" );
    }
}
