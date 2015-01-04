package de.kwirz.yapne.model;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by konstantin on 26/11/14.
 */
public class PetriNetTransition extends PetriNetNode {


    public PetriNetTransition(String id) {
        super(id);
    }

    @Override
    public String toXml() {
        return String.format("<transition id=\"%s\">\n" +
                "<name>\n" +
                "<value>%s</value>\n" +
                "</name>\n" +
                "<graphics>\n" +
                "%s\n" +
                "</graphics>\n" +
                "</transition>", getId(), getName(), getPosition().toXml());
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

    /**
     * Gibt alle Eingangsstellen dieser Transition zurück.
     */
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

    public void occur() {
        if (!isEnabled()) {
            logger.log(Level.WARNING, "transition is not enabled, nothing to do...");
            return;
        }

        if (outputArcs.isEmpty()) {
            logger.log(Level.WARNING, "there are no output arcs, nothing to do...");
            return;
        }

        int marking = 0;
        for (PetriNetArc inputArc : inputArcs) {
            assert inputArc.getSource() instanceof PetriNetPlace;

            PetriNetPlace place = (PetriNetPlace) inputArc.getSource();

            assert place.getMarking() > 0;

            place.setMarking(place.getMarking() - 1);
            ++marking;
        }

        assert marking == inputArcs.size();

        for (PetriNetArc outputArc : outputArcs) {
            assert outputArc.getTarget() instanceof PetriNetPlace;

            PetriNetPlace place = (PetriNetPlace) outputArc.getTarget();
            place.setMarking(place.getMarking() + marking);
        }
    }
}
