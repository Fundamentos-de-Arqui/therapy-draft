package com.soulware.therapydraft.shared.domain.model.events;

import java.time.ZonedDateTime;

/**
 * Contract for all Domain Events.
 */
public interface DomainEvent {
    /**
     * Gets the date that the event was published at.
     * @return The occurredAt date of the event.
     */
    ZonedDateTime getOccurredAt();
}
