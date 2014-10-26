package edu.pne.pnml.model;

/**
 * Created by konstantin on 24/10/14.
 */
public class Arc extends Element {

    private Element source = null;
    private Element target = null;

    private void validateArg(final Element element) {
        if ( !(element instanceof Place || element instanceof Transition) ) {
            throw new IllegalArgumentException("item is not a instance of Place or Transition");
        }
    }

    public Arc(final String id) {
        super(id);
    }

    public Element getSource() {
        return source;
    }

    public void setSource(Element source) {
        validateArg(source);
        this.source = source;
    }

    public Element getTarget() {
        return target;
    }

    public void setTarget(Element target) {
        validateArg(target);
        this.target = target;
    }

    public String toXML() {
        return String.format("<arc id=\"%s\" source=\"%s\" target=\"%s\"> </ arc>", getId(),
                getSource()!=null?getSource().getId():"", getTarget()!=null?getTarget().getId():"");
    }
}
