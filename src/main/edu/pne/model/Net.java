package edu.pne.model;


import com.sun.javaws.exceptions.InvalidArgumentException;
import com.sun.tools.corba.se.idl.InvalidArgument;

import java.util.ArrayList;

/**
 * Created by konstantin on 26/10/14.
 */
public class Net {

    private ArrayList<Element> elements = null;


    public Net() {
        elements = new ArrayList<Element>();
    }

    public void addElement(Element element) {
        if (element.getId().isEmpty() || element == null)
            throw new IllegalArgumentException("passed invalid element");

        if (hasId(element.getId()))
            throw new IllegalArgumentException("id already exists");

        validateElement(element);

        elements.add(element);
    }

    private void validateElement(final Element element) {
        if (element == null || element.getId().isEmpty()) {
            throw new IllegalArgumentException("invalid element passed");
        }

        if (element instanceof Arc) {
            validateArc((Arc)element);
        } else if (element instanceof Place) {
            validatePlace((Place)element);
        } else if (element instanceof Transition) {
            validateTransition((Transition)element);
        } else {
            throw new UnsupportedOperationException("cannot validate given type");
        }
    }

    private void validateArc(final Arc arc) {
        if (arc == null || arc.getSource() == null || arc.getTarget() == null) {
            throw new IllegalArgumentException("invalid arc passed");
        }

        if (!hasId(arc.getSource().getId()) || !hasId(arc.getTarget().getId())) {
            throw new IllegalArgumentException("has no arc's source id or target id ");
        }
    }

    private void validatePlace(final Place place) {
    }

    private void validateTransition(final Transition transition) {
    }

    public ArrayList<Element> getElements() {
        return elements;
    }

    public void clear() {
        elements.clear();
    }

    public boolean hasId(final String id) {
        return getElementById(id) != null;
    }

    public Element getElementById(final String id) {
        Element element = null;
        for (Element el : elements) {
            if (el.getId() == id) {
                element = el;
                break;
            }
        }

        return element;
    }

    public void removeElementById(final String id) {
        Element toRemove = getElementById(id);
        if (toRemove != null) {
            elements.remove(toRemove);
        }
    }
}
