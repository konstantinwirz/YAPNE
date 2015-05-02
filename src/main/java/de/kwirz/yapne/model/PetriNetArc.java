package de.kwirz.yapne.model;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Repräsentiert eine Kante im Petri Netz
 */
public final class PetriNetArc extends PetriNetElement {

    /** Quellknoten */
    private PetriNetNode source = null;

    /** Zielknoten */
    private PetriNetNode target = null;

    /**
     * Erstellt eine Kante
     * @param id Kennung dieser Kante
     */
    public PetriNetArc(final String id) {
        super(id);
    }

    /** Gibt Quellknoten zurück  */
    public PetriNetNode getSource() {
        return source;
    }

    /**
     * Setzt den Quellknoten.
     * <p>Diese Kante wird bei dem Knoten <b>source</b> als Ausgangskante gesetzt, falls
     * ein Quellknoten bereits gesetzt ist, wird diese Kante aus seiner Ausgangskantenliste
     * entfernt und er wird überschrieben.
     * @param source Quellknoten
     * @throws IllegalArgumentException falls Quell- und Zielknoten vom selben Typ sind
     */
    public void setSource(PetriNetNode source) {
        if (this.source == source)
            return;

        if (this.target != null && source != null && target.getClass().equals(source.getClass()))
            throw new IllegalArgumentException("source cannot be of same type as target");

        if (this.source != null)
            this.source.removeOutputArc(this);

        this.source = source;

        if (this.source != null)
            this.source.addOutputArc(this);
    }

    /** Gibt Zielknoten zurück  */
    public PetriNetNode getTarget() {
        return target;
    }

    /**
     * Setzt den Zielknoten.
     * <p>Diese Kante wird bei dem Knoten <b>target</b> als Eingangskante gesetzt, falls
     * ein Zielknoten bereits gesetzt ist, wird diese Kante aus seiner Eingangskantenliste
     * entfernt und er wird überschrieben.
     * @param target Zielknoten
     * @throws IllegalArgumentException falls Quell- und Zielknoten vom selben Typ sind
     */
    public void setTarget(PetriNetNode target) {
        if (this.target == target)
            return;

        if (this.source != null && target != null && source.getClass().equals(target.getClass()))
            throw new IllegalArgumentException("target cannot be of same type as source");

        if (this.target != null)
            this.target.removeInputArc(this);

        this.target = target;

        if (this.target != null)
            this.target.addInputArc(this);
    }

    /** {@inheritDoc} */
    @Override
    public String toPNML() {
        return String.format("<arc id=\"%s\" source=\"%s\" target=\"%s\"> </arc>", getId(),
                getSource()!=null?getSource().getId():"", getTarget()!=null?getTarget().getId():"");
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .appendSuper(super.toString())
                .append("target", target)
                .append("source", source)
                .toString();
    }

}
