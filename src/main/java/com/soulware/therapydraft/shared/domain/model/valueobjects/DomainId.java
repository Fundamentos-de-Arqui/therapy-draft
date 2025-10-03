package com.soulware.therapydraft.shared.domain.model.valueobjects;

/**
 * Contract for all Domain Identity Value Objects.
 * Ensures the existence of a value field corresponding to the database primary key.
 */
public interface DomainId {

    /**
     * Gets the underlying identifier value, used identity comparison.
     * @return The unique identifier value.
     */
    Long value();
}
