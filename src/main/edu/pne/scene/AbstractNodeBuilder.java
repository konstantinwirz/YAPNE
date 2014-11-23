package edu.pne.scene;

import com.sun.xml.internal.rngom.dt.builtin.BuiltinDatatypeLibrary;
import edu.pne.utils.BuilderValue;
import javafx.scene.paint.Color;

/**
 * Created by konstantin on 18/11/14.
 */
public abstract class AbstractNodeBuilder<T extends AbstractNodeBuilder<T>> {
    protected BuilderValue<String> label_ = new BuilderValue<String>();
    protected BuilderValue<Double> size_ = new BuilderValue<Double>();
    protected BuilderValue<Double> strokeWidth_ = new BuilderValue<Double>();
    protected BuilderValue<Color> fillColor_ = new BuilderValue<Color>();
    protected BuilderValue<Color> strokeColor_ = new BuilderValue<Color>();
    protected BuilderValue<Double> centerX_ = new BuilderValue<Double>();
    protected BuilderValue<Double> centerY_ = new BuilderValue<Double>();

    public T label(String text) {
        label_.setValue(text);
        return (T)this;
    }

    public T size(double sz) {
        size_.setValue(sz);
        return (T)this;
    }

    public T strokeWidth(double width) {
        strokeWidth_.setValue(width);
        return (T)this;
    }

    public T fillColor(Color color) {
        fillColor_.setValue(color);
        return (T)this;
    }

    public T strokeColor(Color color) {
        strokeColor_.setValue(color);
        return (T)this;
    }

    public T centerX(double x) {
        centerX_ .setValue(x);
        return (T)this;
    }

    public T centerY(double y) {
        centerY_.setValue(y);
        return (T)this;
    }

    protected void prepare(AbstractNode node) {
        if (label_.isSet())
            node.setLabel(label_.getValue());

        if (size_.isSet())
            node.setSize(size_.getValue());

        if (strokeWidth_.isSet())
            node.setStrokeWidth(strokeWidth_.getValue());

        if (fillColor_.isSet())
            node.setFillColor(fillColor_.getValue());

        if (strokeColor_.isSet())
            node.setStrokeColor(strokeColor_.getValue());

        if (centerX_.isSet())
            node.setCenterX(centerX_.getValue());

        if (centerY_.isSet())
            node.setCenterY(centerY_.getValue());
    }

}
