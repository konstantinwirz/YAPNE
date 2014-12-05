package edu.yapne.scene;

/**
 * Created by konstantin on 19/11/14.
 */
public class TransitionBuilder extends AbstractNodeBuilder<TransitionBuilder> {

    public static TransitionBuilder create() {
        return new TransitionBuilder();
    }

    public Transition build() {
        Transition transition = new Transition();
        prepare(transition);
        return transition;
    }
}
