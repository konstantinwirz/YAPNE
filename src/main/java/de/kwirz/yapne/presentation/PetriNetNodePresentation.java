package de.kwirz.yapne.presentation;

import de.kwirz.yapne.model.PetriNetNode;
import de.kwirz.yapne.utils.Utils;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * Created by konstantin on 20/12/14.
 */
public abstract class PetriNetNodePresentation extends BorderPane
        implements PetriNetElementPresentation {

    public final static double MAXIMUM_SIZE = 150.0;
    public final static double MINIMUM_SIZE = 20.0;
    public final static double DEFAULT_SIZE = 45.0;

    private final static Color DEFAULT_FILL_COLOR = Color.TRANSPARENT;

    private DoubleProperty size = new SimpleDoubleProperty(DEFAULT_SIZE);
    private StringProperty label = new SimpleStringProperty();
    private DoubleProperty strokeWidth = new SimpleDoubleProperty(DEFAULT_STROKE_WIDTH);
    private DoubleProperty centerX = new SimpleDoubleProperty(0.0);
    private DoubleProperty centerY = new SimpleDoubleProperty(0.0);

    private Text labelText;
    private static int counter = 0;

    public PetriNetNodePresentation() {
        setupUi();
        registerListeners();
    }

    public static Color getDefaultFillColor() {
        return DEFAULT_FILL_COLOR;
    }

    public static Color getDefaultStrokeColor() {
        return DEFAULT_STROKE_COLOR;
    }

    private void setupUi() {
        labelText = TextBuilder.create()
                .text(String.format("Item %d", ++counter))
                .font(new Font(16.0))
                .build();

        this.setTop(labelText);
        this.setAlignment(labelText, Pos.TOP_CENTER);
        this.setMargin(labelText, new Insets(0, 0, 5, 0));
    }

    private void registerListeners() {
        label.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                labelText.setText(newValue);
            }
        });

        size.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                double value = newValue.doubleValue();
                if (Utils.inRange(value, MINIMUM_SIZE, MAXIMUM_SIZE)) {
                    onSizeChanged(value);
                } else {
                    size.set(Utils.ensureRange(value, MINIMUM_SIZE, MAXIMUM_SIZE));
                }
            }
        });

        strokeWidth.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                double value = newValue.doubleValue();
                if (Utils.inRange(value, MINIMUM_STROKE_WIDTH, MAXIMUM_STROKE_WIDTH)) {
                    onStrokeWidthChanged(value);
                } else {
                    strokeWidth.set(Utils.ensureRange(value, MINIMUM_STROKE_WIDTH, MAXIMUM_STROKE_WIDTH));
                }
            }
        });

        centerX.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                Node center = getCenter();
                if (center == null)
                    throw new NullPointerException("center node can not be null");

                onCenterXChanged(newValue.doubleValue());
            }
        });

        centerY.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                Node center = getCenter();
                if (center == null)
                    throw new NullPointerException("center node can not be null");

                onCenterYChanged(newValue.doubleValue());
            }
        });

        labelText.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getClickCount() < 2)
                    return;

                final Text source = (Text) mouseEvent.getSource();
                final Scene sourceScene = source.getScene();
                final Window sourceWindow = sourceScene.getWindow();

                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(sourceWindow);
                dialog.initStyle(StageStyle.UNDECORATED);

                final Point2D coord = source.localToScene(0.0, 0.0);
                dialog.setX(sourceWindow.getX() + coord.getX());
                dialog.setY(sourceWindow.getY() + coord.getY());


                final TextField field = new TextField();
                field.setText(source.getText());
                field.selectAll();
                field.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if (!field.getText().isEmpty())
                            label.set(field.getText());
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
                box.getChildren().addAll(TextBuilder.create().text("Name:").build(), field);

                dialog.setScene(new Scene(box));

                dialog.show();
            }
        });
    }

    /**
     * Wird bei einer Änderung der {@link #centerXProperty} ausgeführt.
     * @param value X-Wert
     */
    private void onCenterXChanged(double value) {
        // Um Zentrum des Layouts richtig zu bestimmen brauchen wir die maximale Breite
        double width = Math.max(labelText.getLayoutBounds().getWidth(), getSize());
        setLayoutX(value - width / 2 - getStrokeWidth());
    }

    private void onCenterYChanged(double value) {
        double offset = labelText.getBoundsInLocal().getHeight() +
                getMargin(labelText).getBottom() +
                getSize() / 2 +
                getStrokeWidth() / 2;
        setLayoutY(value - offset);
    }

    protected void onSizeChanged(double newSize) {}
    protected void onStrokeWidthChanged(double newWidth) {}

    public final String getLabel() {
        return label.get();
    }

    public final StringProperty labelProperty() {
        return label;
    }

    public final void setLabel(String label) {
        this.label.set(label);
    }

    public final double getSize() {
        return size.get();
    }

    public final DoubleProperty sizeProperty() {
        return size;
    }

    public final void setSize(double size) {
        this.size.set(size);
    }

    public final double getStrokeWidth() {
        return this.strokeWidth.get();
    }

    public final void setStrokeWidth(double width) {
        this.strokeWidth.set(width);
    }

    public final DoubleProperty strokeWidthProperty() {
        return this.strokeWidth;
    }

    public double getCenterX() {
        return centerX.get();
    }

    public DoubleProperty centerXProperty() {
        return centerX;
    }

    public void setCenterX(double centerX) {
        this.centerX.set(centerX);
    }

    public double getCenterY() {
        return centerY.get();
    }

    public DoubleProperty centerYProperty() {
        return centerY;
    }

    public void setCenterY(double centerY) {
        this.centerY.set(centerY);
    }

    @Override
    public void syncToModel() {
        assert getModel() != null;
        assert getModel() instanceof  PetriNetNode;

        PetriNetNode model = (PetriNetNode) getModel();

        model.setName(getLabel());
        model.setPosition(new PetriNetNode.Position((int) getCenterX(), (int) getCenterY()));
        fireEvent(new OccurrenceEvent());
    }

    @Override
    public void syncFromModel() {
        assert getModel() != null;
        assert getModel() instanceof PetriNetNode;

        PetriNetNode model = (PetriNetNode) getModel();

        setLabel(model.getName());
        setCenterX(model.getPosition().getX());
        setCenterY(model.getPosition().getY());
    }
}
