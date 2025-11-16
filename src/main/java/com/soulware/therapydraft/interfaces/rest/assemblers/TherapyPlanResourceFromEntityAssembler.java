package com.soulware.therapydraft.interfaces.rest.assemblers;

import com.soulware.therapydraft.domain.model.aggregates.TherapyPlan;
import com.soulware.therapydraft.interfaces.rest.resources.ScheduleEntryResource;
import com.soulware.therapydraft.interfaces.rest.resources.TherapyPlanResource;

import java.util.List;

public class TherapyPlanResourceFromEntityAssembler {
    public static TherapyPlanResource toResourceFromEntity(TherapyPlan entity) {
        List<ScheduleEntryResource> scheduleResources =
                ScheduleEntryResourceFromEntityAssembler.toResourceList(entity.getSchedule());
        return new TherapyPlanResource(
                entity.getId().value(),
                entity.getAssessmentId().value(),
                entity.getTherapyPlanInformation().Description(),
                entity.getTherapyPlanInformation().Goals(),
                entity.getAssignedTherapistId().value(),
                entity.getPatientId().value(),
                entity.getLegalResponsibleId().value(),
                scheduleResources
        );
    }
}
