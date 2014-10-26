package edu.pne.pnml.model;

/**
 * Created by konstantin on 24/10/14.
 */
public class Place extends Element {

    private String name = "";
    private int marking = 0;
    private Position position = new Position();

    public Place(final String id) {
        super(id);
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public int getMarking() {
        return marking;
    }

    public void setMarking(int marking) {
        if (marking < 0) {
            throw new IllegalArgumentException("marking cannot be negative");
        }
        this.marking = marking;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public String toXML() {
        return String.format("<place id=\"%s\">\n" +
                             "<name>\n" +
                             "<value>%s</ value>\n" +
                            "</name> <initialMarking>\n" +
                            "<token>\n" +
                            "<value>%d</ value>\n" +
                            "</token>\n" +
                            "</ initialMarking> <graphics>\n" +
                            "%s </ graphics>\n" +
                            "</place>", getId(), getName(), getMarking(), getPosition().toXML());
    }

}
