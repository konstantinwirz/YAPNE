package edu.yapne.utils;

/**
 * Created by konstantin on 18/11/14.
 */

public class BuilderValue<T> {
    private T value;
    private boolean isSet = false;

    public void setValue(T value) {
        this.value = value;
        this.isSet = true;
    }

    public T getValue() {
        return this.value;
    }

    public boolean isSet() {
        return this.isSet;
    }
}