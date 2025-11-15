package com.soulware.therapydraft.infrastructure.persistence.jpa.mappers;

import com.soulware.therapydraft.domain.model.aggregates.TherapyPlan;
import com.soulware.therapydraft.domain.model.valueobjects.TherapyPlanInformation;
import com.soulware.therapydraft.domain.model.valueobjects.WeeklySchedule;
import com.soulware.therapydraft.domain.model.valueobjects.ids.*;
import com.soulware.therapydraft.infrastructure.persistence.jpa.entities.TherapyPlanEntity;
import com.soulware.therapydraft.shared.domain.model.events.DomainEventPublisher;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TherapyPlanMapper {
    @Inject
    WeeklyScheduleMapper weeklyScheduleMapper;

    @Inject
    DomainEventPublisher domainEventPublisher;

    @Inject
    public TherapyPlanMapper() {}

    public TherapyPlanEntity toEntity(TherapyPlan domain){
        if (domain == null){
            return null;
        }

        return new TherapyPlanEntity(
                domain.getTherapyPlanInformation().Description(),
                domain.getTherapyPlanInformation().Goals(),
                domain.getAssignedTherapistId().value(),
                domain.getPatientId().value(),
                domain.getLegalResponsibleId().value()
        );
    }

    public TherapyPlan toDomain(TherapyPlanEntity entity){
        if (entity == null){
            return null;
        }

        TherapyPlanId id =  new TherapyPlanId(entity.getId());
        AssessmentId assessmentId = new AssessmentId(entity.getAssessmentId());
        TherapyPlanInformation information = new TherapyPlanInformation(entity.getDescription(), entity.getGoals());
        TherapistId assignedTherapistId = new TherapistId(entity.getAssignedTherapistId());
        PatientId patientId = new PatientId(entity.getPatientId());
        LegalResponsibleId legalResponsibleId = new LegalResponsibleId(entity.getLegalResponsibleId());

        WeeklySchedule weeklySchedule = weeklyScheduleMapper.toDomain(entity.getWeeklySchedule());

        return new TherapyPlan(
                id,
                this.domainEventPublisher,
                assessmentId,
                information,
                assignedTherapistId,
                patientId,
                legalResponsibleId,
                weeklySchedule
        );
    }
}
