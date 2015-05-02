package de.kwirz.yapne.presentation;

import de.kwirz.yapne.utils.BuilderValue;


/**
 * Builder für {@link PetriNetPlacePresentation}.
 * <pre>
 *     PetriNetPlacePresentation place = new PetriNetPlacePresentation();
 *     place.setSize(..);
 *     place.setStrokeWidth(..);
 *     place.setModel(..);
 *     place.setMarking(..);
 * </pre>
 * ist äquivalent zu
 * <pre>
 *  PetriNetPlacePresentation place = PetriNetPlacePresentationBuilder.create()
 *                                      .size(..)
 *                                      .strokeWidth(..)
 *                                      .model(..)
 *                                      .marking(..)
 *                                      .build();
 * </pre>
 *
 *
 */
public class PetriNetPlacePresentationBuilder
        extends PetriNetNodePresentationBuilder<PetriNetPlacePresentationBuilder> {

    /** Hält den Wert der <b>marking</b> Eigenschaft */
    private BuilderValue<Integer> marking_ = new BuilderValue<>();

    /** Erstellt und gibt einen <b>PetriNetPlacePresentationBuilder</b> zurück */
    public static PetriNetPlacePresentationBuilder create() {
        return new PetriNetPlacePresentationBuilder();
    }

    /** Gibt die Markierung zurück */
    @SuppressWarnings("unused")
    public PetriNetPlacePresentationBuilder marking(int value) {
        marking_.setValue(value);
        return this;
    }

    /** Erstellt, konfiguriert und gibt eine {@link PetriNetPlacePresentation} zurück */
    public PetriNetPlacePresentation build() {
        PetriNetPlacePresentation place = new PetriNetPlacePresentation();
        prepare(place);

        if (marking_.isSet())
            place.setMarking(marking_.getValue());

        return place;
    }

}
