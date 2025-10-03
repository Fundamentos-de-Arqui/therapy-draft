package com.soulware.therapydraft.domain.model.valueobjects.ids;

import com.soulware.therapydraft.shared.domain.model.valueobjects.DomainId;

import java.util.Objects;

/**
 * Value Object representing the unique identity of a WeeklySessionsRepository Aggregate Root.
 */
public record WeeklySessionsId(Long value) implements DomainId {
    public WeeklySessionsId {
        if (value == null || value <= 0) {
            throw new IllegalArgumentException("WeeklySessionsRepository ID value must be positive.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WeeklySessionsId that = (WeeklySessionsId) o;
        return Objects.equals(value, that.value);
    }

}

