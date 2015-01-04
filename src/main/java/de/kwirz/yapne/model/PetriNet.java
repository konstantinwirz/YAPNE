package de.kwirz.yapne.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Repräsentiert ein Petri Netz
 * <p>
 * Verwaltet Elemente des Petri Netzes, liefert die PNML Darstellung des Netzes.
 */
public class PetriNet {

    /**
     * Logger
     */
    private final static Logger logger = Logger.getLogger(PetriNet.class.getName());

    /**
     * Petri Netz Elemente
     */
    private List<PetriNetElement> elements = new ArrayList<>();

    /**
     * Fügt ein Element den Netz hinzu.
     *
     * @exception IllegalArgumentException falls Element ist nicht gültig
     */
    public void addElement(PetriNetElement element) {
        validateElement(element);

        elements.add(element);
    }

    /**
     * Macht Validierung von Elementen
     *
     * @exception IllegalArgumentException falls Element ist nicht gültig
     */
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
            throw new RuntimeException("this place will be never reached");
        }
    }

    /**
     * Macht Validierung von Kanten
     * @exception IllegalArgumentException falls Kante nicht gültig
     */
    private void validateArc(PetriNetArc arc) {
        if (arc.getSource() == null || arc.getTarget() == null) {
            throw new IllegalArgumentException("passed an arc without source or/and target");
        }
    }

    /**
     * Kann Code enthalten zum validieren von Stellen.
     * @exception IllegalArgumentException falls Stelle nicht gültig
     */
    private void validatePlace(PetriNetPlace place) {

    }

    /**
     * Kann Code enthalten zum validieren von Übergängen
     * @exception IllegalArgumentException falls Übergang nicht gültig
     */
    private void validateTransition(PetriNetTransition place) {

    }

    /**
     * Liefert Id's aller Elemente im Netz
     * @return
     */
    public List<String> getIds() {
        ArrayList<String> ids = new ArrayList<>();
        for(PetriNetElement element : this.elements) {
            ids.add(element.getId());
        }
        return ids;
    }

    /**
     * Entfernt alle Element aus dem Netz
     */
    public void clear() {
        elements.clear();
    }

    /**
     * Liefert <code>true</code> falls ein Element mit der gegebenen Id existiert.
     */
    public boolean hasElementById(String id) {
        return getElementById(id) != null;
    }

    /**
     * Liefert Element mit der gegebenen Id
     * @return ein {@linkplain PetriNetElement} oder <code>null</code> falls kein Element existiert.
     */
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
     * Entfernt ein Element aus dem Petri Netz.
     * <p>
     * Falls der Element eine Kante ist, werden Verweise auf diese Kante von Zeil- und
     * Quellknoten entfernt.
     * Falls der Element ein Knoten ist, werden seine Eingangs- und Ausgangskanten entfernt.
     *
     * @param id Element mit dieser Id wird entfernt
     * @exception IllegalArgumentException wenn kein Element mit der gegebenen Id existiert
     */
    public void removeElementById(String id) {
        if (!hasElementById(id))
            throw new IllegalArgumentException(String.format("has no element with id '%s'", id));

        PetriNetElement element = getElementById(id);
        assert element != null;

        if ( element instanceof PetriNetNode ) {
            PetriNetNode node = (PetriNetNode) element;

            Iterator<PetriNetArc> iterator = node.inputArcs.iterator();
            while (iterator.hasNext()) {
                PetriNetArc arc = iterator.next();
                PetriNetNode target = arc.getTarget();
                if (hasElementById(arc.getId()))
                    elements.remove(arc);
                iterator.remove();
            }

            iterator = node.outputArcs.iterator();
            while (iterator.hasNext()) {
                PetriNetArc arc = iterator.next();
                PetriNetNode source = arc.getSource();
                if (hasElementById(arc.getId()))
                    elements.remove(arc);
                iterator.remove();
            }
        } else if (element instanceof PetriNetArc) {
            PetriNetArc arc = (PetriNetArc) element;

            arc.getSource().outputArcs.remove(arc);
            arc.getTarget().inputArcs.remove(arc);
        }

        elements.remove(element);
        logger.log(Level.INFO, "removed element: " + element.getId());
    }

    /**
     * Liefert eine PNML Representation des Netzes
     */
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
