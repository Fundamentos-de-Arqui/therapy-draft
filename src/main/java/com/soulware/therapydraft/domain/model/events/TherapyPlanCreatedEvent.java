package com.soulware.therapydraft.domain.model.events;

import com.soulware.therapydraft.domain.model.valueobjects.TimeSlot;
import com.soulware.therapydraft.domain.model.valueobjects.ids.*;
import com.soulware.therapydraft.shared.domain.model.events.BaseAbstractDomainEvent;

import java.util.List;

public class TherapyPlanCreatedEvent extends BaseAbstractDomainEvent {

    private final TherapyPlanId  therapyPlanId;
    private final PatientId  patientId;
    private final LegalResponsibleId legalResponsibleId;
    private final AssessmentId assessmentId;
    private final TimeSlot duration;
    private final List<WeeklySessionsId> weeklySessionsIds;

    public TherapyPlanCreatedEvent(
            TherapyPlanId therapyPlanId,
            PatientId patientId,
            LegalResponsibleId legalResponsibleId,
            AssessmentId assessmentId,
            TimeSlot timeSlot,
            List<WeeklySessionsId> weeklySessionsIds
    ){
        this.therapyPlanId = therapyPlanId;
        this.patientId = patientId;
        this.legalResponsibleId = legalResponsibleId;
        this.assessmentId = assessmentId;
        this.duration = timeSlot;
        this.weeklySessionsIds = weeklySessionsIds;
    }

    public TherapyPlanId getTherapyPlanId() { return this.therapyPlanId; }
    public PatientId getPatientId() { return this.patientId; }
    public LegalResponsibleId getLegalResponsibleId() { return this.legalResponsibleId; }
    public AssessmentId getAssessmentId() { return this.assessmentId; }
    public TimeSlot getDuration() { return this.duration; }
    public List<WeeklySessionsId> getWeeklySessionsIds() { return this.weeklySessionsIds; }
}
