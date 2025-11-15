package com.soulware.therapydraft.application.services;

import com.soulware.therapydraft.application.queries.GetAssessmentsQuery;
import com.soulware.therapydraft.domain.model.aggregates.Assessment;
import com.soulware.therapydraft.domain.repositories.AssessmentRepository;
import com.soulware.therapydraft.infrastructure.events.cdi.CdiEventPublisher;
import com.soulware.therapydraft.shared.infrastructure.PagedResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

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
