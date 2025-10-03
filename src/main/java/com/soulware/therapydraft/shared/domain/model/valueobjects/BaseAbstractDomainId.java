package com.soulware.therapydraft.shared.domain.model.valueobjects;

import java.util.Objects;

/**
 * Base abstract class for all custom Domain Identifiers within the system (both entities from inside the Bounded Context or entities from external contexts).
 * Ensures the identifier value is Long type.
 */
public abstract class BaseAbstractDomainId implements DomainId {
    private final Long value;

    protected BaseAbstractDomainId(Long value) {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("ID value must be positive.");
        }
        this.value = value;
    }

    @Override
    public final Long value() {
        return value;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BaseAbstractDomainId that = (BaseAbstractDomainId) o;
        return Objects.equals(this.value, that.value);
    }

    @Override
    public final int hashCode() {
        return Objects.hash(value);
    }
}
