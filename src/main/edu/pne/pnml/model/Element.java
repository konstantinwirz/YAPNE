package edu.pne.pnml.model;

/**
 * Created by konstantin on 24/10/14.
 */
public abstract class Element {

    private String id;
    private String name;
    private Position position;

    public Element(final String id) {
        this.id = id;
        this.name = "";
        position = new Position(0, 0);
    }

    String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(final Position position) {
        this.position = position;
    }

    public abstract String toXML();
}
