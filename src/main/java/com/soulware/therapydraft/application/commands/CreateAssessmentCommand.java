package com.soulware.therapydraft.application.commands;

import java.time.ZonedDateTime;

public record CreateAssessmentCommand(
        Long patientId,
        Long therapistId,
        ZonedDateTime scheduledAt
) {
}