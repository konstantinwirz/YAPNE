package de.kwirz.yapne.presentation;

import de.kwirz.yapne.model.PetriNetElement;
import de.kwirz.yapne.model.PetriNetTransition;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;

/**
 * Created by konstantin on 20/12/14.
 */
public class PetriNetTransitionPresentation extends PetriNetNodePresentation {

    private PetriNetTransition model;
    private Rectangle rectangle;
    private Color DEFAULT_ENABLED_STROKE_COLOR = Color.GREEN;

    public PetriNetTransitionPresentation() {
        setupUi();
        registerListeners();
    }

    public PetriNetTransitionPresentation(PetriNetTransition model) {
        this();
        setModel(model);
    }

    private void setupUi() {
        rectangle = RectangleBuilder.create()
                .width(getSize())
                .stroke(getDefaultStrokeColor())
                .fill(getDefaultFillColor())
                .height(getSize())
                .strokeWidth(getStrokeWidth())
                .build();

        setCenter(rectangle);
        setAlignment(rectangle, Pos.CENTER);
    }

    private void registerListeners() {
        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() < 2)
                    return;

                model.occur();
                fireEvent(new OccurrenceEvent());
            }
        });
    }

    @Override
    protected final void onSizeChanged(double newSize) {
        rectangle.setWidth(newSize);
        rectangle.setHeight(newSize);
    }

    @Override
    protected final void onStrokeWidthChanged(double newWidth) {
        rectangle.setStrokeWidth(newWidth);
    }

    private void setEnabled(boolean tf) {
        rectangle.setStroke(tf?DEFAULT_ENABLED_STROKE_COLOR:getDefaultStrokeColor());
    }

    @Override
    public void setModel(PetriNetElement element) {
        model = (PetriNetTransition) element;
        syncFromModel();
    }

    @Override
    public PetriNetElement getModel() {
        return model;
    }

    @Override
    public void syncToModel() {
        super.syncToModel();
    }

    @Override
    public void syncFromModel() {
        super.syncFromModel();
        setEnabled(model.isEnabled());
    }
}
