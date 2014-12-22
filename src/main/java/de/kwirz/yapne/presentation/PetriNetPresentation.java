package de.kwirz.yapne.presentation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.kwirz.yapne.model.*;
import de.kwirz.yapne.utils.Settings;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.DropShadowBuilder;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


/**
 * Created by konstantin on 20/12/14.
 */
public class PetriNetPresentation extends Pane {
    private static final Logger logger = Logger.getLogger(PetriNetPresentation.class.getName());

    private PetriNet model;

    private static int placeCounter = 0;
    private static int transitionCounter = 0;
    private static int arcCounter = 0;

    private EventHandler<? super MouseEvent> mouseClickedEventHandler;
    private EventHandler<? super MouseEvent> mouseDraggedEventHandler;

    private PetriNetElementPresentation selectedElement;


    public PetriNetPresentation() {
        model = new PetriNet();
    }

    public void setModel(PetriNet model) {
        this.model = model;
        initializeFromModel();
    }

    private void initializeFromModel() {
        if (model == null)
            return;

        getChildren().clear(); // remove elements from old model

        // Hilfsabbildung
        HashMap<String, Node> nodes = new HashMap<>();

        Settings settings = Settings.getInstance();
        double strokeWidth = getStrokeWidthFromSettings();
        double nodeSize = getNodeSizeFormSettings();

        // at first create places and transitions then create arcs
        for (final String id : model.getIds()) {
            PetriNetElement element = model.getElementById(id);
            assert element != null;
            assert !nodes.containsKey(element.getId());

            PetriNetElementPresentation presentation = null;
            if (element instanceof PetriNetPlace) {
                presentation = PetriNetPlacePresentationBuilder.create()
                        .model((PetriNetPlace) element)
                        .size(nodeSize)
                        .strokeWidth(strokeWidth)
                        .build();
                ++placeCounter;
            } else if (element instanceof PetriNetTransition) {
                presentation = PetriNetTransitionPresentationBuilder.create()
                        .model((PetriNetTransition) element)
                        .size(nodeSize)
                        .strokeWidth(strokeWidth)
                        .build();
                ++transitionCounter;
            }
            if (presentation != null)
                nodes.put(element.getId(), (Node) presentation);
        }

        // create arcs
        for (final String id : model.getIds()) {
            PetriNetElement element = model.getElementById(id);
            assert element != null;
            assert !nodes.containsKey(element.getId());

            if (element instanceof PetriNetArc) {
                PetriNetNodePresentation source =
                        (PetriNetNodePresentation) nodes.get(((PetriNetArc) element).getSource().getId());
                PetriNetNodePresentation target =
                        (PetriNetNodePresentation) nodes.get(((PetriNetArc) element).getTarget().getId());

                assert source != null && target != null;
                PetriNetArcPresentation presentation = PetriNetArcPresentationBuilder.create()
                                                        .source(source)
                                                        .target(target)
                                                        .model((PetriNetArc) element)
                                                        .strokeWidth(strokeWidth)
                                                        .build();
                ++arcCounter;
                nodes.put(element.getId(), (Node) presentation);
                ((Node) presentation).setScaleZ(100);
            }
        }

        getChildren().addAll(nodes.values());
        setOnMouseDraggedForEachElement(mouseDraggedEventHandler);
        setOnMouseClickedForEachElement(mouseClickedEventHandler);
    }

    private double getStrokeWidthFromSettings() {
        return Double.valueOf(Settings.getInstance()
                .getValue("stroke_width", PetriNetNodePresentation.getDefaultStrokeWidth()));
    }

    private double getNodeSizeFormSettings() {
        return Double.valueOf(Settings.getInstance()
                .getValue("node_size", PetriNetNodePresentation.getDefaultSize()));
    }

    public PetriNet getModel() {
        return model;
    }

    public List<PetriNetElementPresentation> getElements() {
        List<PetriNetElementPresentation> elements = new ArrayList<>();

        for (Node node : getChildren()) {
            PetriNetElementPresentation presentation = (PetriNetElementPresentation) node;
            elements.add(presentation);
        }

        return elements;
    }

    public void setOnMouseClickedForEachElement(EventHandler<? super MouseEvent> handler) {
        for (Node node : getChildren()) {
            node.setOnMouseClicked(handler);
        }
        mouseClickedEventHandler = handler;
    }

    public void setOnMouseDraggedForEachElement(EventHandler<? super MouseEvent> handler) {
        for (Node node : getChildren()) {
            node.setOnMouseDragged(handler);
        }
        mouseDraggedEventHandler = handler;
    }

    public void syncToModel() {

    }



    public void syncFromModel() {

    }

    public void createPlace(double x, double y) {
        PetriNetPlace placeModel =
                createNodeHelper(x, y, "place", ++placeCounter, PetriNetPlace.class);

        PetriNetPlacePresentation presentation =
                createPresentationHelper(placeModel, PetriNetPlacePresentation.class);
    }

    public void createTransition(double x, double y) {
        PetriNetTransition transitionModel =
                createNodeHelper(x, y, "transition", ++transitionCounter, PetriNetTransition.class);

        PetriNetTransitionPresentation presentation =
                createPresentationHelper(transitionModel, PetriNetTransitionPresentation.class);
    }

    private <T extends PetriNetNodePresentation> T createPresentationHelper(PetriNetNode nodeModel,
                                                                      Class<T> class_) {
        T presentation = null;

        try {
            presentation = class_.newInstance();
            presentation.setStrokeWidth(getStrokeWidthFromSettings());
            presentation.setSize(getNodeSizeFormSettings());
            presentation.setModel(nodeModel);

            presentation.syncFromModel();
            presentation.setOnMouseClicked(mouseClickedEventHandler);
            presentation.setOnMouseDragged(mouseDraggedEventHandler);

            model.addElement(nodeModel);
            getChildren().add(presentation);

        } catch (InstantiationException | IllegalAccessException e) {
            logger.log(Level.SEVERE,
                    String.format("couldn't create an instance of class '%s'", class_));
        }

        return presentation;
    }

    private <T extends PetriNetNode> T createNodeHelper(double x, double y, String name, int counter,
                                                        Class<T> class_) {
        String id;
        do {
            id = String.format("%s%d", name, counter);
        } while (model.hasElementById(id));

        T node = null;

        try {
            node = class_.getConstructor(String.class).newInstance(id);
            node.setName(id);
            node.setPosition(new PetriNetNode.Position((int)x, (int)y));
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                NoSuchMethodException e) {
            logger.log(Level.SEVERE,
                    String.format("couldn't create an instance of class '%s'", class_));
        }

        return node;
    }

    public void selectElement(PetriNetElementPresentation element) {
        if (selectedElement != null)
            unselectElement(selectedElement);

        assert element != null;

        DropShadow shadow = DropShadowBuilder.create()
                .offsetX(3)
                .offsetY(3)
                .radius(5)
                .color(Color.color(0.4, 0.5, 0.6))
                .build();
        ((Node) element).setEffect(shadow);
        selectedElement = element;
    }

    public void unselectElement(PetriNetElementPresentation element) {
        if (element != null)
            ((Node) selectedElement).setEffect(null);
    }

    public void removeSelectedElement() {
        if (selectedElement == null) {
            logger.log(Level.WARNING, "no element selected");
            return;
        }

        assert selectedElement != null;

        getModel().removeElementById(selectedElement.getModel().getId());
        initializeFromModel();
        selectedElement = null;
    }

    public void moveNode(PetriNetNodePresentation presentation, Point2D point) {
        presentation.setCenterX(point.getX());
        presentation.setCenterY(point.getY());
        presentation.syncToModel();
    }

}
