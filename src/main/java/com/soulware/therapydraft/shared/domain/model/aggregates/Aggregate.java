package com.soulware.therapydraft.shared.domain.model.aggregates;

import com.soulware.therapydraft.shared.domain.model.valueobjects.DomainId;

/**
 * Contract for all Domain Aggregates.
 */
public interface Aggregate {
    /**
     * Gets the identifier for the aggregate, used for identity comparison.
     * @return The unique identifier value.
     */
    DomainId getId();

    /**
     * Publishes an event to be executed within the current transaction context.
     * @param event a domain event.
     */
    void publishEvent(Object event);

    /**
     * Publishes an event to be executed asynchronously.
     * @param event a domain event.
     */
    void publishEventAsync(Object event);
}
