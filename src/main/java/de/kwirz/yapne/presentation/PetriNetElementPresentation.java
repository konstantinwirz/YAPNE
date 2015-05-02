package de.kwirz.yapne.presentation;

import de.kwirz.yapne.model.PetriNetElement;
import javafx.scene.paint.Color;

/**
 * Grafische Darstellung eines Petri Netz Elementes
 * <p>Als Model dient {@link PetriNetElement}
 */
public interface PetriNetElementPresentation {

    /** Minimale Linienstärke = {@value #MINIMUM_STROKE_WIDTH}  */
    double MINIMUM_STROKE_WIDTH = 0.5;

    /** Maximale Linienstärke = {@value #MAXIMUM_STROKE_WIDTH} */
    double MAXIMUM_STROKE_WIDTH = 10.0;

    /** Standard Linienstärke = {@value #DEFAULT_STROKE_WIDTH} */
    double DEFAULT_STROKE_WIDTH = 2.0;

    /** Linienfarbe */
    Color DEFAULT_STROKE_COLOR = Color.BLACK;

    /** Setzt den Model */
    void setModel(PetriNetElement element);

    /** Gibt den Model zurück */
    PetriNetElement getModel();

    /** Schreibt Änderungen in den Model */
    void syncToModel();

    /** Liest Änderungen aus dem Model */
    void syncFromModel();

}
