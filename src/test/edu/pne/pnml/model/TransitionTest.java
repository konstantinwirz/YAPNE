package edu.pne.pnml.model;

import static org.junit.Assert.*;
import org.junit.Test;


/**
 * Created by konstantin on 24/10/14.
 */
public class TransitionTest {


    @Test
    public void testToXML() {
        Transition transition = new Transition("transition1");
        transition.setName("a");
        transition.setPosition(new Position(200, 100));

        assertEquals(transition.toXML(), "<transition id=\"transition1\">\n" +
                                         "<name>\n" +
                                         "<value>a</value>\n" +
                                         "</name>\n" +
                                         "<graphics>\n" +
                                         "<position x=\"200\" y=\"100\" />\n" +
                                         "</graphics>\n" +
                                         "</transition>" );
    }
}
