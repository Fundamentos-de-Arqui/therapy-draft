package com.soulware.therapydraft.domain.model.aggregates;

import com.soulware.therapydraft.domain.model.valueobjects.TherapyPlanInformation;
import com.soulware.therapydraft.domain.model.valueobjects.WeeklySchedule;
import com.soulware.therapydraft.domain.model.valueobjects.ids.*;
import com.soulware.therapydraft.shared.domain.model.aggregates.BaseAbstractAggregate;
import com.soulware.therapydraft.shared.domain.model.events.DomainEventPublisher;

public class TherapyPlan extends BaseAbstractAggregate  {
    private AssessmentId  assessmentId;
    private TherapyPlanInformation therapyPlanInformation;
    private TherapistId assignedTherapistId;
    private PatientId patientId;
    private LegalResponsibleId legalResponsibleId;
    private WeeklySchedule schedule;

    /**
     * Creates a therapy plan aggregate.
     */
    public TherapyPlan(TherapyPlanId id, DomainEventPublisher eventPublisher, AssessmentId assessmentId, TherapyPlanInformation information, TherapistId assignedTherapistId, PatientId patientId, LegalResponsibleId legalResponsibleId, WeeklySchedule schedule) {
        super(id, eventPublisher);

        if (assignedTherapistId == null) {
            throw new IllegalArgumentException("Assigned therapist cannot be null.");
        }
        if (patientId == null) {
            throw new IllegalArgumentException("Patient cannot be null.");
        }
        if (legalResponsibleId == null) {
            throw new IllegalArgumentException("Legal responsible cannot be null.");
        }
        if (schedule == null) {
            throw new IllegalArgumentException("Weekly layout cannot be null.");
        }

        this.assessmentId = assessmentId;
        this.therapyPlanInformation = information;
        this.assignedTherapistId = assignedTherapistId;
        this.patientId = patientId;
        this.legalResponsibleId = legalResponsibleId;
        this.schedule = schedule;
    }

    @Override
    public TherapyPlanId getId() {
        return (TherapyPlanId) super.getId();
    }
    public AssessmentId getAssessmentId() { return this.assessmentId; }
    public TherapyPlanInformation getTherapyPlanInformation() { return this.therapyPlanInformation; }
    public TherapistId getAssignedTherapistId() { return this.assignedTherapistId; }
    public PatientId getPatientId() { return this.patientId; }
    public LegalResponsibleId getLegalResponsibleId() { return this.legalResponsibleId; }
    public WeeklySchedule getSchedule() { return this.schedule; }
}
