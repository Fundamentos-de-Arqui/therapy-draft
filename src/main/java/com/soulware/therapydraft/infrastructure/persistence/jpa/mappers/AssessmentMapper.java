package com.soulware.therapydraft.infrastructure.persistence.jpa.mappers;

import com.soulware.therapydraft.domain.model.aggregates.Assessment;
import com.soulware.therapydraft.domain.model.valueobjects.AssessmentStatus;
import com.soulware.therapydraft.domain.model.valueobjects.AssessmentType;
import com.soulware.therapydraft.domain.model.valueobjects.ids.AssessmentId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.PatientId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.TherapistId;
import com.soulware.therapydraft.infrastructure.persistence.jpa.entities.AssessmentEntity;
import com.soulware.therapydraft.shared.domain.model.events.DomainEventPublisher;
import jakarta.inject.Inject;

import java.time.ZonedDateTime;

public record AssessmentMapper (
        AssessmentStatusMapper assessmentStatusMapper,
        AssessmentTypeMapper assessmentTypeMapper,
        DomainEventPublisher domainEventPublisher
){

    @Inject
    public AssessmentMapper{}

    public AssessmentEntity toEntity(Assessment domain){
        if(domain == null){
            return null;
        }

        return new AssessmentEntity(
                domain.getPatientId().value(),
                domain.getTherapistId().value(),
                assessmentTypeMapper.toEntity(domain.getType()),
                assessmentStatusMapper.toEntity(domain.getStatus()),
                domain.getScheduledAt()
        );
    }

    public Assessment toDomain(AssessmentEntity entity){
        if (entity == null){
            return null;
        }

        AssessmentId id = new  AssessmentId(entity.getId());
        PatientId patientId = new PatientId(entity.getPatientId());
        TherapistId therapistId = new TherapistId(entity.getId());
        AssessmentType type = assessmentTypeMapper.toDomain(entity.getType());
        AssessmentStatus status = assessmentStatusMapper.toDomain(entity.getStatus());
        ZonedDateTime scheduledAt = entity.getScheduledAt();

        return new Assessment(
                id,
                this.domainEventPublisher,
                patientId,
                therapistId,
                type,
                status,
                scheduledAt
        );
    }
}
