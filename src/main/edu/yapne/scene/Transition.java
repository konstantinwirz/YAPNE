package edu.yapne.scene;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.RectangleBuilder;


/**
 * Created by konstantin on 09/11/14.
 */
public final class Transition extends  AbstractNode {

    private Rectangle rectangle;
    private SimpleBooleanProperty enabled = new SimpleBooleanProperty(false);
    private Color DEFAULT_ENABLED_STROKE_COLOR = Color.GREEN;

    public Transition() {
        setupUi();
        registerListeners();
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
        enabled.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean oldValue, Boolean newValue) {
                onSetEnabled(newValue.booleanValue());
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

    private void onSetEnabled(boolean tf) {
        rectangle.setStroke(tf?DEFAULT_ENABLED_STROKE_COLOR:getDefaultStrokeColor());
    }

    public SimpleBooleanProperty isEnabledProperty() {
        return enabled;
    }

    public void setEnabled(boolean tf) {
        enabled.set(tf);
    }

    public boolean isEnabled() {
        return enabled.get();
    }
}
