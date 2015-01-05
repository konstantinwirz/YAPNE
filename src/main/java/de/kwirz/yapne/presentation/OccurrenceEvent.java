package de.kwirz.yapne.presentation;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Event wird ausgelöst beim Schalten einer Transition
 */
public class OccurrenceEvent extends Event {

    public static final EventType<OccurrenceEvent> OCCURRED = new EventType<>(ANY, "OCCURRED");

    public OccurrenceEvent() {
        this(OCCURRED);
    }

    private OccurrenceEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
