package de.kwirz.yapne.presentation;

/**
 * Builder für {@link PetriNetTransitionPresentation}.
 * <p>
 * <pre>
 *     PetriNetTransitionPresentation place = new PetriNetTransitionPresentation();
 *     place.setSize(..);
 *     place.setStrokeWidth(..);
 *     place.setModel(..);
 * </pre>
 * ist äquivalent zu
 * <pre>
 *      PetriNetTransitionPresentation place = PetriNetTransitionPresentationBuilder.create()
 *                                              .size(..)
 *                                              .strokeWidth(..)
 *                                              .model(..)
 *                                              .build();
 * </pre>
 *
 */
public class PetriNetTransitionPresentationBuilder
        extends PetriNetNodePresentationBuilder<PetriNetTransitionPresentationBuilder> {

    /** Erstellt und gibt einen Builder zurück */
    public static PetriNetTransitionPresentationBuilder create() {
        return new PetriNetTransitionPresentationBuilder();
    }

    /** Erstellt, konfiguriert und gibt eine {@link PetriNetTransitionPresentation} zurück */
    public PetriNetTransitionPresentation build() {
        PetriNetTransitionPresentation transition = new PetriNetTransitionPresentation();
        prepare(transition);

        return transition;
    }
}
