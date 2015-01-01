package de.kwirz.yapne.presentation;

import de.kwirz.yapne.model.PetriNetElement;
import javafx.scene.paint.Color;

/**
 * Created by konstantin on 20/12/14.
 */
public interface PetriNetElementPresentation {

    public static double MINIMUM_STROKE_WIDTH = 0.5;
    public static double MAXIMUM_STROKE_WIDTH = 10.0;
    public static double DEFAULT_STROKE_WIDTH = 2.0;
    public static Color DEFAULT_STROKE_COLOR = Color.BLACK;

    public void setModel(PetriNetElement element);
    public PetriNetElement getModel();

    public void syncToModel();
    public void syncFromModel();
}
