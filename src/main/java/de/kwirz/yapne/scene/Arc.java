package de.kwirz.yapne.scene;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 * Created by konstantin on 19/11/14.
 */
public class Arc extends Path {

    private final double MINIMUM_LENGTH = 80;
    private AbstractNode source = null;
    private AbstractNode target = null;
    private ChangeListener<Number> changeListener = null;

    public Arc() {
        changeListener = new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                update();
            }
        };
    }

    @Override
    public void finalize() throws Throwable {
        clear();
        super.finalize();
    }


    /**
     * Setzt source und target auf Null
     */
    public void clear() {
        setSource(null);
        setTarget(null);
    }

    public AbstractNode getSource() {
        return source;
    }

    public void setSource(AbstractNode source) {
        if (this.source != null) {
            this.source.centerXProperty().addListener(changeListener);
            this.source.centerYProperty().addListener(changeListener);
        }
        this.source = source;
        update();
        source.centerXProperty().addListener(changeListener);
        source.centerYProperty().addListener(changeListener);
    }

    public AbstractNode getTarget() {
        return target;
    }

    public void setTarget(AbstractNode target) {
        if (this.target != null) {
            this.target.centerXProperty().removeListener(changeListener);
            this.target.centerYProperty().removeListener(changeListener);
        }
        this.target = target;
        update();
        target.centerXProperty().addListener(changeListener);
        target.centerYProperty().addListener(changeListener);
    }

    public double getMinimumLength() {
        return MINIMUM_LENGTH;
    }

    private void update() {
        getElements().clear();

        if (source == null || target == null)
            return;

        double x1 = source.getCenterX();
        double y1 = source.getCenterY();
        double x2 = target.getCenterX();
        double y2 = target.getCenterY();
        double dx = x2 - x1;
        double dy = y2 - y1;
        double length = Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dy, 2.0));
        if (length < getMinimumLength())
            return;

        double theta = Math.atan2(dy, dx);
        x1 = x1 + source.getCenterXOffset() * Math.cos(theta);
        y1 = y1 + source.getCenterYOffset() * Math.sin(theta);
        getElements().add(new MoveTo(x1, y1));

        x2 = x2 - target.getCenterXOffset() * Math.cos(theta);
        y2 = y2 - target.getCenterYOffset() * Math.sin(theta);
        getElements().add(new LineTo(x2, y2));

        double phi = Math.toRadians(40);
        double rho = theta + phi;
        double barb = 10;
        double x = x2 - barb * Math.cos(rho);
        double y = y2 - barb * Math.sin(rho);
        getElements().add(new LineTo(x, y));

        getElements().add(new MoveTo(x2, y2));

        rho = theta - phi;
        x = x2 - barb * Math.cos( rho );
        y = y2 - barb * Math.sin( rho );
        getElements().add(new LineTo(x, y));
    }
}
