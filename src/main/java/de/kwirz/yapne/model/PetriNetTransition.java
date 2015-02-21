package de.kwirz.yapne.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Repräsentiert eine Transition im Petri Netz
 */
public final class PetriNetTransition extends PetriNetNode {

    /**
     * Erstellt eine Transition
     * @param id Kennung
     */
    public PetriNetTransition(String id) {
        super(id);
    }

    /** {@inheritDoc} */
    @Override
    public String toPNML() {
        return String.format("<transition id=\"%s\">\n" +
                "<name>\n" +
                "<value>%s</value>\n" +
                "</name>\n" +
                "<graphics>\n" +
                "%s\n" +
                "</graphics>\n" +
                "</transition>", getId(), getName(), getPosition().toPNML());
    }

    /**
     * Gibt <code>true</code> zurück falls diese Transition aktiviert ist.
     * <p>
     * Eine Transition ist aktiviert falls:
     * <ul>
     *     <li>keine Eingangsstellen vorhanden</li>
     *     <li>jede ihrer Eingangsstellen wenigstens eine Marke trägt</li>
     * </ul>
     *
     */
    public boolean isEnabled() {
        for (PetriNetPlace place : getInputPlaces()) {
            if (place.getMarking() < 1)
                return false;
        }

        return true;
    }

    /** Gibt alle Eingangsstellen zurück. */
    private List<PetriNetPlace> getInputPlaces() {
        List<PetriNetPlace> places = new ArrayList<>();

        if (inputArcs != null) {
            for (PetriNetArc arc : inputArcs) {
                PetriNetNode node = arc.getSource();
                if (node instanceof PetriNetPlace) {
                    PetriNetPlace place = (PetriNetPlace) node;
                    places.add(place);
                }
            }
        }

        return places;
    }

    /**
     * Schaltet die Transition
     * <p>
     * Damit die Schaltung erfolgt muss diese Transition aktiviert sein
     */
    public void occur() {
        if (!isEnabled()) {
            logger.log(Level.WARNING, "transition is not enabled, nothing to do...");
            return;
        }

        if (!inputArcs.isEmpty())
            decrementMarkingOfInputPlaces();

        if (!outputArcs.isEmpty())
            incrementMarkingOfOutputPlaces();
    }

    /**
     * Inkrementiert Markierungen aller Ausgangsstellen
     */
    private void incrementMarkingOfOutputPlaces() {
        for (PetriNetArc outputArc : outputArcs) {
            assert outputArc.getTarget() instanceof PetriNetPlace;

            PetriNetPlace place = (PetriNetPlace) outputArc.getTarget();
            place.setMarking(place.getMarking() + 1);
        }
    }

    /**
     * Decrementiert Markierungen aller Eingangsstellen.
     */
    private void decrementMarkingOfInputPlaces() {
        for (PetriNetArc inputArc : inputArcs) {
            assert inputArc.getSource() instanceof PetriNetPlace;
            PetriNetPlace place = (PetriNetPlace) inputArc.getSource();
            assert place.getMarking() > 0;
            place.setMarking(place.getMarking() - 1);
        }
    }

}
