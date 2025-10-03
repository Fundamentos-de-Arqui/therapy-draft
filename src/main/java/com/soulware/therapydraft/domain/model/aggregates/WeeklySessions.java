package com.soulware.therapydraft.domain.model.aggregates;

import com.soulware.therapydraft.domain.model.entities.Session;
import com.soulware.therapydraft.domain.model.valueobjects.PlanWeek;
import com.soulware.therapydraft.domain.model.valueobjects.YearWeek;
import com.soulware.therapydraft.domain.model.valueobjects.ids.*;
import com.soulware.therapydraft.shared.domain.model.aggregates.BaseAbstractAggregate;
import com.soulware.therapydraft.shared.domain.model.events.DomainEventPublisher;

import java.util.Collections;
import java.util.List;

public class WeeklySessions extends BaseAbstractAggregate  {
    private final PlanWeek planWeek;
    private final YearWeek yearWeek;
    private final TherapyPlanId therapyPlanId;
    private final List<Session> sessions;
    private final LegalResponsibleId  legalResponsibleId;

    /**
     * Constructs a Weekly Schedule Aggregate Root.
     */
    public WeeklySessions(WeeklySessionsId id, DomainEventPublisher eventPublisher, PlanWeek planWeek, YearWeek yearWeek, TherapyPlanId therapyPlanId, List<Session> sessions, LegalResponsibleId legalResponsibleId) {
        super(id, eventPublisher);

        if (planWeek == null) {
            throw new IllegalArgumentException("Time slot cannot be null.");
        }
        if (yearWeek == null) {
            throw new IllegalArgumentException("Therapist ID cannot be null.");
        }
        if (therapyPlanId == null) {
            throw new IllegalArgumentException("Patient ID cannot be null.");
        }
        if (sessions == null) {
            throw new IllegalArgumentException("Sessions cannot be null.");
        }
        if (sessions.isEmpty()) {
            throw new IllegalArgumentException("Sessions list cannot be empty.");
        }
        if (legalResponsibleId == null) {
            throw new IllegalArgumentException("Legal responsible ID cannot be null.");
        }

        this.planWeek = planWeek;
        this.yearWeek = yearWeek;
        this.therapyPlanId = therapyPlanId;
        this.sessions = sessions;
        this.legalResponsibleId = legalResponsibleId;
    }

    // Getters
    public PlanWeek getPlanWeek() { return this.planWeek; }
    public YearWeek getYearWeek() { return this.yearWeek; }
    public TherapyPlanId getTherapyPlanId() { return this.therapyPlanId; }
    public LegalResponsibleId getLegalResponsible() { return this.legalResponsibleId; }

    /**
     * Provides an unmodifiable view of the internal sessions list.
     * This prevents external code from directly modifying the aggregate's state.
     * @return An unmodifiable List of sessions.
     */
    public List<Session> getSessions() {
        return Collections.unmodifiableList(this.sessions);
    }

    @Override
    public WeeklySessionsId getId() {
        return (WeeklySessionsId) super.getId();
    }
}
