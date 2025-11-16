package com.soulware.therapydraft.interfaces.rest.resources;

import com.soulware.therapydraft.application.commands.ScheduleEntryCommand;

import java.util.List;

public record CreateTherapyPlanResource(
        Long assessmentId,
        String description,
        String goals,
        Long assignedTherapistId,
        Long patientId,
        Long legalResponsibleId,
        List<ScheduleEntryCommand>  schedule
) {
}
