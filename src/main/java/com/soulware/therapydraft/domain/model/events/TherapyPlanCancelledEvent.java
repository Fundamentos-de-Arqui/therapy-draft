package com.soulware.therapydraft.domain.model.events;

import com.soulware.therapydraft.domain.model.valueobjects.TherapyPlanCancelledType;
import com.soulware.therapydraft.domain.model.valueobjects.ids.PatientId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.TherapyPlanId;
import com.soulware.therapydraft.shared.domain.model.events.BaseAbstractDomainEvent;

public class TherapyPlanCancelledEvent extends BaseAbstractDomainEvent {
    private final TherapyPlanId therapyPlanId;
    private final PatientId patientId;
    private final TherapyPlanCancelledType reason;

    public TherapyPlanCancelledEvent(
            TherapyPlanId therapyPlanId,
            PatientId patientId,
            TherapyPlanCancelledType reason
    ){
        this.therapyPlanId = therapyPlanId;
        this.patientId = patientId;
        this.reason = reason;
    }

    public TherapyPlanId getTherapyPlanId() { return  this.therapyPlanId; }
    public PatientId getPatientId() { return  this.patientId; }
    public TherapyPlanCancelledType getReason() { return this.reason; }
}
