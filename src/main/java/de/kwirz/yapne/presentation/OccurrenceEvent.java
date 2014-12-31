package de.kwirz.yapne.presentation;

import javafx.event.Event;
import javafx.event.EventType;

/**
 * Created by konstantin on 30/12/14.
 */
public class OccurrenceEvent extends Event {

    public static final EventType<OccurrenceEvent> OCCURRED = new EventType<>(ANY, "OCCURRED");

    public OccurrenceEvent() {
        this(OCCURRED);
    }

    public OccurrenceEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
