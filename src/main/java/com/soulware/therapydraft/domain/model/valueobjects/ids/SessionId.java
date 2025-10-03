package com.soulware.therapydraft.domain.model.valueobjects.ids;

import com.soulware.therapydraft.shared.domain.model.valueobjects.DomainId;

import java.util.Objects;

/**
 * Value Object representing the unique identity of a Session Aggregate Root.
 */
public record SessionId(Long value) implements DomainId {
    public SessionId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("WeeklySessionsRepository ID value must be positive.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionId that = (SessionId) o;
        return Objects.equals(value, that.value);
    }

}
