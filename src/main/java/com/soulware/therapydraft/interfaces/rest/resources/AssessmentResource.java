package com.soulware.therapydraft.interfaces.rest.resources;

import java.time.ZonedDateTime;

public record AssessmentResource(
        Long id,
        Long patientId,
        Long therapistId,
        String type,
        String status,
        ZonedDateTime scheduledTo
) {
}
