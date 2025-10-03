package com.soulware.therapydraft.shared.domain.model.events;

import java.time.ZonedDateTime;

/**
 * Base abstract class for all Domain Events in the Bounded Context.
 * Provides a common structure and ensures that every concrete event has an
 * immutable timestamp recording when the event was created/published.
 */
public abstract class BaseAbstractDomainEvent implements DomainEvent {

    private final ZonedDateTime occurredAt;

    /**
     * Constructs the base event and sets the immutable timestamp of the moment
     * the event object was created, representing when the event occurred in the domain.
     */
    protected BaseAbstractDomainEvent() {
        this.occurredAt = ZonedDateTime.now();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ZonedDateTime getOccurredAt() {
        return this.occurredAt;
    }
}
