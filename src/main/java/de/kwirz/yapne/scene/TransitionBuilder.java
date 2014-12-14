package de.kwirz.yapne.scene;

import de.kwirz.yapne.utils.BuilderValue;

/**
 * Created by konstantin on 19/11/14.
 */
public class TransitionBuilder extends AbstractNodeBuilder<TransitionBuilder> {

    private BuilderValue<Boolean> enabled_ = new BuilderValue<>();

    public TransitionBuilder enabled(boolean tf) {
        enabled_.setValue(tf);
        return this;
    }

    public static TransitionBuilder create() {
        return new TransitionBuilder();
    }

    public Transition build() {
        Transition transition = new Transition();
        prepare(transition);
        if (enabled_.isSet())
            transition.setEnabled(enabled_.getValue());

        return transition;
    }
}
