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

    @Override
    public boolean equals(final Object other) {
        if (this == other)
            return true;

        if (other instanceof Element)
            return this.id.equals(((Element)other).getId()) &&
                    this.name.equals(((Element)other).getName()) &&
                    this.position.equals(((Element)other).getPosition());

        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode() + name.hashCode() + position.hashCode();

    }

    public abstract String toXML();
}
