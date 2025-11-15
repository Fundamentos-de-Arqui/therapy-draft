package com.soulware.therapydraft.interfaces.rest.resources;

import java.time.ZonedDateTime;

public record CreateAssessmentResource(
        Long patientId,
        Long therapistId,
        ZonedDateTime scheduledAt
) {
}
