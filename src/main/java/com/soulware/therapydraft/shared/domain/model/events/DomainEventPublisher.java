package com.soulware.therapydraft.shared.domain.model.events;

/**
 * Contract for publishing a Domain Event.
 * Allows Aggregate Roots to remain decoupled from the underlying event infrastructure (CDI, JMS, etc.).
 */
public interface DomainEventPublisher {

    /**
     * Publishes a Domain Event to all registered observers or consumers.
     * The event is typically executed synchronously within the current transaction.
     * @param event The Domain Event object (a simple POJO) to be published.
     */
    void publish(DomainEvent event);

    /**
     * Publishes a Domain Event for asynchronous processing.
     * @param event The Domain Event object to be published.
     */
    void publishAsync(DomainEvent event);
}
