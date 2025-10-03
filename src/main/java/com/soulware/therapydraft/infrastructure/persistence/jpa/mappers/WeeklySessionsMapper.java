package com.soulware.therapydraft.infrastructure.persistence.jpa.mappers;

import com.soulware.therapydraft.domain.model.aggregates.WeeklySessions;
import com.soulware.therapydraft.domain.model.valueobjects.PlanWeek;
import com.soulware.therapydraft.domain.model.valueobjects.YearWeek;
import com.soulware.therapydraft.domain.model.valueobjects.ids.LegalResponsibleId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.TherapyPlanId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.WeeklySessionsId;
import com.soulware.therapydraft.infrastructure.persistence.jpa.entities.WeeklySessionsEntity;
import com.soulware.therapydraft.shared.domain.model.events.DomainEventPublisher;
import jakarta.inject.Inject;

import java.util.stream.Collectors;

public record WeeklySessionsMapper(    SessionMapper sessionMapper,
                                      DomainEventPublisher domainEventPublisher) {

    @Inject
    public WeeklySessionsMapper{}

    public WeeklySessionsEntity toEntity(WeeklySessions domain) {
        if (domain == null){
            return null;
        }

        WeeklySessionsEntity entity = new WeeklySessionsEntity(
                domain.getPlanWeek().getValue(),
                domain.getYearWeek().toString(),
                domain.getTherapyPlanId().value(),
                domain.getLegalResponsible().value()
        );

        if (domain.getSessions() != null){
            entity.setSessions(
                    domain.getSessions().stream()
                            .map(sessionMapper::toEntity)
                            .collect(Collectors.toList())
            );
        }

        return entity;
    }

    public WeeklySessions toDomain(WeeklySessionsEntity entity) {
        if (entity == null){
            return null;
        }

        WeeklySessionsId id = new WeeklySessionsId(entity.getId());
        PlanWeek planWeek = new PlanWeek(entity.getPlanWeekNumber());
        YearWeek yearWeek = YearWeek.fromFormattedString(entity.getYearWeek());
        TherapyPlanId therapyPlanId = new TherapyPlanId(entity.getTherapyPlanId());
        LegalResponsibleId legalResponsibleId = new LegalResponsibleId(entity.getLegalResponsibleId());

        var sessions = entity.getSessions().stream()
                .map(sessionMapper::toDomain)
                .toList();

        return new WeeklySessions(
                id,
                this.domainEventPublisher,
                planWeek,
                yearWeek,
                therapyPlanId,
                sessions,
                legalResponsibleId
        );
    }
}
