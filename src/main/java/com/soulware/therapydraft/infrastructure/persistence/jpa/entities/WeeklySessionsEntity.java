package com.soulware.therapydraft.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "weekly_sessions")
public class WeeklySessionsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plan_week_number", nullable = false)
    private int planWeekNumber;

    @Column(name = "year_week", nullable = false)
    private String yearWeek;

    @Column(name = "therapy_plan_id", nullable = false)
    private Long therapyPlanId;

    @Column(name = "legal_responsible_id", nullable = false)
    private Long legalResponsibleId;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "weekly_schedule_id")
    private List<SessionEntity> sessions;

    // Audit
    @Column(name = "created_at", updatable = false, nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private ZonedDateTime modifiedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = ZonedDateTime.now();
        this.modifiedAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedAt = ZonedDateTime.now();
    }

    protected WeeklySessionsEntity() {}

    public WeeklySessionsEntity(int planWeekNumber, String yearWeek,  Long therapyPlanId, Long legalResponsibleId) {
        this.planWeekNumber = planWeekNumber;
        this.yearWeek = yearWeek;
        this.therapyPlanId = therapyPlanId;
        this.legalResponsibleId = legalResponsibleId;
    }

    public Long getId() { return this.id; }
    public int getPlanWeekNumber() { return this.planWeekNumber; }
    public String getYearWeek() { return this.yearWeek; }
    public Long getTherapyPlanId() { return this.therapyPlanId; }
    public Long getLegalResponsibleId() { return this.legalResponsibleId; }
    public List<SessionEntity> getSessions() { return this.sessions; }
    public void setSessions(List<SessionEntity> sessions) { this.sessions = sessions; }
}
