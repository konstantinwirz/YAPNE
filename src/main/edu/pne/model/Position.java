package edu.pne.model;

/**
 * Created by konstantin on 23/10/14.
 */
public class Position {

    private int x, y;

    public Position(int x, int y) {
        setX(x);
        setY(y);
    }

    public Position() {
       this(0, 0);
    }

    public void setY(int y) {
        if (y < 0) {
            throw new IllegalArgumentException("Negative values are not accepted");
        }
        this.y = y;
    }

    public void setX(int x) {
        if (x < 0) {
            throw new IllegalArgumentException("Negative values are not accepted");
        }
        this.x = x;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toXML() {
        return String.format("<position x=\"%d\" y=\"%d\" />", getX(), getY());
    }

    public String toString() {
        return toXML();
    }

}
