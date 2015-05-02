package de.kwirz.yapne.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import de.kwirz.yapne.model.*;


/**
 * Dies ist ein einfacher PNML Parser
 */
public class PnmlParser {

    /**
     * Diese Variable dient als Zwischenspeicher für die ID des zuletzt gefundenen Elements.
     */
    private String lastId = null;

    /**
     * Dieses Flag zeigt an, ob der Parser gerade innerhalb eines Token Elements liest.
     */
    private boolean isToken = false;

    /**
     * Dieses Flag zeigt an, ob der Parser gerade innerhalb eines Name Elements liest.
     */
    private boolean isName = false;

    /**
     * Dieses Flag zeigt an, ob der Parser gerade innerhalb eines Value Elements liest.
     */
    private boolean isValue = false;

    /**
     * Wird beim Parsen aufgebaut
     */
    private PetriNet net = null;


    /**
     * Parst <b>input</b> und erstellt daraus Petri Netz Model
     * @param input PNML Repräsentation eines Netzes
     * @return Petri Netz
     */
    public PetriNet parse(final String input) {
        net = new PetriNet();

        InputStream stream = new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8));
        /*
            Dies ist eine Referenz zum XML Parser. Diese Referenz wird durch die
            Methode parse() initialisiert.
        */
        XMLEventReader xmlParser;
        try {
            xmlParser = XMLInputFactory.newInstance().createXMLEventReader(stream);
        } catch (XMLStreamException e) {
            throw new RuntimeException("XML processing error", e);
        }

        try {
            while (xmlParser.hasNext()) {
                XMLEvent event = xmlParser.nextEvent();
                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT:
                        handleStartEvent(event);
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        final String name = event.asEndElement().getName().toString().toLowerCase();
                        switch (name) {
                            case "token":
                                isToken = false;
                                break;
                            case "name":
                                isName = false;
                                break;
                            case "value":
                                isValue = false;
                                break;
                        }
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        if (isValue && lastId != null) {
                            Characters ch = event.asCharacters();
                            if (!ch.isWhiteSpace()) {
                                handleValue(ch.getData());
                            }
                        }
                        break;
                    case XMLStreamConstants.END_DOCUMENT:
                        xmlParser.close();
                        break;
                    default:
                }
            }
        } catch (XMLStreamException e) {
            throw new RuntimeException("could not process PNML document", e);
        } catch (NullPointerException e) {
            throw new RuntimeException("couldn't parse the given source", e);
        }

        return net;
    }

    /**
     * Diese Methode behandelt den Start neuer XML Elemente, in dem der Name des
     * Elements überprüft wird und dann die Behandlung an spezielle Methoden
     * delegiert wird.
     *
     * @param event {@link XMLEvent}
     */

    private void handleStartEvent(final XMLEvent event) {
        StartElement element = event.asStartElement();
        if (element.getName().toString().toLowerCase().equals("transition")) {
            handleTransition(element);
        } else if (element.getName().toString().toLowerCase().equals("place")) {
            handlePlace(element);
        } else if (element.getName().toString().toLowerCase().equals("arc")) {
            handleArc(element);
        } else if (element.getName().toString().toLowerCase().equals("name")) {
            isName = true;
        } else if (element.getName().toString().toLowerCase().equals("position")) {
            handlePosition(element);
        } else if (element.getName().toString().toLowerCase().equals("token")) {
            isToken = true;
        } else if (element.getName().toString().toLowerCase().equals("value")) {
            isValue = true;
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn Text innerhalb eines Value Elements gelesen wird.
     *
     * @param value Der gelesene Text als String
     */
    private void handleValue(final String value) {
        if (isName) {
            setName(lastId, value);
        } else if (isToken) {
            setMarking(lastId, value);
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn ein Positionselement gelesen wird.
     *
     * @param element das Positionselement
     */
    private void handlePosition(final StartElement element) {
        String x = null;
        String y = null;
        Iterator<?> attributes = element.getAttributes();
        while (attributes.hasNext()) {
            Attribute attr = (Attribute) attributes.next();
            if (attr.getName().toString().toLowerCase().equals("x")) {
                x = attr.getValue();
            } else if (attr.getName().toString().toLowerCase().equals("y")) {
                y = attr.getValue();
            }
        }
        if (x != null && y != null && lastId != null) {
            setPosition(lastId, x, y);
        } else {
            System.err.println("Unvollständige Position wurde verworfen!");
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn ein Transitionselement gelesen wird.
     *
     * @param element das Transitionselement
     */
    private void handleTransition(final StartElement element) {
        String transitionId = null;
        Iterator<?> attributes = element.getAttributes();
        while (attributes.hasNext()) {
            Attribute attr = (Attribute) attributes.next();
            if (attr.getName().toString().toLowerCase().equals("id")) {
                transitionId = attr.getValue();
                break;
            }
        }
        if (transitionId != null) {
            newTransition(transitionId);
            lastId = transitionId;
        } else {
            System.err.println("Transition ohne id wurde verworfen!");
            lastId = null;
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn ein Stellenelement gelesen wird.
     *
     * @param element das Stellenelement
     */
    private void handlePlace(final StartElement element) {
        String placeId = null;
        Iterator<?> attributes = element.getAttributes();
        while (attributes.hasNext()) {
            Attribute attr = (Attribute) attributes.next();
            if (attr.getName().toString().toLowerCase().equals("id")) {
                placeId = attr.getValue();
                break;
            }
        }
        if (placeId != null) {
            newPlace(placeId);
            lastId = placeId;
        } else {
            System.err.println("Stelle ohne id wurde verworfen!");
            lastId = null;
        }
    }

    /**
     * Diese Methode wird aufgerufen, wenn ein Kantenelement gelesen wird.
     *
     * @param element das Kantenelement
     */
    private void handleArc(final StartElement element) {
        String arcId = null;
        String source = null;
        String target = null;
        Iterator<?> attributes = element.getAttributes();
        while (attributes.hasNext()) {
            Attribute attr = (Attribute) attributes.next();
            if (attr.getName().toString().toLowerCase().equals("id")) {
                arcId = attr.getValue();
            } else if (attr.getName().toString().toLowerCase().equals("source")) {
                source = attr.getValue();
            } else if (attr.getName().toString().toLowerCase().equals("target")) {
                target = attr.getValue();
            }
        }
        if (arcId != null && source != null && target != null) {
            newArc(arcId, source, target);
        } else {
            System.err.println("Unvollständige Kante wurde verworfen!");
        }
        //Die id von Kanten wird nicht gebraucht
        lastId = null;
    }

    /**
     * Diese Methode kann überschrieben werden, um geladene Transitionen zu erstellen.
     *
     * @param id Identifikationstext der Transition
     */
    public void newTransition(final String id) {
        assert net != null;

        PetriNetTransition transition = new PetriNetTransition(id);
        net.addElement(transition);
    }

    /**
     * Diese Methode kann überschrieben werden, um geladene Stellen zu erstellen.
     *
     * @param id Identifikationstext der Stelle
     */
    public void newPlace(final String id) {
        assert net != null;

        PetriNetPlace place = new PetriNetPlace(id);
        net.addElement(place);
    }

    /**
     * Diese Methode kann überschrieben werden, um geladene Kanten zu erstellen.
     *
     * @param id       Identifikationstext der Kante
     * @param sourceId Identifikationstext des Startelements der Kante
     * @param targetId Identifikationstext des Endelements der Kante
     */
    public void newArc(final String id, final String sourceId, final String targetId) {
        assert net != null;

        PetriNetElement source = net.getElementById(sourceId);
        assert source != null;

        PetriNetElement target = net.getElementById(targetId);
        assert target != null;

        PetriNetArc arc = new PetriNetArc(id);
        arc.setSource((PetriNetNode) source);
        arc.setTarget((PetriNetNode) target);

        net.addElement(arc);
    }

    /**
     * Diese Methode kann überschrieben werden, um die Positionen der geladenen
     * Elemente zu aktualisieren.
     *
     * @param id Identifikationstext des Elements
     * @param x  x Position des Elements
     * @param y  y Position des Elements
     */
    public void setPosition(final String id, final String x, final String y) {
        assert net != null;

        PetriNetNode node = (PetriNetNode) net.getElementById(id);
        assert node != null;

        node.setPosition(new PetriNetNode.Position(Integer.parseInt(x), Integer.parseInt(y)));
    }

    /**
     * Diese Methode kann überschrieben werden, um den Beschriftungstext der geladenen
     * Elemente zu aktualisieren.
     *
     * @param id   Identifikationstext des Elements
     * @param name Beschriftungstext des Elements
     */
    public void setName(final String id, final String name) {
        assert net != null;

        PetriNetNode node = (PetriNetNode) net.getElementById(id);
        assert node != null;

        node.setName(name);
    }

    /**
     * Diese Methode kann überschrieben werden, um die Markierung der geladenen
     * Elemente zu aktualisieren.
     *
     * @param id      Identifikationstext des Elements
     * @param marking Markierung des Elements
     */
    public void setMarking(final String id, final String marking) {
        assert net != null;

        PetriNetPlace place = (PetriNetPlace) net.getElementById(id);
        assert place != null;

        place.setMarking(Integer.parseInt(marking));
    }
}
