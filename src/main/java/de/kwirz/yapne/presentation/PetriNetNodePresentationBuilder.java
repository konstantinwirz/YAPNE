package de.kwirz.yapne.presentation;

import de.kwirz.yapne.model.PetriNetElement;
import de.kwirz.yapne.utils.BuilderValue;


/**
 * Basisklasse für {@link PetriNetPlacePresentationBuilder} und
 * {@link de.kwirz.yapne.presentation.PetriNetTransitionPresentationBuilder}.
 * <p>
 * Hier werden gemeinsame Eigenschaften konfiguriert. Damit die Methoden abgeleiteter Klassen
 * passenden statischen Typ zurückgeben, wird diese Klasse mittels Generics implementiert.
 * @see de.kwirz.yapne.presentation.PetriNetTransitionPresentationBuilder
 * @see de.kwirz.yapne.presentation.PetriNetPlacePresentationBuilder
 *
 */
public class PetriNetNodePresentationBuilder<T extends PetriNetNodePresentationBuilder<T>>  {

    /** Hält den Wert der <b>size</b> Eigenschaft */
    protected BuilderValue<Double> size_ = new BuilderValue<>();

    /** Hält den Wert der <b>strokeWidth</b> Eigenschaft */
    protected BuilderValue<Double> strokeWidth_ = new BuilderValue<>();

    /** Hält den Model */
    protected BuilderValue<PetriNetElement> model_ = new BuilderValue<>();

    /** Setzt die <b>size</b> Eigenschaft */
    public T size(double sz) {
        size_.setValue(sz);
        return (T)this;
    }

    /** Setzt den Model */
    public T model(PetriNetElement element) {
        model_.setValue(element);
        return (T)this;
    }

    /** Setzt die <b>strokeWidth</b> Eigenschaft */
    public T strokeWidth(double width) {
        strokeWidth_.setValue(width);
        return (T)this;
    }

    /**
     * Setzt die Werte.
     * <p>
     * Muss von abgeleiteten Klassen beim Bauen ausgeführt werden
     * @param node Knoten zum konfigurieren.
     */
    protected void prepare(PetriNetNodePresentation node) {
        if (size_.isSet())
            node.setSize(size_.getValue());

        if (strokeWidth_.isSet())
            node.setStrokeWidth(strokeWidth_.getValue());

        if (model_.isSet())
            node.setModel(model_.getValue());
    }
}
