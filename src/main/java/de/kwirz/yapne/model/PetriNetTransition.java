package de.kwirz.yapne.model;

import java.util.ArrayList;
import java.util.List;

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
     * Eine Transition ohne Eingangsstellen ist stets aktiviert
     * oder wenn jede ihrer Eingangsstellen (Stellen, von denen eine Kante zur Transition führt)
     * wenigstens eine Marke trägt
     * @return {@code true} falls Transition aktiviert ist, sonst {@code false}
     */
    public boolean isEnabled() {
        if (inputArcs.isEmpty())
            return true;

        int minimumMarking = 1;
        List<PetriNetPlace> places = getInputPlaces();
        for (PetriNetPlace place : places) {
            minimumMarking = (minimumMarking > place.getMarking())?place.getMarking():minimumMarking;
        }

        if (!places.isEmpty() && minimumMarking >= 1)
            return true;

        return false;
    }

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
        if (!isEnabled())
            return;

        int marking = 0;
        for (PetriNetArc inputArc : inputArcs) {
            if ( !(inputArc.getSource() instanceof PetriNetPlace) )
                continue;
            PetriNetPlace place = (PetriNetPlace) inputArc.getSource();
            if (place.getMarking() >= 1) {
                ++marking;
                place.setMarking(place.getMarking() - 1);
            }
        }

        for (PetriNetArc outputArc : outputArcs) {
            if ( !(outputArc.getTarget() instanceof PetriNetPlace) )
                continue;
            PetriNetPlace place = (PetriNetPlace) outputArc.getTarget();
            place.setMarking(place.getMarking() + marking);
        }
    }
}
