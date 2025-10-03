package com.soulware.therapydraft.domain.model.aggregates;

import com.soulware.therapydraft.domain.model.events.TherapyPlanCancelledEvent;
import com.soulware.therapydraft.domain.model.events.TherapyPlanCreatedEvent;
import com.soulware.therapydraft.domain.model.valueobjects.AssessmentStatus;
import com.soulware.therapydraft.domain.model.valueobjects.AssessmentType;
import com.soulware.therapydraft.domain.model.valueobjects.TherapyPlanCancelledType;
import com.soulware.therapydraft.domain.model.valueobjects.TimeSlot;
import com.soulware.therapydraft.domain.model.valueobjects.ids.*;
import com.soulware.therapydraft.shared.domain.model.aggregates.BaseAbstractAggregate;
import com.soulware.therapydraft.shared.domain.model.events.DomainEventPublisher;

import java.time.ZonedDateTime;
import java.util.List;

public class Assessment extends BaseAbstractAggregate {
    private PatientId patientId;
    private TherapistId therapistId;
    private AssessmentType type;
    private AssessmentStatus status;
    private ZonedDateTime scheduledAt;

    public Assessment(
            AssessmentId id,
            DomainEventPublisher eventPublisher,
            PatientId patientId,
            TherapistId therapistId,
            AssessmentType type,
            AssessmentStatus status,
            ZonedDateTime scheduledAt
    ) {
        super(id, eventPublisher);
        this.patientId = patientId;
        this.therapistId = therapistId;
        this.type = type;
        this.status = status;
        this.scheduledAt = scheduledAt;
    }

    public static Assessment createInitial(
            AssessmentId id,
            DomainEventPublisher eventPublisher,
            PatientId patientId,
            TherapistId therapistId,
            AssessmentStatus status,
            ZonedDateTime scheduledAt,
            TherapyPlanId therapyPlanId,
            LegalResponsibleId legalResponsibleId,
            TimeSlot duration,
            List<WeeklySessionsId> weeklySessionsIds
    ) {
        Assessment assessment = new Assessment(id, eventPublisher, patientId, therapistId,
                AssessmentType.INITIAL, status, scheduledAt);

        assessment.publishEvent(new TherapyPlanCreatedEvent(
                therapyPlanId,
                patientId,
                legalResponsibleId,
                id,
                duration,
                weeklySessionsIds
        ));

        return assessment;
    }

    public static Assessment createReassessment(
            AssessmentId id,
            DomainEventPublisher eventPublisher,
            PatientId patientId,
            TherapistId therapistId,
            AssessmentStatus status,
            ZonedDateTime scheduledAt,
            TherapyPlanId existingPlanId,
            TherapyPlanCancelledType reason
    ) {
        Assessment assessment = new Assessment(id, eventPublisher, patientId, therapistId,
                AssessmentType.REASSESSMENT, status, scheduledAt);

        assessment.publishEvent(new TherapyPlanCancelledEvent(
                existingPlanId,
                patientId,
                reason
        ));

        return assessment;
    }

    public void changeStatus(AssessmentStatus newStatus) {
        if (!this.status.canTransitionTo(newStatus)) {
            throw new IllegalStateException(
                    String.format("Cannot change assessment from %s to %s", this.status, newStatus)
            );
        }
        this.status = newStatus;
    }

    // Helpers
    private boolean isCanceled() { return this.status == AssessmentStatus.CANCELED; }
    private boolean isDone() { return this.status == AssessmentStatus.DONE; }
    private boolean isMissed() { return this.status == AssessmentStatus.MISSED; }

    public PatientId getPatientId() { return this.patientId; }
    public TherapistId getTherapistId() { return this.therapistId; }
    public AssessmentType getType() { return this.type; }
    public AssessmentStatus getStatus() { return this.status; }
    public ZonedDateTime getScheduledAt() { return this.scheduledAt; }
}
