package de.kwirz.yapne.presentation;

import de.kwirz.yapne.model.PetriNetArc;
import de.kwirz.yapne.utils.BuilderValue;

/**
 * Builder für {@link PetriNetArcPresentation}.
 * <p>
 * <pre>
 * PetriNetArcPresentation arc = new PetriNetArcPresentation();
 * arc.setSource(..);
 * arc.setTarget(..);
 * arc.setModel(..);
 * arc.setStrokeWidth(..);
 * </pre>
 * ist äquivalent zu
 * <pre>
 * PetriNetArcPresentation arc = PetriNetArcPresentationBuilder.create()
 *                              .source(..)
 *                              .target(..)
 *                              .model(..)
 *                              .srokeWidth(..)
 *                              .build();
 * </pre>
 */
public class PetriNetArcPresentationBuilder {
    BuilderValue<PetriNetNodePresentation> source_ = new BuilderValue<>();
    BuilderValue<PetriNetNodePresentation> target_ = new BuilderValue<>();
    BuilderValue<PetriNetArc> model_ = new BuilderValue<>();
    BuilderValue<Double> width_ = new BuilderValue<>();

    /** Erstellt und gibt einen Builder zurück. */
    public static PetriNetArcPresentationBuilder create() {
        return new PetriNetArcPresentationBuilder();
    }

    /** Setzt den Quellknoten */
    public PetriNetArcPresentationBuilder source(PetriNetNodePresentation source) {
        source_.setValue(source);
        return this;
    }

    /** Setzt den Zielknoten */
    public PetriNetArcPresentationBuilder target(PetriNetNodePresentation target) {
        target_.setValue(target);
        return this;
    }

    /** Sett den Model */
    public PetriNetArcPresentationBuilder model(PetriNetArc model) {
        model_.setValue(model);
        return this;
    }

    /** Setzt die Linienstärke */
    public PetriNetArcPresentationBuilder strokeWidth(double width) {
        width_.setValue(width);
        return this;
    }

    /** Erstellt, konfiguriert und gibt eine {@link PetriNetArcPresentation} zurück */
    public PetriNetArcPresentation build() {
        PetriNetArcPresentation presentation = new PetriNetArcPresentation();

        if (source_.isSet())
            presentation.setSource(source_.getValue());

        if (target_.isSet())
            presentation.setTarget(target_.getValue());

        if (model_.isSet())
            presentation.setModel(model_.getValue());

        if (width_.isSet())
            presentation.setStrokeWidth(width_.getValue());

        return presentation;
    }

}
