package com.soulware.therapydraft.domain.repositories;

import com.soulware.therapydraft.domain.model.aggregates.TherapyPlan;
import com.soulware.therapydraft.domain.model.valueobjects.ids.TherapistId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.TherapyPlanId;

import java.util.List;
import java.util.Optional;

public interface TherapyPlanRepository {
    Optional<TherapyPlan> findById(TherapyPlanId id);
    void save(TherapyPlan therapyPlan);
    List<TherapyPlan> findByAssignedTherapistId(TherapistId therapistId);
}
