package com.soulware.therapydraft.interfaces.rest.resources;

import java.time.ZonedDateTime;

public record AssessmentResource(
        Long patientId,
        Long therapistId,
        String type,
        String status,
        ZonedDateTime scheduledAt
) {
}
