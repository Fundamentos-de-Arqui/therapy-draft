package com.soulware.therapydraft.interfaces.rest.resources;

import java.util.List;

public record TherapyPlanResource(
        Long id,
        Long assessmentId,
        String description,
        String goals,
        Long assignedTherapistId,
        Long patientId,
        Long legalResponsibleId,
        List<ScheduleEntryResource> schedule
) {
}
