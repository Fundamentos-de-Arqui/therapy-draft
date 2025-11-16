package com.soulware.therapydraft.shared.domain.model.aggregates;

import com.soulware.therapydraft.shared.domain.model.events.DomainEvent;
import com.soulware.therapydraft.shared.domain.model.events.DomainEventPublisher;
import com.soulware.therapydraft.shared.domain.model.valueobjects.DomainId;

import java.util.Objects;

/**
 * Base abstract class for all Aggregate Roots in the Domain Bounded Context.
 * Extends the Entity concept to enforce transactional integrity and provide 
 * the mechanism for publishing Domain Events through a dedicated service.
 */
public abstract class BaseAbstractAggregate implements Aggregate {
    private final DomainId id;
    private final DomainEventPublisher eventPublisher;

    /**
     * Constructs the base aggregate root, initializing its unique identifier and event publisher.
     */
    protected BaseAbstractAggregate(DomainId id, DomainEventPublisher eventPublisher) {
        if (id == null) {
            throw new IllegalArgumentException("Aggregate ID cannot be null.");
        }
        Objects.requireNonNull(eventPublisher, "Domain Event Publisher cannot be null.");

        this.id = id;
        this.eventPublisher = eventPublisher;
    }

    protected BaseAbstractAggregate(DomainEventPublisher eventPublisher) {
        Objects.requireNonNull(eventPublisher, "Domain Event Publisher cannot be null.");

        this.id = null;
        this.eventPublisher = eventPublisher;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DomainId getId() {
        return id;
    }

    /**
     * Publishes a Domain Event for synchronous processing using the injected {@link DomainEventPublisher}.
     * This method is used by Aggregate behaviors to notify other domain components 
     * of state changes within the current transaction.
     * @param event The DomainEvent object to publish.
     */
    @Override
    public void publishEvent(Object event) {
        this.eventPublisher.publish((DomainEvent) event);
    }

    /**
     * Publishes a Domain Event for asynchronous processing.
     * @param event The DomainEvent object to publish.
     */
    @Override
    public void publishEventAsync(Object event) {
        this.eventPublisher.publishAsync((DomainEvent) event);
    }

    /**
     * Compares this aggregate to the specified object. The comparison is based 
     * exclusively on the unique identifier (ID), adhering to the Entity concept in DDD.
     * @param o The object to compare with.
     * @return true if the IDs of both objects are equal, and they are of the same class.
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseAbstractAggregate that = (BaseAbstractAggregate) o;
        return Objects.equals(this.id, that.id);
    }

    /**
     * Returns a hash code value for the object, derived solely from the aggregate's ID.
     * @return A hash code based on the aggregate's DomainId.
     */
    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}
