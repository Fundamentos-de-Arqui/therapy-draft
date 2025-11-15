package com.soulware.therapydraft.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "therapy_plans")
public class TherapyPlanEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "assessment_id", nullable = false)
    private Long assessmentId;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "goals", nullable = false)
    private String goals;

    @Column(name = "assigned_therapist_id", nullable = false)
    private Long assignedTherapistId;

    @Column(name = "patient_id", nullable = false, updatable = false)
    private Long patientId;

    @Column(name = "legal_responsible_id", nullable = false)
    private Long legalResponsibleId;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "therapy_plan_id")
    private List<TherapyScheduleEntryEntity> scheduleEntries;

    @Column(name = "created_at", updatable = false, nullable = false)
    private ZonedDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private ZonedDateTime modifiedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = ZonedDateTime.now();
        this.modifiedAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void onUpdate(){
        this.modifiedAt = ZonedDateTime.now();
    }

    protected TherapyPlanEntity() {}

    public TherapyPlanEntity(String description, String goals, Long assignedTherapistId, Long patientId, Long legalResponsibleId) {
        this.description = description;
        this.goals = goals;
        this.assignedTherapistId = assignedTherapistId;
        this.patientId = patientId;
        this.legalResponsibleId = legalResponsibleId;
    }

    public Long getId() { return this.id; }
    public Long getAssessmentId() { return this.assessmentId; }
    public String getDescription() { return this.description; }
    public String getGoals() { return this.goals; }
    public Long getAssignedTherapistId() { return this.assignedTherapistId; }
    public Long getPatientId() { return this.patientId; }
    public Long getLegalResponsibleId() { return this.legalResponsibleId; }
    public List<TherapyScheduleEntryEntity> getWeeklySchedule() { return scheduleEntries; }

}
