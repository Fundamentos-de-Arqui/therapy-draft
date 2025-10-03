package com.soulware.therapydraft.domain.model.valueobjects.ids;

import com.soulware.therapydraft.shared.domain.model.valueobjects.DomainId;

public record AssessmentId(Long value) implements DomainId {
    public AssessmentId{
        if  (value == null || value <= 0)
            throw new IllegalArgumentException("Assessment ID value must be positive");
    }

    @Override
    public boolean equals(Object o){
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AssessmentId that = (AssessmentId) o;
        return value.equals(that.value);
    }
}
