package com.soulware.therapydraft.application.commands;

import java.util.List;

public record CreateTherapyPlanCommand(
        Long assessmentId,
        String description,
        String goals,
        Long assignedTherapistId,
        Long legalResponsibleId,
        List<ScheduleEntryCommand> schedule
) {
}
