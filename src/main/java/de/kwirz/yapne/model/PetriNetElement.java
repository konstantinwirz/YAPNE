package de.kwirz.yapne.model;

import java.util.logging.Logger;

/**
 * Ist eine abstrakte Basisklasse für Stellen, Transitionen und Kanten
 *
 * @see de.kwirz.yapne.model.PetriNetPlace
 * @see de.kwirz.yapne.model.PetriNetTransition
 * @see de.kwirz.yapne.model.PetriNetArc
 *
 */
public abstract class PetriNetElement {
    protected final static Logger logger = Logger.getLogger(PetriNetElement.class.getName());
    private final String id;

    /**
     * Erstellt eine Instanz von PetriNetElement
     * @param id ist eine Read-Only Eigenschaft
     */
    public PetriNetElement(String id) {
        if (id == null || id.trim().isEmpty()) {
           throw new IllegalArgumentException("invalid id passed");
        }

        this.id = id;
        logger.info(String.format("created a '%s' with id '%s'", this.getClass().getName(), this.getId()));
    }

    /**
     * Liefert den Id
     * @return id
     */
    public String getId() {
        return this.id;
    }

    /**
     * Liefert XML Representation von diesem Element
     * @return
     */
    public abstract String toXml();


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PetriNetElement that = (PetriNetElement) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("PetriNetElement{id='%s'}", id);
    }
}
