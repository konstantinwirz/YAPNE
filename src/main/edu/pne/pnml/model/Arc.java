package edu.pne.pnml.model;

/**
 * Created by konstantin on 24/10/14.
 */
public class Arc extends Element {

    private Element source = null;
    private Element target = null;

    public Arc(final String id) {
        super(id);
    }

    public Element getSource() {
        return source;
    }

    public void setSource(Element source) {
        validateElement(source);
        validateSource(source);
        this.source = source;
    }

    public Element getTarget() {
        return target;
    }

    public void setTarget(Element target) {
        validateElement(target);
        validateTarger(target);
        this.target = target;
    }

    public String toXML() {
        return String.format("<arc id=\"%s\" source=\"%s\" target=\"%s\"> </arc>", getId(),
                getSource()!=null?getSource().getId():"", getTarget()!=null?getTarget().getId():"");
    }

    private void validateElement(final Element element) {
        if ( !(element instanceof Place || element instanceof Transition) ) {
            throw new IllegalArgumentException("item is not an instance of Place or Transition");
        }
    }

    private void validateSource(final Element source) {
        if (this.target != null && target.getClass().equals(source.getClass()))
            throw new IllegalArgumentException("source cannot be of same type as target");
    }

    private void validateTarger(final Element target) {
        if (this.source != null && source.getClass().equals(target.getClass()))
            throw new IllegalArgumentException("target cannot be of same type as source");
    }

    @Override
    public String getName() {
        throwNotSupportedPropertyException("name");
        return null;
    }

    @Override
    public void setName(final String name) {
        throwNotSupportedPropertyException("name");
    }

    @Override
    public Position getPosition() {
        throwNotSupportedPropertyException("position");
        return null;
    }

    @Override
    public void setPosition(final Position position) {
        throwNotSupportedPropertyException("position");
    }

    private void throwNotSupportedPropertyException(final String property) {
        throw new UnsupportedOperationException("arc does not support '" + property + "' property");
    }
}
