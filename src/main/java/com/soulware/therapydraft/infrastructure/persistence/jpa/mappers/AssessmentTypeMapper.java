package com.soulware.therapydraft.infrastructure.persistence.jpa.mappers;

import com.soulware.therapydraft.domain.model.valueobjects.AssessmentType;
import com.soulware.therapydraft.infrastructure.persistence.jpa.entities.AssessmentTypeEntity;
import com.soulware.therapydraft.infrastructure.persistence.jpa.repositories.AssessmentTypeRepository;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;

public record AssessmentTypeMapper(AssessmentTypeRepository assessmentTypeRepository) {

    @Inject
    public AssessmentTypeMapper{}

    public AssessmentType toDomain(AssessmentTypeEntity entity){
        if(entity == null){
            return null;
        }

        return AssessmentType.valueOf(entity.getName());
    }

    public AssessmentTypeEntity toEntity(AssessmentType domain){
        if(domain == null){
            return null;
        }

        String typeName = domain.name();

        return assessmentTypeRepository.findByName(typeName)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Assessment Type " + typeName + " not found."
                ));
    }
}
