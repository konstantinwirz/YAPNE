package edu.yapne.scene;

import edu.yapne.utils.BuilderValue;

/**
 * Created by konstantin on 20/11/14.
 */
public class ArcBuilder {
    BuilderValue<AbstractNode> source_ = new BuilderValue<AbstractNode>();
    BuilderValue<AbstractNode> target_ = new BuilderValue<AbstractNode>();

    public static ArcBuilder create() {
        return new ArcBuilder();
    }

    public ArcBuilder source(AbstractNode source) {
        source_.setValue(source);
        return this;
    }

    public ArcBuilder target(AbstractNode target) {
        target_.setValue(target);
        return this;
    }

    public Arc build() {
        Arc arc = new Arc();

        if (source_.isSet())
            arc.setSource(source_.getValue());
        if (target_.isSet())
            arc.setTarget(target_.getValue());

        return arc;
    }

}
