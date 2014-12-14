package de.kwirz.yapne.scene;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CircleBuilder;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;


/**
 * Created by konstantin on 09/11/14.
 */
public class Place extends AbstractNode {

    private StackPane stack = null;
    private Circle markingCircle = null;
    private Text markingText = null;
    private Circle circle = null;

    private IntegerProperty marking = new SimpleIntegerProperty(0);

    public Place() {
        setupUi();
        registerListeners();
    }

    private void setupUi() {
        stack = StackPaneBuilder.create()
                .build();
        circle = CircleBuilder.create()
                .strokeWidth(getStrokeWidth())
                .fill(getDefaultFillColor())
                .stroke(getDefaultStrokeColor())
                .radius(getSize() / 2)
                .build();
        stack.getChildren().add(circle);
        setCenter(stack);
        setAlignment(stack, Pos.CENTER);

        onMarkingValueChanged(getMarking());
    }

    private void registerListeners() {
        marking.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                onMarkingValueChanged(newValue.intValue());
            }
        });
    }

    @Override
    protected void onSizeChanged(double newSize) {
        if (circle == null)
            System.err.println("cannot handle size changes, elements are not constructed yet");
        else
            circle.setRadius(newSize / 2);
    }

    public final int getMarking() {
        return marking.get();
    }

    public final IntegerProperty markingProperty() {
        return marking;
    }

    public final void setMarking(int marking) {
        this.marking.set(marking);
    }

    @Override
    protected void onStrokeWidthChanged(double newWidth) {
        circle.setStrokeWidth(newWidth);
    }

    protected void onMarkingValueChanged(int newMarking) {
        if (newMarking < 0) {
            throw new IllegalArgumentException("marking cannot be negative");
        }

        stack.getChildren().removeAll(markingText, markingCircle);
        markingText = null;
        markingCircle = null;

        if (newMarking == 1) {
            markingCircle = CircleBuilder.create()
                    .radius(getSize() / 10)
                    .strokeWidth(getStrokeWidth())
                    .build();
            stack.getChildren().add(markingCircle);
        } else if (newMarking > 1) {
            markingText = TextBuilder.create().
                    text(String.valueOf(newMarking))
                    .strokeWidth(getStrokeWidth())
                    .build();
            stack.getChildren().add(markingText);
        }
    }

}
