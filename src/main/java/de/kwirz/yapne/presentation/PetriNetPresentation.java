package de.kwirz.yapne.presentation;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.kwirz.yapne.model.*;
import de.kwirz.yapne.utils.Settings;
import de.kwirz.yapne.utils.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.DropShadowBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import sun.nio.cs.UTF_32LE;


/**
 * Grafische Darstellung eines Petri Netzes.
 * <p>
 * Bildet Elemente der Klasse {@link de.kwirz.yapne.model.PetriNet}, die hier als Model
 * eingesetzt wird, auf ihre Präsentationen ab, zeichnet sie und bietet folgende Operationen an:
 * <ul>
 *     <li>Erstellen und Entfernen von Transitionen, Stellen und Kanten</li>
 *     <li>Verschieben von Transitionen und Stellen</li>
 *     <li>Aus- un Ab -wählen von Transitionen, Stellen und Kanten</li>
 * </ul>
 *
 */
public class PetriNetPresentation extends Pane {

    /** wird zum Loggen verwendet */
    private static final Logger logger = Logger.getLogger(PetriNetPresentation.class.getName());

    /** Ein {@link PetriNet} dient als Model */
    private PetriNet model = new PetriNet();

    /**
     * Anzahl Stellen im Netz.
     * <p>
     * Wird für automatische Generierung von Stellen Ids verwendet.
     */
    private int placeCounter = 0;

    /**
     *
     * Anzahl Transitionen im Netz
     * <p>
     * Wird für automatische Generierung von Transitionen Ids verwendet.
     */
    private int transitionCounter = 0;

    /**
     *
     * Anzahl der Kanten im Netz
     * <p>
     * Wird für automatische Generierung von Kanten Ids verwendet.
     */
    private int arcCounter = 0;

    /**  Managt Mausklicks für jedes Element des Netzes */
    private EventHandler<? super MouseEvent> mouseClickedEventHandler;
    /** Managt Maus Dragged Events für jedes Element des Netzes */
    private EventHandler<? super MouseEvent> mouseDraggedEventHandler;

    /** Id aktuell ausgewähltes Elements */
    private String selectedElementId = null;

    /**
     * Erstellt eine <b>PetriNetPresentation</b>
     */
    public PetriNetPresentation() {

    }

    /**
     * Setzt den Model.
     * Modelelemente werden neu gezeichnet
     */
    public void setModel(PetriNet model) {
        this.model = model;
        reload();
    }

    /**
     * Erstellt Präsentationen aus den Modelelementen und zeichnet sie.
     * <p>
     * <ol>
     * <li>Alle Präsentationen werden entfernt.</li>
     * <li>Erstelle Präsentationen für Stellen und Transitionen aus dem Model</li>
     * <li>Erstelle Präsentationen für Kanten aus dem Model</li>
     * <li>Installiere Maus Events Handler für jede Präsentation</li>
     * </ol>
     * <p>
     * Kantenpräsentationen werden zuletzt erstellt um sicher zu gehen dass alle Ziel- und
     * Quellknoten bereits als Präsentationen existieren.
     */
    public void reload() {
        getChildren().clear();

        placeCounter = 0;
        transitionCounter = 0;
        arcCounter = 0;

        Settings settings = Settings.getInstance();
        double strokeWidth = getStrokeWidthFromSettings();
        double nodeSize = getNodeSizeFormSettings();

        // at first create places and transitions then create arcs
        for (final String id : model.getIds()) {
            PetriNetElement element = model.getElementById(id);
            assert element != null;
            assert lookup("#" + element.getId()) == null;

            PetriNetElementPresentation presentation = null;
            if (element instanceof PetriNetPlace) {
                presentation = PetriNetPlacePresentationBuilder.create()
                        .model((PetriNetPlace) element)
                        .size(nodeSize)
                        .strokeWidth(strokeWidth)
                        .build();
                ++placeCounter;
                ((PetriNetPlacePresentation) presentation).markingProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                        reload();
                    }
                });
            } else if (element instanceof PetriNetTransition) {
                presentation = PetriNetTransitionPresentationBuilder.create()
                        .model((PetriNetTransition) element)
                        .size(nodeSize)
                        .strokeWidth(strokeWidth)
                        .build();
                ++transitionCounter;
            }

            if (presentation != null) {
                ((Node) presentation).setId(normalizeId(id));
                getChildren().add((Node) presentation);
                ((Node) presentation).addEventHandler(OccurrenceEvent.OCCURRED, new EventHandler<Event>() {
                    @Override
                    public void handle(Event event) {
                        reload();
                    }
                });
            }
        }

        // create arcs
        for (final String id : model.getIds()) {
            PetriNetElement element = model.getElementById(id);
            assert element != null;

            if ( !(element instanceof PetriNetArc) )
                continue;

            PetriNetNodePresentation source =
                    (PetriNetNodePresentation) lookup("#" + normalizeId(((PetriNetArc) element).getSource().getId()));
            PetriNetNodePresentation target =
                    (PetriNetNodePresentation)  lookup("#" + normalizeId(((PetriNetArc) element).getTarget().getId()));

            assert source != null && target != null;

            PetriNetArcPresentation presentation = PetriNetArcPresentationBuilder.create()
                    .source(source)
                    .target(target)
                    .model((PetriNetArc) element)
                    .strokeWidth(strokeWidth)
                    .build();
            ++arcCounter;

            ((Node) presentation).setId(normalizeId(id));
            getChildren().add((Node) presentation);
        }

        setOnMouseDraggedForEachElement(mouseDraggedEventHandler);
        setOnMouseClickedForEachElement(mouseClickedEventHandler);
    }

    /** Liest und konvertiert die <b>strokeWidth</b> Eigenschaft aus den Einstellungen */
    private double getStrokeWidthFromSettings() {
        return Double.valueOf(Settings.getInstance()
                .getValue("stroke_width", PetriNetNodePresentation.DEFAULT_STROKE_WIDTH));
    }

    /** Liest und konvertiert die <b>nodeSize</b> Eigenschaft aus den Einstellungen */
    private double getNodeSizeFormSettings() {
        return Double.valueOf(Settings.getInstance()
                .getValue("node_size", PetriNetNodePresentation.DEFAULT_STROKE_WIDTH));
    }

    /** Gibt den Model zurück */
    public PetriNet getModel() {
        return model;
    }


    /** Setzt den Handler für jeden Knoten des Netzes */
    public void setOnMouseClickedForEachElement(EventHandler<? super MouseEvent> handler) {
        for (Node node : getChildren()) {
            node.setOnMouseClicked(handler);
        }
        mouseClickedEventHandler = handler;
    }

    /** Setzt den Handler für jeden Knoten des Netzes */
    public void setOnMouseDraggedForEachElement(EventHandler<? super MouseEvent> handler) {
        for (Node node : getChildren()) {
            node.setOnMouseDragged(handler);
        }
        mouseDraggedEventHandler = handler;
    }

    /**
     * Erstellt eine Stelle und fügt sie dem Model hinzu.
     * <p>
     * Um Änderungen sichtbar zu machen werden Präsentationen neu gezeichnet.
     * @see #reload()
     * @param x X-Koordinate
     * @param y Y-Koordinate
     */
    public void createPlace(double x, double y) {
        PetriNetPlace place = new PetriNetPlace(String.format("place%d", ++placeCounter));
        place.setName(place.getId());
        Point2D localPoint = localToScene(x ,y);
        place.setPosition(new PetriNetNode.Position((int)localPoint.getX(), (int)localPoint.getY()));

        model.addElement(place);
        reload();
    }

    /**
     * Erstellt eine Transition und fügt sie dem Model hinzu.
     * <p>
     * Um Änderungen sichtbar zu machen werden Präsentationen neu gezeichnet.
     * @see #reload()
     * @param x X-Koordinate
     * @param y Y-Koordinate
     */
    public void createTransition(double x, double y) {
        PetriNetTransition transition = new PetriNetTransition(String.format("trans%d", ++transitionCounter));
        transition.setName(transition.getId());
        transition.setPosition(new PetriNetNode.Position((int)x , (int)y));

        model.addElement(transition);
        reload();
    }

    /**
     * Erstellt eine Kante und fügt sie dem Model hinzu.
     * <p>
     * Um Änderungen sichtbar zu machen werden Präsentationen neu gezeichnet.
     * @see #reload()
     * @param source Präsentation des Quellknotens
     * @param target Präsentation des Zielknotens
     */
    public void createArc(PetriNetNodePresentation source, PetriNetNodePresentation target) {
        PetriNetArc arc = new PetriNetArc(String.format("arc%d", ++arcCounter));
        arc.setSource((PetriNetNode) source.getModel());
        arc.setTarget((PetriNetNode) target.getModel());

        model.addElement(arc);
        reload();
    }

    /**
     * Wählt ein Element aus.
     * <p>
     * Element wird sichtbar als Ausgewählt hervorgehoben. Ist bereits ein Element ausgewählt,
     * wird das abgewählt.
     * @param id Element zum auswählen
     */
    public void selectElementById(String id) {
        if (selectedElementId != null)
            unselectElement();

        Node node = lookup("#" + id);
        if (node == null) {
            logger.log(Level.WARNING, "no node with id '" + id + "' found");
            return;
        }

        DropShadow shadow = DropShadowBuilder.create()
                .offsetX(3)
                .offsetY(3)
                .radius(5)
                .color(Color.color(0.4, 0.5, 0.6))
                .build();
        node.setEffect(shadow);
        selectedElementId = node.getId();
    }

    /**
     * Wählt ein Element ab.
     * <p>Falls Element ausgewählt ist, wird das abgewählt.
     */
    public void unselectElement() {
        Node node = lookup("#" + selectedElementId);
        if (node == null) {
            logger.log(Level.WARNING, "no node with id '" + selectedElementId + "' found");
        } else {
            node.setEffect(null);
        }
        selectedElementId = null;
    }

    /**
     * Entfernt ausgewähltes Element
     * <p>Element wird aus dem Model entfernt und anschließend neu gezeichnet.
     */
    public void removeSelectedElement() {
        Node node = lookup("#" + selectedElementId);

        if (node == null) {
            logger.log(Level.WARNING, "no element selected");
            return;
        }

        assert node != null;

        getModel().removeElementById(((PetriNetElementPresentation) node).getModel().getId());
        selectedElementId = "";
        reload();
    }


    /**
     * Verschiebt eine Knotenpräsentation.
     * @param presentation Präsentation zum verschieben
     * @param point Koordinate
     */
    public void moveNode(PetriNetNodePresentation presentation, Point2D point) {
        // vermeidet Verschiebungen außerhalb des Sichtbereichs
        double x = Utils.ensureRange(point.getX(), 0d, getLayoutBounds().getWidth());
        double y = Utils.ensureRange(point.getY(), 0d, getLayoutBounds().getHeight());

        presentation.setCenterX(x);
        presentation.setCenterY(y);
        presentation.syncToModel();
    }

    /**
     * Gibt aktuell ausgewähltes Element zurück.
     * <p>Wenn kein Element ausgewählt gibt <code>null</code> zurück.
     */
    public PetriNetElementPresentation getSelectedElement() {
        Node node = lookup("#" + selectedElementId);
        if (node == null)
            return null;

        return (PetriNetElementPresentation) node;
    }

    /**
     * Löscht alle Elemente aus dem Model und ihre Präsentationen.
     */
    public void clear() {
        model.clear();
        getChildren().clear();
        reload();
    }

    /**
     * Normalisiert Ids.
     * <p>
     * JavaFX FrameWork benutzt für Id's CSS Selektoren, in denen einige Zeichen (z.B. Punkt) nicht
     * vorkommen dürfen. So werden hier Punkte durch Unterstriche ersetzt.
     */
    private String normalizeId(String id) {
        return id.replace('.','_');
    }
}
