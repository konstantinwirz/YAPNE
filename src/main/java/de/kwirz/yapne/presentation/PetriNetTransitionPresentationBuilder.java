package de.kwirz.yapne.presentation;

/**
 * Created by konstantin on 20/12/14.
 */
public class PetriNetTransitionPresentationBuilder
        extends PetriNetNodePresentationBuilder<PetriNetTransitionPresentationBuilder> {

    public static PetriNetTransitionPresentationBuilder create() {
        return new PetriNetTransitionPresentationBuilder();
    }

    public PetriNetTransitionPresentation build() {
        PetriNetTransitionPresentation transition = new PetriNetTransitionPresentation();
        prepare(transition);

        return transition;
    }
}
