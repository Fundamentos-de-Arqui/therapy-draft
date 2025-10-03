package com.soulware.therapydraft.infrastructure.persistence.jpa.repositories;

import com.soulware.therapydraft.infrastructure.persistence.jpa.entities.AssessmentTypeEntity;

import java.util.Optional;

public interface AssessmentTypeRepository {
    Optional<AssessmentTypeEntity> findByName(String name);
}
