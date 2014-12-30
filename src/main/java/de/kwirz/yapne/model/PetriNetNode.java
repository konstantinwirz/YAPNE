package de.kwirz.yapne.model;

/**
 * Created by konstantin on 25/11/14.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

/**
 * Abstrakte Bassisklasse für Stellen und Transitionen
 * @see PetriNetPlace
 * @see PetriNetTransition
 */
public abstract class PetriNetNode extends PetriNetElement {
    private String name = "";
    private Position position = new Position();
    protected List<PetriNetArc> inputArcs = new ArrayList<>();
    protected List<PetriNetArc> outputArcs = new ArrayList<>();


    private enum ArcType {
        INPUT_ARC, OUTPUT_ARC;

        public String toString() {
            return (this == INPUT_ARC)?"input":"output";
        }
    }


    public static class Position {
        private int x, y;

        public Position(int x, int y) {
            setX(x);
            setY(y);
        }

        public Position() {
            this(0, 0);
        }

        public void setY(int y) {
            validateValue(y);
            this.y = y;
        }

        public void setX(int x) {
            validateValue(x);
            this.x = x;
        }

        private void validateValue(int value) {
            if (value < 0)
                throw new IllegalArgumentException("negative values are not accepted");
        }

        public int getX() {
            return this.x;
        }

        public int getY() {
            return this.y;
        }

        public String toXml() {
            return String.format("<position x=\"%d\" y=\"%d\" />", getX(), getY());
        }

        @Override
        public String toString() {
            return String.format("Position{x=%d, y=%d}", x, y);
        }

        @Override
        public boolean equals(Object other) {
            if (other == null)
                return false;

            if (other == this)
                return true;

            if ( !(other instanceof Position) )
                return false;

            Position position = (Position) other;
            return position.getX() == this.getX() && position.getY() == this.getY();
        }

        @Override
        public int hashCode() {
            return (x + 1) * 17 + (y + 1) * 31;
        }
    }



    public PetriNetNode(String id) {
        super(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void addInputArc(PetriNetArc arc) {
        for (PetriNetArc inputArc : inputArcs ) {
            if ( inputArc.getSource() != null && arc.getSource() == inputArc.getSource()) {
                throw new IllegalArgumentException("Knoten sind bereits verbunden");
            }
        }

        addArc(arc, ArcType.INPUT_ARC);
    }

    public void removeInputArc(PetriNetArc arc) {
        removeArc(arc, ArcType.INPUT_ARC);
    }

    private void removeArc(PetriNetArc arc, ArcType arcType) {
        if (arc == null)
            throw new NullPointerException("passed a null arc");
        List<PetriNetArc> arcs = (arcType == ArcType.INPUT_ARC)?inputArcs:outputArcs;

        if (!arcs.contains(arc)) {
            logger.log(Level.WARNING, String.format("no such arc '%s' in %s arcs of node '%s'",
                    arc.getId(), arcType.toString(), getId()));
            return;
        }

        assert arcs.contains(arc);
        arcs.remove(arc);
        logger.log(Level.INFO,
                String.format("removed %s arc '%s' from node '%s'", arcType.toString(), arc.getId(), getId()));

        if (arcType == ArcType.INPUT_ARC) {
            if (arc.getTarget() == this)
                arc.setTarget(null);
        } else {
            if (arc.getSource() == this)
                arc.setSource(null);
        }
    }

    public void addOutputArc(PetriNetArc arc) {
        for (PetriNetArc outputArc : outputArcs) {
            if (arc.getTarget() != null && outputArc.getTarget() != null &&
                    arc.getTarget() == outputArc.getTarget()) {
                throw new IllegalArgumentException("Zwischen Knoten besteht bereits eine Verbindung");
            }
        }

        addArc(arc, ArcType.OUTPUT_ARC);
    }

    private void addArc(PetriNetArc arc, ArcType arcType) {
        if (arc == null)
            throw new NullPointerException("passed a null arc");

        List<PetriNetArc> arcs = (arcType == ArcType.INPUT_ARC)?inputArcs:outputArcs;

        if (arcs.contains(arc)) {
            logger.log(Level.WARNING,
                    String.format("arc '%s' is already an %s arc of node '%s'", arc.getId(), arcType.toString(), getId()));
        } else {
            arcs.add(arc);
            logger.log(Level.INFO,
                    String.format("added %s arc '%s' to node '%s'", arcType.toString(), arc.getId(), getId()));
        }

        if (arcType == ArcType.INPUT_ARC) {
            if (arc.getTarget() != this)
                arc.setTarget(this);
        } else {
            if (arc.getSource() != this)
                arc.setSource(this);
        }
    }

    public void removeOutputArc(PetriNetArc arc) {
        removeArc(arc, ArcType.OUTPUT_ARC);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PetriNetNode)) return false;
        if (!super.equals(o)) return false;

        PetriNetNode that = (PetriNetNode) o;

        if (inputArcs != null ? !inputArcs.equals(that.inputArcs) : that.inputArcs != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (outputArcs != null ? !outputArcs.equals(that.outputArcs) : that.outputArcs != null) return false;
        if (position != null ? !position.equals(that.position) : that.position != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (inputArcs != null ? inputArcs.hashCode() : 0);
        result = 31 * result + (outputArcs != null ? outputArcs.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return String.format("PetriNetNode{id='%s', name='%s', pos='%s'}",
                getId(), name, position.toString());
    }


    /**
     * Verbindet 2 Knoten (this -> node) mit Hilfe einer Kante
     * @param node Zielknoten
     * */
    public void connectToNode(PetriNetNode node) {
        PetriNetArc arc =
                new PetriNetArc("arc" + String.valueOf(new Random().nextInt(Integer.MAX_VALUE)));

        arc.setSource(this);
        arc.setTarget(node);
    }



}
