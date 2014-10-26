package edu.pne.pnml.model;

/**
 * Created by konstantin on 24/10/14.
 */
public class Transition extends Element {

    private String name;
    private Position position;

    public Transition(String id) {
        super(id);
        setName("");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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
