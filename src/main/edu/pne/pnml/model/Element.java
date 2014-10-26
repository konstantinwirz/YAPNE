package edu.pne.pnml.model;

/**
 * Created by konstantin on 24/10/14.
 */
public abstract class Element {

    private String id;

    public Element(final String id) {
        this.id = id;
    }

    String getId() {
        return id;
    }
}
