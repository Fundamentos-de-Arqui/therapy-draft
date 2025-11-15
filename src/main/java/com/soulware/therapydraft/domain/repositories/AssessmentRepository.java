package com.soulware.therapydraft.domain.repositories;

import com.soulware.therapydraft.domain.model.aggregates.Assessment;
import com.soulware.therapydraft.domain.model.valueobjects.ids.AssessmentId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.PatientId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.TherapistId;
import com.soulware.therapydraft.shared.infrastructure.PagedResult;

import java.util.List;
import java.util.Optional;

public interface AssessmentRepository {
    Optional<Assessment> findById(AssessmentId id);
    void save(Assessment assessment);
    void update(Assessment assessment);
    List<Assessment> findByPatientId(PatientId patientId);
    List<Assessment> findByTherapistId(TherapistId therapistId);
    PagedResult<Assessment> findByFilters(
            Long patientId,
            Long therapistId,
            String status,
            String scheduledAt,
            int page,
            int size
    );
}
