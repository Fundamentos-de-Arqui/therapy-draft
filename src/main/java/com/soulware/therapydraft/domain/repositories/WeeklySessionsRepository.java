package com.soulware.therapydraft.domain.repositories;

import com.soulware.therapydraft.domain.model.aggregates.WeeklySessions;
import com.soulware.therapydraft.domain.model.valueobjects.ids.*;

import java.util.List;
import java.util.Optional;

public interface WeeklySessionsRepository {
    Optional<WeeklySessions> findById(WeeklySessionsId id);
    void save(WeeklySessions weeklySessions);

    List<WeeklySessions> findByTherapyPlanId(TherapyPlanId therapyPlanId);
    List<WeeklySessions> findByLegalResponsibleId(LegalResponsibleId legalResponsibleId);
}
