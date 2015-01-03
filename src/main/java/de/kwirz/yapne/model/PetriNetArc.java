package de.kwirz.yapne.model;

/**
 * Representiert eine Kante im Petri Netz
 */
public class PetriNetArc extends PetriNetElement {

    private PetriNetNode source = null;
    private PetriNetNode target = null;

    public PetriNetArc(final String id) {
        super(id);
    }

    public PetriNetNode getSource() {
        return source;
    }

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

    public PetriNetNode getTarget() {
        return target;
    }

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

    @Override
    public String toXml() {
        return String.format("<arc id=\"%s\" source=\"%s\" target=\"%s\"> </arc>", getId(),
                getSource()!=null?getSource().getId():"", getTarget()!=null?getTarget().getId():"");
    }

    @Override
    public String toString() {
        return String.format("%s { id='%s', target='%s', source='%s' ",
                getId(),
                getClass().getSimpleName(),
                target == null?"":target.getName(),
                source == null? "":source.getName());
    }
}
