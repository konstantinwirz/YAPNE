package de.kwirz.yapne.presentation;

import de.kwirz.yapne.model.PetriNetElement;
import de.kwirz.yapne.utils.BuilderValue;


/**
 * Created by konstantin on 20/12/14.
 */
public class PetriNetNodePresentationBuilder<T extends PetriNetNodePresentationBuilder<T>>  {
    protected BuilderValue<Double> size_ = new BuilderValue<>();
    protected BuilderValue<Double> strokeWidth_ = new BuilderValue<>();
    protected BuilderValue<PetriNetElement> model_ = new BuilderValue<>();

    public T size(double sz) {
        size_.setValue(sz);
        return (T)this;
    }

    public T model(PetriNetElement element) {
        model_.setValue(element);
        return (T)this;
    }

    public T strokeWidth(double width) {
        strokeWidth_.setValue(width);
        return (T)this;
    }

    protected void prepare(PetriNetNodePresentation node) {
        if (size_.isSet())
            node.setSize(size_.getValue());

        if (strokeWidth_.isSet())
            node.setStrokeWidth(strokeWidth_.getValue());

        if (model_.isSet())
            node.setModel(model_.getValue());
    }
}
