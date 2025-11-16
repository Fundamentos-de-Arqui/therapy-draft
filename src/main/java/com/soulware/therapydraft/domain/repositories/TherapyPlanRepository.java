package com.soulware.therapydraft.domain.repositories;

import com.soulware.therapydraft.domain.model.aggregates.TherapyPlan;
import com.soulware.therapydraft.domain.model.valueobjects.ids.AssessmentId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.TherapistId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.TherapyPlanId;
import com.soulware.therapydraft.shared.infrastructure.PagedResult;

import java.util.List;
import java.util.Optional;

public interface TherapyPlanRepository {
    Optional<TherapyPlan> findById(TherapyPlanId id);
    void save(TherapyPlan therapyPlan);
    Optional<TherapyPlan> findByAssessmentId(AssessmentId assessmentId);
    PagedResult<TherapyPlan> findByFilters(
            Long assessmentId,
            Long therapistId,
            Long patientId,
            Long legalResponsibleId,
            int page,
            int size
    );
}
