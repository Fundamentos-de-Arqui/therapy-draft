package com.soulware.therapydraft.interfaces.rest.assemblers;

import com.soulware.therapydraft.domain.model.aggregates.Assessment;
import com.soulware.therapydraft.interfaces.rest.resources.AssessmentResource;

public class AssessmentResourceFromEntityAssembler {
    public static AssessmentResource toResourceFromEntity(Assessment entity){
        return new AssessmentResource(
                entity.getId().value(),
                entity.getPatientId().value(),
                entity.getTherapistId().value(),
                entity.getType().name(),
                entity.getStatus().name(),
                entity.getScheduledTo()
        );
    }
}
