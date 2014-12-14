package de.kwirz.yapne.model;

/**
 * Created by konstantin on 26/11/14.
 */
public class PetriNetPlace extends PetriNetNode {
    private int marking = 0;

    public PetriNetPlace(final String id) {
        super(id);
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

    @Override
    public String toXml() {
        return String.format("<place id=\"%s\">\n" +
                "<name>\n" +
                "<value>%s</value>\n" +
                "</name>\n" +
                "<initialMarking>\n" +
                "<token>\n" +
                "<value>%d</value>\n" +
                "</token>\n" +
                "</initialMarking>\n" +
                "<graphics>\n" +
                "%s\n" +
                "</graphics>\n" +
                "</place>", getId(), getName(), getMarking(), getPosition().toXml());
    }
}
