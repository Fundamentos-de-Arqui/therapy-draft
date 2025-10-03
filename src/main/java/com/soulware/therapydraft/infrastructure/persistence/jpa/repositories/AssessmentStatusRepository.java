package com.soulware.therapydraft.infrastructure.persistence.jpa.repositories;

import com.soulware.therapydraft.infrastructure.persistence.jpa.entities.AssessmentStatusEntity;

import java.util.Optional;

public interface AssessmentStatusRepository {
    Optional<AssessmentStatusEntity> findByName(String name);
}
