package de.kwirz.yapne.utils;


/**
 * Wird in Buildern eingesetzt, da es oft wichtig ist zu wissen ob der Wert überhaupt
 * gesetzt wurde. In diesem Fall ist <code>null</code> nicht ausreichend, da er auch
 * als gültiger Wert gilt.
 */
public class BuilderValue<T> {

    /**
     * Wert
     */
    private T value;

    /**
     * Wurde der Wert geschrieben?
     */
    private boolean isSet = false;

    /**
     * Setzt den Wert.
     * <p>
     * Danach ist <code>isSet=true</code>
     */
    public void setValue(T value) {
        this.value = value;
        this.isSet = true;
    }

    /**
     * Gibt den Wert zurück
     */
    public T getValue() {
        return this.value;
    }

    /**
     * Gibt <code>true</code> zurück falls der Wert gesetzt wurde.
     */
    public boolean isSet() {
        return this.isSet;
    }
}