package de.kwirz.yapne.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

/**
 * Abstrakte Basisklasse für Stellen und Transitionen.
 * <p>
 * Implementiert Eigenschaften die obengenannte Klassen gemeinsam haben:
 * <ul>
 * <li>Position-Eigenschaft</li>
 * <li>Erstellung von Verbindungen mit anderen Knoten</li>
 * </ul>
 * @see PetriNetPlace
 * @see PetriNetTransition
 */
public abstract class PetriNetNode extends PetriNetElement {

    /**
     * Knotenname
     */
    private String name = "";

    /**
     * Knotenposition
     */
    private Position position = new Position();

    /**
     * Eingangskanten
     */
    protected List<PetriNetArc> inputArcs = new ArrayList<>();

    /**
     * Ausgangskanten
     */
    protected List<PetriNetArc> outputArcs = new ArrayList<>();


    /**
     * Kantentyp
     * <p>
     * wird nur für interne Zwecke eingesetzt
     */
    private enum ArcType {
        /**
         * Eingangskante
         */
        INPUT_ARC,
        /**
         * Ausgangskante
         */
        OUTPUT_ARC;

        /**
         * Gibt die String Repräsentation zurück
         */
        public String toString() {
            return (this == INPUT_ARC)?"input":"output";
        }
    }


    /**
     * Repräsentiert eine Position, ein 2D Punkt, mit <b>x</b> und <b>y</b> Eigenschaften.
     */
    public static class Position {
        /**
         * Koordinaten
         */
        private int x, y;

        /**
         * Erstellt eine Position
         * @param x X Koordinate
         * @param y Y Koordinate
         * @throws IllegalArgumentException falls x oder y ist negativ
         */
        public Position(int x, int y) {
            setX(x);
            setY(y);
        }

        /**
         * Erstellt eine Position mit x=0, y=0
         */
        public Position() {
            this(0, 0);
        }

        /**
         * Setzt die Y-Koordinate
         * @throws IllegalArgumentException falls y ist negativ
         */
        public void setY(int y) {
            validateValue(y);
            this.y = y;
        }

        /**
         * Setzt die X-Koordinate
         * @throws IllegalArgumentException falls x ist negativ
         */
        public void setX(int x) {
            validateValue(x);
            this.x = x;
        }

        /**
         * Macht die Koordinatenvalidierung,
         * @param value X oder Y Koordinate
         * @throws IllegalArgumentException falls value ist negativ
         */
        private void validateValue(int value) {
            if (value < 0)
                throw new IllegalArgumentException("negative values are not accepted");
        }

        /**
         * Gibt die X-Koordinate zurück
         */
        public int getX() {
            return this.x;
        }

        /**
         * Gibt die Y-Koordinate zurück
         */
        public int getY() {
            return this.y;
        }

        /**
         * Gibt die PNML Repräsentation zurück
         */
        public String toXml() {
            return String.format("<position x=\"%d\" y=\"%d\" />", getX(), getY());
        }

        /**
         * Gibt die String Repräsentation zurück
         */
        @Override
        public String toString() {
            return String.format("Position{x=%d, y=%d}", x, y);
        }

        /**
         * Es werden X- und Y-Koordinaten verglichen.
         */
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

        /**
         * Gibt den eindeutigen Hashcode zurück
         */
        @Override
        public int hashCode() {
            return (x + 1) * 17 + (y + 1) * 31;
        }
    }

    /**
     * Erstellt einen Knoten
     * @param id Id des Knoten
     */
    public PetriNetNode(String id) {
        super(id);
    }

    /**
     * Gibt den Knotennamen zurück
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Knotennamen
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt die Position zurück
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Setzt die Position
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Fügt eine Eingangskante hinzu.
     * <p>
     * Setzt diesen Knoten als Quellknoten für die Kante <b>arc</b>.
     * @param arc Kante
     * @throws IllegalArgumentException falls es eine Verbindung mit dem Quellknoten der
     *         Kante bereits besteht
     * @see #addArc(PetriNetArc arc, ArcType arcType)
     */
    public void addInputArc(PetriNetArc arc) {
        for (PetriNetArc inputArc : inputArcs ) {
            if ( inputArc.getSource() != null && arc.getSource() == inputArc.getSource()) {
                throw new IllegalArgumentException("nodes are already connected");
            }
        }

        addArc(arc, ArcType.INPUT_ARC);
    }

    /**
     * Fügt eine Ausgangskante hinzu.
     * <p>
     * Setzt diesen Knoten als Zielknoten für die Kante <b>arc</b>.
     * @param arc Kante
     * @throws IllegalArgumentException falls es eine Verbindung mit dem Zielknoten der
     *         Kante bereits besteht
     * @see #addArc(PetriNetArc arc, ArcType arcType)
     */
    public void addOutputArc(PetriNetArc arc) {
        for (PetriNetArc outputArc : outputArcs) {
            if (arc.getTarget() != null && outputArc.getTarget() != null &&
                    arc.getTarget() == outputArc.getTarget()) {
                throw new IllegalArgumentException("nodes are already connected");
            }
        }

        addArc(arc, ArcType.OUTPUT_ARC);
    }

    /**
     * Fügt eine Kante hinzu.
     * <p>Setzt diesen Knoten als Quell bzw. Zielknoten für die Kante, abhängig von Kantentyp.
     * <p>Diese Methode macht die Arbeit für {@link #addInputArc} und {@link #addOutputArc} Methoden.
     * @param arc Kante
     * @param arcType Kantentyp
     */
    private void addArc(PetriNetArc arc, ArcType arcType) {
        List<PetriNetArc> arcs = (arcType == ArcType.INPUT_ARC)?inputArcs:outputArcs;

        if (arcs.contains(arc)) {
            logger.log(Level.WARNING,
                    String.format("arc '%s' is already an %s arc of node '%s'",
                            arc.getId(), arcType.toString(), getId()));
        } else {
            arcs.add(arc);
            logger.log(Level.INFO,
                    String.format("added %s arc '%s' to node '%s'",
                            arcType.toString(), arc.getId(), getId()));
        }

        if (arcType == ArcType.INPUT_ARC) {
            if (arc.getTarget() != this)
                arc.setTarget(this);
        } else {
            if (arc.getSource() != this)
                arc.setSource(this);
        }
    }

    /**
     * Entfernt die Eingangskante <b>arc</b>
     * @param arc Eingangskante
     * @see #removeArc(PetriNetArc arc, ArcType arcType)
     */
    public void removeInputArc(PetriNetArc arc) {
        removeArc(arc, ArcType.INPUT_ARC);
    }

    /**
     * Entfernt die Ausgangskante <b>arc</b>
     * @param arc Ausgangskante
     * @see #removeArc(PetriNetArc arc, ArcType arcType)
     */
    public void removeOutputArc(PetriNetArc arc) {
        removeArc(arc, ArcType.OUTPUT_ARC);
    }

    /**
     * Entfernt eine Kante.
     * <p>Falls die Kante <b>arc</b> eine Eingangs- oder Ausgangskante ist, wird sie entfernt
     * und Ziel- bzw. Quellknoten der Kante zurückgesetzt.
     * <p>Diese Methode macht Arbeit für {@link #removeInputArc} und {@link #removeOutputArc}
     * Methoden.
     * @param arc Kante
     * @param arcType Kantentyp
     */
    private void removeArc(PetriNetArc arc, ArcType arcType) {
        List<PetriNetArc> arcs = (arcType == ArcType.INPUT_ARC)?inputArcs:outputArcs;

        if (!arcs.contains(arc)) {
            logger.log(Level.WARNING, String.format("no such arc '%s' in %s arcs of node '%s'",
                    arc.getId(), arcType.toString(), getId()));
            return;
        }

        assert arcs.contains(arc);
        arcs.remove(arc);
        logger.log(Level.INFO, String.format("removed %s arc '%s' from node '%s'",
                        arcType.toString(), arc.getId(), getId()));

        if (arcType == ArcType.INPUT_ARC) {
            if (arc.getTarget() == this)
                arc.setTarget(null);
        } else {
            if (arc.getSource() == this)
                arc.setSource(null);
        }
    }

    /** Vergleicht Ausgangs- und Eingangskanten, Name sowie Position */
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

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (inputArcs != null ? inputArcs.hashCode() : 0);
        result = 31 * result + (outputArcs != null ? outputArcs.hashCode() : 0);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format("PetriNetNode{id='%s', name='%s', pos='%s'}",
                getId(), name, position.toString());
    }

    /**
     * Verbindet diesen Knoten als Quellknoten mit dem Knoten <b>node</b>
     * @param node Zielknoten
     */
    public void connectToNode(PetriNetNode node) {
        PetriNetArc arc = generateArc();

        arc.setSource(this);
        arc.setTarget(node);
    }

    /**
     *  Verbindet diesen Knoten als Quellknoten mit allen Knoten aus <b>nodes</b>
     *  @param nodes Zielknoten
     *  @see #connectToNode
     */
    public void connectToNodes(PetriNetNode ... nodes) {
        for (PetriNetNode node : nodes)
            connectToNode(node);
    }

    /**
     * Verbindet diesen Knoten als Zielknoten mit dem Knoten <b>node</b>
     * @param node Quellknoten
     */
    public void connectFromNode(PetriNetNode node) {
        PetriNetArc arc = generateArc();

        arc.setSource(node);
        arc.setTarget(this);
    }

    /**
     * Verbindet diesen Knoten als Zielknoten mit allen Knoten aus <b>nodes</b>
     * @param nodes Quellknoten
     * @see #connectFromNode
     */
    public void connectFromNodes(PetriNetNode ... nodes) {
        for (PetriNetNode node : nodes) {
            connectFromNode(node);
        }
    }

    /**
     * Gibt eine Kante mit zufallsgenerierten Id zurück
     */
    private PetriNetArc generateArc() {
        return new PetriNetArc("arc" + String.valueOf(new Random().nextInt(Integer.MAX_VALUE)));
    }

}
