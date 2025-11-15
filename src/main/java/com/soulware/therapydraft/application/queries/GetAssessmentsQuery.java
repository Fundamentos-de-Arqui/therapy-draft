package com.soulware.therapydraft.application.queries;

import jakarta.validation.constraints.Null;

import java.time.ZonedDateTime;

public record GetAssessmentsQuery(
        @Null Long patientId,
        @Null Long therapistId,
        @Null String status,
        @Null String scheduledAt,
        int page,
        int size
) {
}
