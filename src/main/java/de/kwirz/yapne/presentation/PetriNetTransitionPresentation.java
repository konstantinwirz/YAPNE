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
 * Grafische Darstellung einer Transition.
 * <p>
 * Eine Transition wird mit Hilfe eines Quadrats dargestellt, soll die Transition aktiviert sein,
 * so werden Quadratseiten grün hervorgehoben. Ist Transition aktiviert, kann sie mit einem
 * Doppelklick geschaltet werden.
 */
public final class PetriNetTransitionPresentation extends PetriNetNodePresentation {

    /** Eine Petri Netz Transition dient als Model */
    private PetriNetTransition model;

    /** Wird für Quadrat Darstellung benutzt */
    private Rectangle rectangle;

    /**
     * Farbe der aktivierten Transition
     * <p><b>Standardwert:</b> <br>
     * Grün
     */
    private static final Color DEFAULT_ENABLED_STROKE_COLOR = Color.GREEN;

    /**
     * Erstellt eine Präsentation
     */
    public PetriNetTransitionPresentation() {
        setupUi();
        registerListeners();
    }

    /** Erstellt und konfiguriert UI Elemente */
    private void setupUi() {
        rectangle = RectangleBuilder.create()
                .width(getSize())
                .stroke(DEFAULT_STROKE_COLOR)
                .fill(DEFAULT_FILL_COLOR)
                .height(getSize())
                .strokeWidth(getStrokeWidth())
                .build();

        setCenter(rectangle);
        setAlignment(rectangle, Pos.CENTER);
    }

    /** Registriert Listener */
    private void registerListeners() {
        // Schaltet Transition beim Doppelklick.
        rectangle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getClickCount() != 2)
                    return;

                model.occur();
                fireEvent(new OccurrenceEvent());
            }
        });
    }

    /**
     * Aktualisiert Höhe und Breite des Quadrats.
     * @param newSize neue Knotengröße
     */
    @Override
    protected final void onSizeChanged(double newSize) {
        rectangle.setWidth(newSize);
        rectangle.setHeight(newSize);
    }

    /**
     * Aktualisiert die Linienstärke.
     * @param newWidth neue Linienstärke
     */
    @Override
    protected final void onStrokeWidthChanged(double newWidth) {
        rectangle.setStrokeWidth(newWidth);
    }

    private void setEnabled(boolean tf) {
        rectangle.setStroke(tf?DEFAULT_ENABLED_STROKE_COLOR:DEFAULT_STROKE_COLOR);
    }

    /** {@inheritDoc} */
    @Override
    public void setModel(PetriNetElement element) {
        model = (PetriNetTransition) element;
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
    }

    /** {@inheritDoc} */
    @Override
    public void syncFromModel() {
        super.syncFromModel();
        setEnabled(model.isEnabled());
    }
}
