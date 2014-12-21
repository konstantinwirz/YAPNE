package de.kwirz.yapne.presentation;

import de.kwirz.yapne.utils.BuilderValue;


/**
 * Created by konstantin on 20/12/14.
 */
public class PetriNetPlacePresentationBuilder
        extends PetriNetNodePresentationBuilder<PetriNetPlacePresentationBuilder> {

    private BuilderValue<Integer> marking_ = new BuilderValue<Integer>();

    public static PetriNetPlacePresentationBuilder create() {
        return new PetriNetPlacePresentationBuilder();
    }

    public PetriNetPlacePresentationBuilder marking(int value) {
        marking_.setValue(value);
        return this;
    }

    public PetriNetPlacePresentation build() {
        PetriNetPlacePresentation place = new PetriNetPlacePresentation();
        prepare(place);

        if (marking_.isSet())
            place.setMarking(marking_.getValue());

        return place;
    }

}
