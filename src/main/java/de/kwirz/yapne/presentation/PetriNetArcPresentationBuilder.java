package de.kwirz.yapne.presentation;

import de.kwirz.yapne.model.PetriNetArc;
import de.kwirz.yapne.model.PetriNetElement;
import de.kwirz.yapne.utils.BuilderValue;

/**
 * Created by konstantin on 21/12/14.
 */
public class PetriNetArcPresentationBuilder {
    BuilderValue<PetriNetNodePresentation> source_ = new BuilderValue<>();
    BuilderValue<PetriNetNodePresentation> target_ = new BuilderValue<>();
    BuilderValue<PetriNetArc> model_ = new BuilderValue<>();
    BuilderValue<Double> width_ = new BuilderValue<>();

    public static PetriNetArcPresentationBuilder create() {
        return new PetriNetArcPresentationBuilder();
    }

    public PetriNetArcPresentationBuilder source(PetriNetNodePresentation source) {
        source_.setValue(source);
        return this;
    }

    public PetriNetArcPresentationBuilder target(PetriNetNodePresentation target) {
        target_.setValue(target);
        return this;
    }

    public PetriNetArcPresentationBuilder model(PetriNetArc model) {
        model_.setValue(model);
        return this;
    }

    public PetriNetArcPresentationBuilder strokeWidth(double width) {
        width_.setValue(width);
        return this;
    }

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
