package com.soulware.therapydraft.application.queries;

import jakarta.validation.constraints.Null;

public record GetTherapyPlansQuery(
        @Null Long assessmentId,
        @Null Long therapistId,
        @Null Long patientId,
        @Null Long legalResponsibleId,
        int page,
        int size
) {
}
