package de.kwirz.yapne.presentation;

import de.kwirz.yapne.model.PetriNetElement;
import de.kwirz.yapne.model.PetriNetPlace;
import de.kwirz.yapne.utils.Utils;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.logging.Logger;


/**
 * Grafische Darstellung einer Stelle.
 * <p>
 * Eine Stelle wird mit Hilfe eines Kreises mit der Markierung in der Mitte dargestellt.
 * Falls die Markierung ist 1, wird die als einzelner Punkt dargestellt, sonst als dezimale Zahl.
 */
public final class PetriNetPlacePresentation extends PetriNetNodePresentation {

    /** wird zum Loggen eingesetzt */
    private static final Logger logger = Logger.getLogger(PetriNetPlacePresentation.class.getName());

    /** Eine Petri Netz Stelle dient als Model */
    private PetriNetPlace model;

    /** Kreis und Markierung werden in einem {@link javafx.scene.layout.StackPane} dargestellt */
    private StackPane stack = null;

    /** Form der Stelle */
    private Circle circle = null;

    /** Markierungspunkt falls Markierung = 1 */
    private Circle markingCircle = null;

    /** Markierungszahl falls Markierung > 1 */
    private Text markingText = null;

    /**
     * Markierung.
     * <p><b>Standardwert:</b> <br>
     * 0
     */
    private final IntegerProperty marking = new SimpleIntegerProperty(0);

    /**
     * Erlaubt die Eingabe von positiven Ganzzahlen.
     * <p>
     * Da die Klasse von {@link javafx.scene.control.TextField} erbt, muss der Eingabetext in
     * Ganzzahl konvertiert werden und umgekehrt.
     */
     private final class IntegerField extends TextField {

        /** Dieser Wert wird im Feld angezeigt. */
        private IntegerProperty value = new SimpleIntegerProperty();

        /**
         * Erstellt ein Feld.
         */
        public IntegerField () {
            // konvertiert ganze Zahl in Text
            value.addListener((observable, oldValue, newValue) -> {
                setText(newValue.toString());
            });
            // konvertiert Eingabetext in eine ganze Zahl
            textProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue.isEmpty())
                    return;

                setText(newValue.matches("\\d+")?newValue:oldValue.matches("\\d+")?oldValue:"");

                try {
                    setValue(Integer.valueOf(getText()));
                } catch (NumberFormatException e) {
                    logger.warning("not a number");
                }
            });

            // beim Fokus Verlust, wenn Eingabefeld leer ist - auf 0 setzen.
            focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (getText().isEmpty())
                    setText("0");

            });
        }

        /**
         * Setzt den Wert.
         * <p>Wert wird im Feld angezeigt.
         */
        public void setValue(int value) {
            this.value.set(value);
        }

        /** Gibt den angezeigten Wert zurück */
        public int getValue() {
            return this.value.get();
        }

        /** Gibt die {@link #value} Eigenschaft zurück */
        @SuppressWarnings("unused")
        public IntegerProperty valueProperty() {
            return this.value;
        }

    }


    /**
     * Bei einem Mausklick auf den Kreis soll sich ein Dialog zur Eingabe von Markierung
     * öffnen, da in der Mitte sich entweder ein Circle oder Text befinden (abhängig von
     * Markierung) wird dieser Handler auch für diese Elemente gesetzt.
     */
    private EventHandler<MouseEvent> inputMarkingOnMouseClick = event -> {
        if (event.getClickCount() != 2)
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

        field.setOnAction(event1 -> {
            marking.setValue(field.getValue());
            dialog.close();
        });

        field.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ESCAPE)
                dialog.close();
        });

        VBox box = new VBox();
        box.setSpacing(10);
        box.setPadding(new Insets(10, 10, 10, 10));
        box.getChildren().addAll(new Text("Marking:"), field);

        dialog.setScene(new Scene(box));

        dialog.show();
    };

    /**
     * Erstellt eine Instanz von <b>PetriNetPlacePresentation</b>
     */
    public PetriNetPlacePresentation() {
        setupUi();
        registerListeners();
    }

    /** Konfiguriert UI */
    private void setupUi() {
        stack = new StackPane();
        circle = new Circle(getSize() / 2, DEFAULT_FILL_COLOR);
        circle.setStroke(DEFAULT_STROKE_COLOR);
        circle.setStrokeWidth(getStrokeWidth());

        stack.getChildren().add(circle);
        setCenter(stack);
        setAlignment(stack, Pos.CENTER);

        onMarkingValueChanged(getMarking());
    }

    /** Registriert Listener */
    private void registerListeners() {
        // Macht ein Model Update, falls Markierung sich ändert.
        marking.addListener((observableValue, oldValue, newValue) -> {
            onMarkingValueChanged(newValue.intValue());
            if (model != null)
                model.setMarking(newValue.intValue());
        });

        circle.setOnMouseClicked(inputMarkingOnMouseClick);
    }

    /**
     * {@inheritDoc}
     * Aktualisiert Kreisgröße
     * @param newSize Durchmesser des Kreises
     */
    @Override
    protected void onSizeChanged(double newSize) {
        if (circle == null)
            System.err.println("cannot handle size changes, elements are not constructed yet");
        else
            circle.setRadius(newSize / 2);
    }

    /** Gibt Markierung zurück */
    public final int getMarking() {
        return marking.get();
    }

    /** Gibt {@link #marking} Eigenschaft zurück */
    public final IntegerProperty markingProperty() {
        return marking;
    }

    /** Setzt Markierung */
    public final void setMarking(int marking) {
        this.marking.set(marking);
    }

    /**
     * {@inheritDoc}
     * Aktualisiert Linienstärke
     */
    @Override
    protected void onStrokeWidthChanged(double newWidth) {
        circle.setStrokeWidth(newWidth);
    }

    /**
     * Wird bei einer Änderung der Markierung ausgeführt.
     * <p>Hier wird das Markierungselement in der Mitte angepasst, abhängig vom Markierung.
     * @param newMarking neue Markierung
     * @throws IllegalArgumentException falls Markierung negativ ist
     */
    protected void onMarkingValueChanged(int newMarking) {
        if (newMarking < 0) {
            throw new IllegalArgumentException("marking cannot be negative");
        }

        stack.getChildren().removeAll(markingText, markingCircle);
        markingText = null;
        markingCircle = null;

        if (newMarking == 1) {
            markingCircle = new Circle(getSize() / 10);
            markingCircle.setStrokeWidth(getStrokeWidth());
            stack.getChildren().add(markingCircle);
            markingCircle.setOnMouseClicked(inputMarkingOnMouseClick);
        } else if (newMarking > 1) {
            markingText = new Text(String.valueOf(newMarking));
            markingText.setStrokeWidth(getStrokeWidth());
            markingText.setFont(new Font(calculateProperMarkingFontSize()));
            stack.getChildren().add(markingText);
            markingText.setOnMouseClicked(inputMarkingOnMouseClick);
        }
    }

    /** Gibt passende Schriftgröße für Markierung zurück */
    private double calculateProperMarkingFontSize() {
        double maxFontSize = getSize() / 4.0;
        double minFontSize = 10.0;
        return Utils. ensureRange(maxFontSize, minFontSize, maxFontSize);
    }

    /** {@inheritDoc} */
    @Override
    public void setModel(PetriNetElement element) {
        model = (PetriNetPlace) element;
        syncFromModel();
    }

    /** {@inheritDoc} */
    @Override
    public PetriNetElement getModel() {
        return model;
    }

    /** {@inheritDoc} */
    @Override
    public void syncToModel() {
        super.syncToModel();
        model.setMarking(getMarking());
    }

    /** {@inheritDoc} */
    @Override
    public void syncFromModel() {
        super.syncFromModel();
        setMarking(model.getMarking());
    }

}
