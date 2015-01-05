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
 * Abstrakte Basisklasse für Stellen und Transitionen.
 * <p>
 * Implementiert gemeinsame Funktionalität für grafische Darstellungen von Transitionen und Stellen:
 * <ul>
 * <li>Knotengröße </li>
 * <li>Knotenname</li>
 * </ul>
 * Knotenname wird oberhalb des Knotens gezeichnet, bei einem Doppelklick lässt sich der Name ändern.
 * <p>
 * Klassen die von dieser ableiten müssen den Knoten selbst zeichnen und dann als {@link BorderPane} <b>center</b>
 * setzen.
 */
public abstract class PetriNetNodePresentation
        extends BorderPane  implements PetriNetElementPresentation {

    /** Maximale Größe des Knotens */
    public final static double MAXIMUM_SIZE = 150.0;

    /** Minimale Größe des Knotens */
    public final static double MINIMUM_SIZE = 20.0;

    /** Standardgröße des Knotens */
    public final static double DEFAULT_SIZE = 45.0;

    /** Standardfüllfarbe des Knotens */
    public final static Color DEFAULT_FILL_COLOR = Color.TRANSPARENT;

    /**
     * Größe des Knotens.
     * <p>
     * <b>Standardwert:</b> <br>
     * {@value #DEFAULT_SIZE}
     * @see #setSize
     * @see #getSize
     * @see #sizeProperty
     */
    private final DoubleProperty size = new SimpleDoubleProperty(DEFAULT_SIZE);

    /**
     * Knotenname.
     */
    private final StringProperty label = new SimpleStringProperty();


    /**
     * Linienstärke.
     * <p><b>Standardwert:</b> <br>
     * {@value de.kwirz.yapne.presentation.PetriNetElementPresentation#DEFAULT_STROKE_WIDTH}
     * @see #getStrokeWidth
     * @see #setStrokeWidth
     * @see #strokeWidthProperty
     */
    private final DoubleProperty strokeWidth = new SimpleDoubleProperty(DEFAULT_STROKE_WIDTH);

    /**
     * X-Koordinate des Knotenzentrums.
     * <p><b>Standardwert:</b> <br>
     * 0.0
     */
    private final DoubleProperty centerX = new SimpleDoubleProperty(0.0);

    /**
     * Y-Koordinate des Knotenzentrums.
     * <p><b>Standardwert:</b> <br>
     * 0.0
     */
    private final DoubleProperty centerY = new SimpleDoubleProperty(0.0);

    /**
     * In diesem Element steht Knotenname
     */
    private Text labelText;

    /**
     * Dieser Zähler wird zum automatischen Generieren von Knotennamen verwendet
     */
    private static int counter = 0;

    /**
     * Erstellt eine <b>PetriNetNodePresentation</b>
     */
    public PetriNetNodePresentation() {
        setupUi();
        registerListeners();
    }

    /**
     * Erstellt und konfiguriert UI Elemente
     */
    private void setupUi() {
        labelText = TextBuilder.create()
                .text(String.format("Item %d", ++counter))
                .font(new Font(16.0))
                .build();

        this.setTop(labelText);
        this.setAlignment(labelText, Pos.TOP_CENTER);
        this.setMargin(labelText, new Insets(0, 0, 5, 0));
    }

    /**
     * Registriert Listener.
     */
    private void registerListeners() {
        // Knotennamen setzen.
        label.addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                labelText.setText(newValue);
            }
        });

        // sicherstellen dass die Größe >= MINIMUM_SIZE und <= MAXIMUM_SIZE
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

        // sicherstellen dass Linienstärke >= MINIMUM_STROKE_WIDTH und <= MAXIMUM_STROKE_WIDTH
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

        // wenn die Eiganschaft geändert wird - führe den Handler aus.
        centerX.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                onCenterXChanged(newValue.doubleValue());
            }
        });

        // wenn die Eiganschaft geändert wird - führe den Handler aus.
        centerY.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                onCenterYChanged(newValue.doubleValue());
            }
        });

        // Zeige die Maske zum Editieren von Knotennamen.
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
     * @param value X-Koordinate
     */
    private void onCenterXChanged(double value) {
        // Um Zentrum des Layouts richtig zu bestimmen brauchen wir die maximale Breite
        double width = Math.max(labelText.getLayoutBounds().getWidth(), getSize());
        setLayoutX(value - width / 2 - getStrokeWidth());
    }

    /**
     * Wird bei einer Änderung der {@link #centerYProperty} ausgeführt.
     * @param value Y-Koordinate
     */
    private void onCenterYChanged(double value) {
        // entferne Knotennamenhöhe und alle Zwischeräume
        double offset = labelText.getBoundsInLocal().getHeight() +
                getMargin(labelText).getBottom() +
                getSize() / 2 +
                getStrokeWidth() / 2;
        setLayoutY(value - offset);
    }

    /**
     * Wird bei einer Änderung der Knotengröße ausgeführt
     * @param newSize neue Knotengröße
     */
    protected void onSizeChanged(double newSize) {

    }

    /**
     * Wird bei einer Änderung der Linienstärke ausgeführt.
     * @param newWidth neue Linienstärke
     */
    protected void onStrokeWidthChanged(double newWidth) {

    }

    /** Setzt den Knotennamen */
    public final String getLabel() {
        return label.get();
    }

    /** Gibt die {@link #label} Eigenschaft zurück */
    public final StringProperty labelProperty() {
        return label;
    }

    /** Setzt den Knotennamen */
    public final void setLabel(String label) {
        this.label.set(label);
    }

    /** Gibt die Knotengröße zurück */
    public final double getSize() {
        return size.get();
    }

    /** Gibt die {@link #size} Eigenschaft zurück */
    public final DoubleProperty sizeProperty() {
        return size;
    }

    /** Setzt die Knotengröße */
    public final void setSize(double size) {
        this.size.set(size);
    }

    /** Gibt die Linienstärke zurück */
    public final double getStrokeWidth() {
        return this.strokeWidth.get();
    }

    /** Setzt die Linienstärke */
    public final void setStrokeWidth(double width) {
        this.strokeWidth.set(width);
    }

    /** Gibt die {@link #strokeWidth} Eigenschaft zurück */
    public final DoubleProperty strokeWidthProperty() {
        return this.strokeWidth;
    }

    /** Gibt die X-Koordinate des Knotenzentrums zurück. */
    public final double getCenterX() {
        return centerX.get();
    }

    /** Gibt die {@link #centerX} Eigenschaft zurück */
    public final DoubleProperty centerXProperty() {
        return centerX;
    }

    /** Setzt die X-Koordinate des Knotenzentrums */
    public final void setCenterX(double centerX) {
        this.centerX.set(centerX);
    }

    /** Gibt die Y-Koordinate des Knotenzentrums zurück. */
    public final double getCenterY() {
        return centerY.get();
    }

    /** Gibt die {@link #centerY} Eigenschaft zurück */
    public final DoubleProperty centerYProperty() {
        return centerY;
    }

    /** Setzt die Y-Koordinate des Knotenzentrums */
    public final void setCenterY(double centerY) {
        this.centerY.set(centerY);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Klassen die diese Methode überschreiben müssen Methode der Superklasse unbedingt ausführen.
     */
    @Override
    public void syncToModel() {
        assert getModel() != null;
        assert getModel() instanceof  PetriNetNode;

        PetriNetNode model = (PetriNetNode) getModel();

        model.setName(getLabel());
        model.setPosition(new PetriNetNode.Position((int) getCenterX(), (int) getCenterY()));
    }

    /**
     * {@inheritDoc}
     * <p>
     * Klassen die diese Methode überschreiben müssen Methode der Superklasse unbedingt ausführen.
     */
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
