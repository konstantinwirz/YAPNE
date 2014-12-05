package edu.yapne.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by konstantin on 28/11/14.
 */
public class PetriNet {

    private List<PetriNetElement> elements = new ArrayList<>();

    public PetriNet() {

    }

    public void addElement(PetriNetElement element) {
        validateElement(element);

        elements.add(element);
    }

    private void validateElement(PetriNetElement element) {
        if (element.getId().trim().isEmpty())
            throw new IllegalArgumentException("passed an element with empty id");

        if (hasElementById(element.getId()))
            throw new IllegalArgumentException(String.format("element with id '%s' already exists", element.getId()));

        if (element instanceof PetriNetArc) {
            validateArc((PetriNetArc)element);
        } else if (element instanceof PetriNetPlace) {
            validatePlace((PetriNetPlace)element);
        } else if (element instanceof PetriNetTransition) {
            validateTransition((PetriNetTransition)element);
        } else {
            assert false; // should be never reached
        }
    }

    private void validateArc(PetriNetArc arc) {
        if (arc.getSource() == null || arc.getTarget() == null) {
            throw new IllegalArgumentException("passed an arc without source or/and target");
        }
    }

    private void validatePlace(PetriNetPlace place) {

    }

    private void validateTransition(PetriNetTransition place) {

    }

    public List<String> getIds() {
        ArrayList<String> ids = new ArrayList<>();
        for(PetriNetElement element : this.elements) {
            ids.add(element.getId());
        }
        return ids;
    }

    public void clear() {
        elements.clear();
    }

    public boolean hasElementById(String id) {
        return getElementById(id) != null;
    }

    public PetriNetElement getElementById(String id) {
        PetriNetElement element = null;
        for (PetriNetElement el : elements) {
            if (id.equals(el.getId())) {
                element = el;
                break;
            }
        }

        return element;
    }

    /**
     * Entfernt ein Element mit der id aus dem Petri Netz, falls Element ein PetriNetNode ist,
     * werden auch seine Eingangs- sowie Ausgangs- Kanten entfernt.
     */
    public void removeElementById(String id) {
        if (!hasElementById(id))
            throw new IllegalArgumentException(String.format("has no element with id '%s'", id));

        PetriNetElement element = getElementById(id);
        assert element != null;

        if ( element instanceof PetriNetNode ) {
            PetriNetNode node = (PetriNetNode) element;

            for (PetriNetArc arc : node.inputArcs) {
                if (hasElementById(arc.getId()))
                    removeElementById(arc.getId());
            }

            for (PetriNetArc arc : node.outputArcs) {
                if (hasElementById(arc.getId()))
                    removeElementById(arc.getId());
            }
        }


        elements.remove(element);
    }


    public String toXml() {
        String elementsXml = "";
        for (PetriNetElement element : elements) {
            elementsXml += element.toXml() + "\n";
        }

        return "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?>\n" +
                "<pnml>\n" +
                "<net>\n" +
                elementsXml +
                "</net>\n" +
                "</pnml>";
    }

}
