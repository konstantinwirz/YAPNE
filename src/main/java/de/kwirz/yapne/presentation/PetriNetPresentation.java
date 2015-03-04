package de.kwirz.yapne.presentation;

import java.util.*;
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

    /**  Managt Mausklicks für jedes Element des Netzes */
    private EventHandler<? super MouseEvent> mouseClickedEventHandler;
    /** Managt Maus Dragged Events für jedes Element des Netzes */
    private EventHandler<? super MouseEvent> mouseDraggedEventHandler;

    /** Id's aktuell ausgewählter Elemente */
    private Set<String> selectedElements = new HashSet<>();

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
        PetriNetPlace place = new PetriNetPlace(generateValidElementName("place"));
        place.setName(place.getId());
        place.setPosition(new PetriNetNode.Position((int)x, (int)y));
        
        model.addElement(place);
        reload();
    }

    /**
     * Erstellt eine Stelle und fügt sie dem Model hinzu.
     * @see #createPlace(double, double)
     * @param point X,Y Koordinaten
     */
    public void createPlace(Point2D point) {
        createPlace(point.getX(), point.getY());
    }

    /**
     * Gibt eindeutigen Namen mit dem Suffix <b>suffix</b> und einer Zahl zurück.
     * @param suffix z.B. place oder transition
     */
    private String generateValidElementName(String suffix) {
        final int MAX_LOOKUPS = 1000;
        for (int i = 1; i < MAX_LOOKUPS; ++i) {
            String name = suffix + String.valueOf(i);
            if (lookup("#" + name) == null)
                return name;
        }
        return suffix + String.valueOf(new Random().nextInt());
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
        PetriNetTransition transition = new PetriNetTransition(generateValidElementName("trans"));
        transition.setName(transition.getId());
        transition.setPosition(new PetriNetNode.Position((int)x , (int)y));

        model.addElement(transition);
        reload();
    }

    /**
     * Erstellt eine Transition und fügt sie dem Model hinzu.
     * <p>
     * Um Änderungen sichtbar zu machen werden Präsentationen neu gezeichnet.
     * @see #reload()
     * @param point X,Y-Koordinaten
     */
    public void createTransition(Point2D point) {
        createTransition(point.getX(), point.getY());
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
        PetriNetArc arc = new PetriNetArc(generateValidElementName("arc"));
        arc.setSource((PetriNetNode) source.getModel());
        arc.setTarget((PetriNetNode) target.getModel());

        model.addElement(arc);
        reload();
    }

    /**
     * Wählt eine Element aus. Alle anderen Elemente werden abgewählt.
     * @param id Id des Elements
     */
    public void selectExclusiveElementById(String id) {
        unselectAllElements();
        selectElementById(id);
    }

    /**
     * Wählt ein Element aus.
     * <p>
     * Element wird sichtbar als Ausgewählt hervorgehoben. Ist bereits ein Element ausgewählt,
     * wird das abgewählt.
     * @param id Element zum auswählen
     */
    public void selectElementById(String id) {
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
        selectedElements.add(node.getId());
    }

    /**
     * Wählt alle (ausgewählte) Elemente ab.
     */
    public void unselectAllElements() {
        Iterator<String> iter = selectedElements.iterator();
        while (iter.hasNext()) {
            unselectElementById(iter.next());
            iter.remove();
        }
    }

    /**
     * Wählt ein Element ab.
     * <p><b>Wichtig!</b><br>Id bleibt in {@link #selectedElements} enthalten.
     *
     * @param id Id des Elements
     */
    private void unselectElementById(String id) {
        Node node = lookup("#" + id);
        if (node == null) {
            logger.log(Level.WARNING, "no node with id '" + id + "' found");
        } else {
            node.setEffect(null);
        }
    }

    /**
     * Wählt das Element aus oder ab
     * @param id Id des Elements
     */
    public void switchSelectedStateById(String id) {
        if (selectedElements.contains(id)) {
            unselectElementById(id);
            selectedElements.remove(id);
        } else {
            selectElementById(id);
        }
    }

    /**
     * Entfernt ausgewähltes Element
     * <p>Element wird aus dem Model entfernt und anschließend neu gezeichnet.
     */
    public void removeSelectedElements() {
        for (String id : selectedElements) {
            removeElementById(id);
        }

        selectedElements.clear();
        reload();
    }

    private void removeElementById(String id) {
        Node node = lookup("#" + id);

        if (node == null) {
            logger.log(Level.WARNING, "couldn't found node with id " + id);
            return;
        }

        assert node != null;

        getModel().removeElementById(((PetriNetElementPresentation) node).getModel().getId());
    }


    /**
     * Verschiebt ausgewählte Knoten.
     * @param leaderNode Dies ist der führende Konoten
     * @param point neue Koordinate
     */
    public void moveSelectedNodes(PetriNetNodePresentation leaderNode, Point2D point) {
        double deltaX = point.getX() - leaderNode.getCenterX();
        double deltaY = point.getY() - leaderNode.getCenterY();

        List<PetriNetNodePresentation> nodes = getSelectedNodes();
        assert nodes.contains(leaderNode);

        final double OFFSET = leaderNode.getSize() / 2;

        double minX = leaderNode.getCenterX();
        double minY = leaderNode.getCenterY();
        for (PetriNetNodePresentation node : nodes) {
            if (node.getCenterX() < minX)
                minX = node.getCenterX();
            if (node.getCenterY() < minY)
                minY = node.getCenterY();
        }

        deltaX = Utils.ensureRange(deltaX, OFFSET - minX, deltaX);
        deltaY = Utils.ensureRange(deltaY, OFFSET - minY, deltaY);

        for (PetriNetNodePresentation node : nodes) {
            double x = node.getCenterX() + deltaX;
            double y = node.getCenterY() + deltaY;

            x = Utils.ensureRange(x, OFFSET, x);
            y = Utils.ensureRange(y, OFFSET, y);

            node.setCenterX(x);
            node.setCenterY(y);

            node.syncToModel();
        }
    }

    /**
     * Gibt die Liste aller zur Zeit ausgewählter Knoten zurück
     */
    private List<PetriNetNodePresentation> getSelectedNodes() {
        List<PetriNetNodePresentation> nodes = new ArrayList<>();

        for (PetriNetElementPresentation element : getSelectedElements()) {
            if (element instanceof PetriNetNodePresentation)
                nodes.add((PetriNetNodePresentation) element);
        }

        return nodes;
    }


    /**
     * Gibt Elemente zurück die z.Z. ausgewählt sind.
     */
    public List<PetriNetElementPresentation> getSelectedElements() {
        List<PetriNetElementPresentation> elements = new ArrayList<>();

        for (String id : selectedElements) {
            Node node = lookup("#" + id);
            if (node != null) {
                elements.add((PetriNetElementPresentation) node);
            } else {
                logger.warning("couldn't find element with id " + id);
            }
        }

        return elements;
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
