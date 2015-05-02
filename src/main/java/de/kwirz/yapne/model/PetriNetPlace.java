package de.kwirz.yapne.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Repräsentiert eine Stelle im Petri Netz
 */
public final class PetriNetPlace extends PetriNetNode {

    /** Stellenmarkierung */
    private int marking = 0;

    /**
     * Erstellt eine Stelle
     * @param id Kennung
     */
    public PetriNetPlace(final String id) {
        super(id);
    }

    /** Gibt Markierung zurück */
    public int getMarking() {
        return marking;
    }

    /**
     * Setzt Markierung
     * @throws IllegalArgumentException falls <b>marking</b> negativ ist
     */
    public void setMarking(int marking) {
        if (marking < 0)
            throw new IllegalArgumentException("marking cannot be negative");

        this.marking = marking;
    }

    /** {@inheritDoc} */
    @Override
    public String toPNML() {
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
                "</place>", getId(), getName(), getMarking(), getPosition().toPNML());
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("marking", marking)
                .toString();
    }

}
