package de.kwirz.yapne.presentation;

import de.kwirz.yapne.model.PetriNetElement;
import javafx.scene.paint.Color;

/**
 * Grafische Darstellung eines Petri Netz Elementes
 * <p>Als Model dient {@link PetriNetElement}
 */
public interface PetriNetElementPresentation {

    /** Minimale Linienstärke = {@value #MINIMUM_STROKE_WIDTH}  */
    public final static double MINIMUM_STROKE_WIDTH = 0.5;

    /** Maximale Linienstärke = {@value #MAXIMUM_STROKE_WIDTH} */
    public final static double MAXIMUM_STROKE_WIDTH = 10.0;

    /** Standard Linienstärke = {@value #DEFAULT_STROKE_WIDTH} */
    public final static double DEFAULT_STROKE_WIDTH = 2.0;

    /** Linienfarbe */
    public final static Color DEFAULT_STROKE_COLOR = Color.BLACK;

    /** Setzt den Model */
    public void setModel(PetriNetElement element);

    /** Gibt den Model zurück */
    public PetriNetElement getModel();

    /** Schreibt Änderungen in den Model */
    public void syncToModel();

    /** Liest Änderungen aus dem Model */
    public void syncFromModel();

}
