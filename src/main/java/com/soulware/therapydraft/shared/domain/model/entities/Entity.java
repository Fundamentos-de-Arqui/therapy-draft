package com.soulware.therapydraft.shared.domain.model.entities;

import com.soulware.therapydraft.shared.domain.model.valueobjects.DomainId;

/**
 * Contract for all Domain Entities.
 */
public interface Entity {
    /**
     * Gets the identifier for the entity, used for identity comparison.
     * @return The unique identifier value.
     */
    DomainId getId();
}
