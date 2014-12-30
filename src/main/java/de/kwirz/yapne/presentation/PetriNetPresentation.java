package de.kwirz.yapne.presentation;

import java.util.logging.Level;
import java.util.logging.Logger;

import de.kwirz.yapne.model.*;
import de.kwirz.yapne.utils.Settings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

    private int placeCounter = 0;
    private int transitionCounter = 0;
    private int arcCounter = 0;

    private EventHandler<? super MouseEvent> mouseClickedEventHandler;
    private EventHandler<? super MouseEvent> mouseDraggedEventHandler;

    private PetriNetElementPresentation selectedElement;


    public PetriNetPresentation() {
        model = new PetriNet();
    }

    public void setModel(PetriNet model) {
        this.model = model;
        reload();
    }

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
                ((Node) presentation).setId(id);
                getChildren().add((Node) presentation);
            }
        }

        // create arcs
        for (final String id : model.getIds()) {
            PetriNetElement element = model.getElementById(id);
            assert element != null;

            if ( !(element instanceof PetriNetArc) )
                continue;

            PetriNetNodePresentation source =
                    (PetriNetNodePresentation) lookup("#" + ((PetriNetArc) element).getSource().getId());
            PetriNetNodePresentation target =
                    (PetriNetNodePresentation)  lookup("#" + ((PetriNetArc) element).getTarget().getId());

            assert source != null && target != null;

            PetriNetArcPresentation presentation = PetriNetArcPresentationBuilder.create()
                    .source(source)
                    .target(target)
                    .model((PetriNetArc) element)
                    .strokeWidth(strokeWidth)
                    .build();
            ++arcCounter;

            ((Node) presentation).setId(id);
            getChildren().add((Node) presentation);
        }

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

    public void createPlace(double x, double y) {
        PetriNetPlace place = new PetriNetPlace(String.format("place%d", ++placeCounter));
        place.setName(place.getId());
        Point2D localPoint = localToScene(x ,y);
        place.setPosition(new PetriNetNode.Position((int)localPoint.getX(), (int)localPoint.getY()));

        model.addElement(place);
        reload();
    }

    public void createTransition(double x, double y) {
        PetriNetTransition transition = new PetriNetTransition(String.format("trans%d", ++transitionCounter));
        transition.setName(transition.getId());
        transition.setPosition(new PetriNetNode.Position((int)x , (int)y));

        model.addElement(transition);
        reload();
    }

    public void createArc(PetriNetNodePresentation source, PetriNetNodePresentation target) {
        PetriNetArc arc = new PetriNetArc(String.format("arc%d", ++arcCounter));
        arc.setSource((PetriNetNode) source.getModel());
        arc.setTarget((PetriNetNode) target.getModel());

        model.addElement(arc);
        reload();
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
        if (element != null) {
            ((Node) selectedElement).setEffect(null);
            selectedElement = null;
        }
    }

    public void removeSelectedElement() {
        if (selectedElement == null) {
            logger.log(Level.WARNING, "no element selected");
            return;
        }

        assert selectedElement != null;

        getModel().removeElementById(selectedElement.getModel().getId());
        selectedElement = null;
        reload();
    }

    public void moveNode(PetriNetNodePresentation presentation, Point2D point) {
        double x = Math.max(point.getX(), 0);
        double y = Math.max(point.getY(), 0);
        presentation.setCenterX(x);
        presentation.setCenterY(y);
        presentation.syncToModel();
    }

    public PetriNetElementPresentation getSelectedElement() {
        return selectedElement;
    }

}
