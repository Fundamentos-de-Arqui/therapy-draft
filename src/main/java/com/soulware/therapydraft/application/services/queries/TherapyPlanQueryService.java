package com.soulware.therapydraft.application.services.queries;

import com.soulware.therapydraft.application.queries.GetTherapyPlansQuery;
import com.soulware.therapydraft.domain.model.aggregates.TherapyPlan;
import com.soulware.therapydraft.domain.repositories.TherapyPlanRepository;
import com.soulware.therapydraft.shared.infrastructure.PagedResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class TherapyPlanQueryService {

    @Inject
    TherapyPlanRepository therapyPlanRepository;

    public PagedResult<TherapyPlan> getTherapyPlans(GetTherapyPlansQuery query) {
        return therapyPlanRepository.findByFilters(
                query.assessmentId(),
                query.therapistId(),
                query.patientId(),
                query.legalResponsibleId(),
                query.page(),
                query.size()
        );
    }
}
