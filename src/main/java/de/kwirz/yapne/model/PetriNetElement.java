package de.kwirz.yapne.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.logging.Logger;

/**
 * Abstrakte Basisklasse für Stellen, Transitionen und Kanten.
 * <p>
 * Klassen die von dieser erben müssen {@link #toPNML} Methode implementieren, die
 * eine <b>PNML</b> Repräsentation zurückgibt.
 *
 *
 * @see PetriNetPlace
 * @see PetriNetTransition
 * @see PetriNetArc
 *
 */
public abstract class PetriNetElement implements PNMLable {

    /**
     * Wird zum Loggen verwendet
     */
    protected final static Logger logger = Logger.getLogger(PetriNetElement.class.getName());

    /**
     * Kennung des Elements, wird beim Konstruieren gesetzt und darf nicht mehr verändert werden.
     */
    private final String id;

    /**
     * Erstellt eine Instanz von PetriNetElement
     * @param id Kennung, ist eine Read-Only Eigenschaft
     */
    public PetriNetElement(String id) {
        if (id == null || id.trim().isEmpty()) {
           throw new IllegalArgumentException("invalid id passed");
        }

        this.id = id;
        logger.info(String.format("created a '%s' with id '%s'",
                this.getClass().getName(), this.getId()));
    }

    /**
     * Gibt die Id zurück
     */
    public String getId() {
        return this.id;
    }

    /**
     * in dieser Implementierung werden nur Id's verglichen
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PetriNetElement that = (PetriNetElement) o;

        return new EqualsBuilder().append(id, that.id).isEquals();
    }

    /** Gibt den Hashcode zurück */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(31, 17)
                .append(id)
                .hashCode();
    }

    /** Gibt die String Repräsentation zurück */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .toString();
    }
}
