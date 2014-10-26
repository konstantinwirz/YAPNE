package edu.pne.pnml.model;

/**
 * Created by konstantin on 24/10/14.
 */
public class Transition extends Element {

    public Transition(String id) {
        super(id);
    }

    public String toXML() {
        return String.format("<transition id=\"%s\">\n" +
                             "<name>\n" +
                             "<value>%s</ value>\n" +
                             "</name> <graphics>\n" +
                             "%s </ graphics>\n" +
                             "</ transition>", getId(), getName(), getPosition().toXML());
    }
}
