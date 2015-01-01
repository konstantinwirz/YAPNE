package de.kwirz.yapne.presentation;

import de.kwirz.yapne.model.PetriNetArc;
import de.kwirz.yapne.model.PetriNetElement;
import de.kwirz.yapne.utils.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;

/**
 *
 * Grafische Darstellung einer Kante
 *
 */
public class PetriNetArcPresentation extends Path implements PetriNetElementPresentation {

    /**
     * Kanten die kürzer sind werden nicht gezeichnet
     */
    private static final double MINIMUM_LENGTH = 50;
    private PetriNetNodePresentation source = null;
    private PetriNetNodePresentation target = null;
    private PetriNetArc model = null;
    private ChangeListener<Number> changeListener = new ChangeListener<Number>() {
        @Override
        public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
            update();
        }
    };

    /**
     * Erstellt eine Instanz
     */
    public PetriNetArcPresentation() {
        setupUi();
        registerListeners();
    }

    private void registerListeners() {
        // erzwingt dass strokeWidth zwischen Min und Max Werten liegt.
        strokeWidthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                setStrokeWidth(Utils.ensureRange(newValue.doubleValue(),
                        MINIMUM_STROKE_WIDTH, MAXIMUM_STROKE_WIDTH));
            }
        });
    }

    private void setupUi() {
        setPickOnBounds(true);
        setSmooth(true);
    }

    /**
     * Setzt source und target auf Null
     */
    public void clear() {
        setSource(null);
        setTarget(null);
    }

    public PetriNetNodePresentation getSource() {
        return source;
    }

    public void setSource(PetriNetNodePresentation source) {
        if (this.source != null) {
            this.source.centerXProperty().removeListener(changeListener);
            this.source.centerYProperty().removeListener(changeListener);
        }
        this.source = source;
        update();
        source.centerXProperty().addListener(changeListener);
        source.centerYProperty().addListener(changeListener);
    }

    public PetriNetNodePresentation getTarget() {
        return target;
    }

    public void setTarget(PetriNetNodePresentation target) {
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
        final double dx = x2 - x1;
        final double dy = y2 - y1;
        double length = Math.sqrt(Math.pow(dx, 2.0) + Math.pow(dy, 2.0));
        if (length < getMinimumLength())
            return;

        double theta = Math.atan2(dy, dx);

        double sourceOffset = source.getSize() / 2;
        double targetOffset = target.getSize() / 2;

        class QuadratOffsetCalculator {
            double calculate() {
                double alpha = Math.toDegrees(Math.atan2(Math.abs(dx), Math.abs(dy)));
                double leg = target.getSize() / 2.0;
                double hypotenuse = Math.sqrt( Math.pow(leg, 2.0) + Math.pow(leg, 2.0));
                double offset = 0.0;
                if ( alpha < 45 ) {
                    offset = (hypotenuse * Math.cos(Math.toRadians(45))) / Math.cos(Math.toRadians(alpha));
                } else {
                    offset = (hypotenuse * Math.sin(Math.toRadians(45))) / Math.sin(Math.toRadians(alpha));
                }
                return offset;
            }
        }

        if (target instanceof PetriNetTransitionPresentation) {
            targetOffset = new QuadratOffsetCalculator().calculate();
        } else if (source instanceof PetriNetTransitionPresentation) {
            sourceOffset = new QuadratOffsetCalculator().calculate();
        }

        sourceOffset += source.getStrokeWidth();
        targetOffset += target.getStrokeWidth();

        x1 = x1 + sourceOffset * Math.cos(theta);
        y1 = y1 + sourceOffset * Math.sin(theta);
        getElements().add(new MoveTo(x1, y1));

        x2 = x2 - targetOffset * Math.cos(theta);
        y2 = y2 - targetOffset * Math.sin(theta);
        getElements().add(new LineTo(x2, y2));

        double phi = Math.toRadians(40);
        double rho = theta + phi;
        double barb = 8;
        double x = x2 - barb * Math.cos(rho);
        double y = y2 - barb * Math.sin(rho);
        getElements().add(new LineTo(x, y));

        getElements().add(new MoveTo(x2, y2));

        rho = theta - phi;
        x = x2 - barb * Math.cos( rho );
        y = y2 - barb * Math.sin( rho );

        getElements().add(new LineTo(x, y));
    }

    @Override
    public void setModel(PetriNetElement element) {
        this.model = (PetriNetArc) element;
    }

    @Override
    public PetriNetElement getModel() {
        return model;
    }

    @Override
    public void syncToModel() {

    }

    @Override
    public void syncFromModel() {

    }

}