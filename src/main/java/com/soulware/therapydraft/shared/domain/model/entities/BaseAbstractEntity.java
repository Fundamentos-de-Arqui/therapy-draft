package com.soulware.therapydraft.shared.domain.model.entities;

import com.soulware.therapydraft.shared.domain.model.valueobjects.DomainId;

import java.util.Objects;

/**
 * Base abstract class for all Entities and Aggregate Roots in the Domain Bounded Context.
 * Ensures that every Entity possesses a unique {@link DomainId} and enforces
 * the DDD rule of identity, where two entities are considered equal only if their IDs match.
 * The equality check is final, preventing modification by inheriting classes.
 */
public abstract class BaseAbstractEntity implements Entity {

    private final DomainId id;

    /**
     * Constructs the base entity, initializing its unique identifier.
     */
    protected BaseAbstractEntity(DomainId id) {
        if (id == null) {
            throw new IllegalArgumentException("Entity ID cannot be null.");
        }

        this.id = id;
    }

    /**
     * {@inheritDoc}
     * @ The immutable {@link DomainId} of this entity.
     */
    @Override
    public DomainId getId() {
        return id;
    }

    /**
     * Compares this entity to the specified object. The comparison is based 
     * exclusively on the unique identifier (ID), adhering to the Entity concept in DDD.
     * @param o The object to compare with.
     * @return true if the IDs of both objects are equal, and they are of the same class.
     */
    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseAbstractEntity that = (BaseAbstractEntity) o;
        return Objects.equals(this.id, that.id);
    }

    /**
     * Returns a hash code value for the object, derived solely from the entity's ID.
     * @ A hash code based on the entity's {@link DomainId}.
     */
    @Override
    public final int hashCode() {
        return Objects.hash(id);
    }
}
