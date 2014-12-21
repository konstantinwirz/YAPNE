package de.kwirz.yapne.presentation;

import de.kwirz.yapne.model.PetriNetElement;

/**
 * Created by konstantin on 20/12/14.
 */
public interface PetriNetElementPresentation {

    public void setModel(PetriNetElement element);
    public PetriNetElement getModel();

    public void syncToModel();
    public void syncFromModel();
}
