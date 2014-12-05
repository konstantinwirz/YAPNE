package edu.yapne.scene;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;


/**
 * Created by konstantin on 09/11/14.
 */
abstract public class AbstractNode  extends BorderPane {

    private static double MAXIMUM_SIZE = 150.0;
    private static double MINIMUM_SIZE = 20.0;
    private static double DEFAULT_SIZE = 40.0;

    private static double MINIMUM_STROKE_WIDTH = 0.5;
    private static double MAXIMUM_STROKE_WIDTH = 10.0;
    private static double DEFAULT_STROKE_WIDTH = 2.5;

    private static Color DEFAULT_FILL_COLOR = Color.TRANSPARENT;
    private static Color DEFAULT_STROKE_COLOR = Color.BLACK;

    private DoubleProperty size = new SimpleDoubleProperty(DEFAULT_SIZE);
    private StringProperty label = new SimpleStringProperty();
    private ObjectProperty<Color> fillColor = new SimpleObjectProperty<Color>(DEFAULT_FILL_COLOR);
    private ObjectProperty<Color> strokeColor = new SimpleObjectProperty<Color>(DEFAULT_STROKE_COLOR);
    private DoubleProperty strokeWidth = new SimpleDoubleProperty(DEFAULT_STROKE_WIDTH);
    private DoubleProperty centerX = new SimpleDoubleProperty(0.0);
    private DoubleProperty centerY = new SimpleDoubleProperty(0.0);

    private Text labelText;
    private static int counter = 0;

    public AbstractNode() {
        setupUi();
        registerListeners();
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
                if (newValue.doubleValue() > getMaximumSize() || newValue.doubleValue() < getMinimumSize())
                    throw new IllegalArgumentException("invalid size passed");

                onSizeChanged(newValue.doubleValue());
            }
        });

        fillColor.addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observableValue, Color oldValue, Color newValue) {
                onFillColorChanged(newValue);
            }
        });

        strokeWidth.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number olValue, Number newValue) {
                final double value = newValue.doubleValue();
                if (value < getMinimumStrokeWidth() || value > getMaximumStrokeWidth())
                    throw new IllegalArgumentException("invalid stroke width passed");

                onStrokeWidthChanged(value);
            }
        });

        strokeColor.addListener(new ChangeListener<Color>() {
            @Override
            public void changed(ObservableValue<? extends Color> observableValue, Color oldValue, Color newValue) {
                labelText.setStroke(newValue);
                labelText.setFill(newValue);
                onStrokeColorChanged(newValue);
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

        setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                setCenterX(mouseEvent.getSceneX());
                setCenterY(mouseEvent.getSceneY());
            }
        });
    }

    private void onCenterXChanged(double value) {
        setLayoutX(value - getCenterXOffset());
    }

    private void onCenterYChanged(double value) {
        setLayoutY(value - getCenterYOffset());
    }

    public double getCenterXOffset() {
        return getBoundsInParent().getWidth()/ 2 + 1;
    }

    public double getCenterYOffset() {
        return getBoundsInParent().getHeight() / 2 + 1;
    }

    protected void onSizeChanged(double newSize) {}
    protected void onStrokeWidthChanged(double newWidth) {}
    protected void onFillColorChanged(Color newColor) {}
    protected void onStrokeColorChanged(Color newColor) {}


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

    public static double getMaximumSize() {
        return MAXIMUM_SIZE;
    }

    public static double getMinimumSize() {
        return MINIMUM_SIZE;
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

    public static double getMinimumStrokeWidth() {
        return MINIMUM_STROKE_WIDTH;
    }

    public static double getMaximumStrokeWidth() {
        return MAXIMUM_STROKE_WIDTH;
    }

    public final Color getFillColor() {
        return this.fillColor.get();
    }

    public final void setFillColor(Color color) {
        this.fillColor.set(color);
    }

    public final ObjectProperty<Color> fillColorProperty() {
        return this.fillColor;
    }

    public final Color getStrokeColor() {
        return strokeColor.get();
    }

    public final ObjectProperty<Color> strokeColorProperty() {
        return strokeColor;
    }

    public final void setStrokeColor(Color strokeColor) {
        this.strokeColor.set(strokeColor);
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
}
