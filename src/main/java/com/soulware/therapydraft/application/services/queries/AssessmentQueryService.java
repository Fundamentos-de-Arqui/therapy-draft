package com.soulware.therapydraft.application.services.queries;

import com.soulware.therapydraft.application.queries.GetAssessmentsQuery;
import com.soulware.therapydraft.domain.model.aggregates.Assessment;
import com.soulware.therapydraft.domain.repositories.AssessmentRepository;
import com.soulware.therapydraft.shared.infrastructure.PagedResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class AssessmentQueryService {

    @Inject
    AssessmentRepository assessmentRepository;

    public PagedResult<Assessment> getAssessments(GetAssessmentsQuery query) {
        return assessmentRepository.findByFilters(
                query.patientId(),
                query.therapistId(),
                query.status(),
                query.scheduledAt(),
                query.page(),
                query.size()
        );
    }

}
