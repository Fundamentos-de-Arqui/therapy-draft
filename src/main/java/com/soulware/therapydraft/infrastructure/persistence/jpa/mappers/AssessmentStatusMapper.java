package com.soulware.therapydraft.infrastructure.persistence.jpa.mappers;

import com.soulware.therapydraft.domain.model.valueobjects.AssessmentStatus;
import com.soulware.therapydraft.infrastructure.persistence.jpa.entities.AssessmentStatusEntity;
import com.soulware.therapydraft.infrastructure.persistence.jpa.repositories.AssessmentStatusRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityNotFoundException;

@ApplicationScoped
public class AssessmentStatusMapper{
    @Inject
    AssessmentStatusRepository assessmentStatusRepository;

    @Inject
    public AssessmentStatusMapper() {}

    public AssessmentStatus toDomain(AssessmentStatusEntity entity){
        if(entity == null){
            return null;
        }

        return AssessmentStatus.valueOf(entity.getName());
    }

    public AssessmentStatusEntity toEntity(AssessmentStatus domain){
        if(domain == null){
            return null;
        }

        String statusName = domain.name();

        return assessmentStatusRepository.findByName(statusName)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Assessment Status with name " + statusName + " not found."
                ));
    }
}
