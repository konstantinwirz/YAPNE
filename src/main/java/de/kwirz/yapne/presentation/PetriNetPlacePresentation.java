package de.kwirz.yapne.presentation;

import de.kwirz.yapne.app.IntegerField;
import de.kwirz.yapne.model.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;


/**
 * Created by konstantin on 20/12/14.
 */
public class PetriNetPlacePresentation extends PetriNetNodePresentation {

    private PetriNetPlace model;
    private StackPane stack = null;
    private Circle markingCircle = null;
    private Text markingText = null;
    private Circle circle = null;

    private IntegerProperty marking = new SimpleIntegerProperty(0);

    /**
     * Bei einem Mausklick auf den Kreis soll sich ein Dialog zur Eingabe von Markierung
     * öffnen, da in der Mitte sich entweder ein Circle oder Text befinden (abhängig von
     * Markierung) wird dieser Handler auch für diese Knoten gesetzt.
     */
    private EventHandler<MouseEvent> inputMarkingOnMouseClick = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getClickCount() < 2)
                return;

            final Node source = (Node) event.getSource();
            final Scene sourceScene = source.getScene();
            final Window sourceWindow = sourceScene.getWindow();

            final Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(sourceWindow);
            dialog.initStyle(StageStyle.UNDECORATED);

            final Point2D coord = source.localToScene(0.0, 0.0);

            dialog.setX(sourceWindow.getX() + coord.getX());
            dialog.setY(sourceWindow.getY() + coord.getY());

            final IntegerField field = new IntegerField();
            field.setValue(marking.getValue());

            field.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    marking.setValue(field.getValue());
                    dialog.close();
                }
            });

            field.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if (keyEvent.getCode() == KeyCode.ESCAPE)
                        dialog.close();
                }
            });

            VBox box = new VBox();
            box.setSpacing(10);
            box.setPadding(new Insets(10, 10, 10, 10));
            box.getChildren().addAll(TextBuilder.create().text("Marking:").build(), field);

            dialog.setScene(new Scene(box));

            dialog.show();
        }
    };


    public PetriNetPlacePresentation() {
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
                model.setMarking(newValue.intValue());
            }
        });

        circle.setOnMouseClicked(inputMarkingOnMouseClick);
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
            markingCircle.setOnMouseClicked(inputMarkingOnMouseClick);
        } else if (newMarking > 1) {
            markingText = TextBuilder.create().
                    text(String.valueOf(newMarking))
                    .strokeWidth(getStrokeWidth())
                    .build();
            stack.getChildren().add(markingText);
            markingText.setOnMouseClicked(inputMarkingOnMouseClick);
        }
    }

    @Override
    public void setModel(PetriNetElement element) {
        model = (PetriNetPlace) element;
        syncFromModel();
    }

    @Override
    public PetriNetElement getModel() {
        return model;
    }

    @Override
    public void syncToModel() {
        super.syncToModel();
        model.setMarking(getMarking());
    }

    @Override
    public void syncFromModel() {
        super.syncFromModel();
        setMarking(model.getMarking());
    }
}
