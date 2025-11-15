package com.soulware.therapydraft.infrastructure.persistence.jpa.entities;

import com.soulware.therapydraft.domain.model.valueobjects.AssessmentStatus;
import com.soulware.therapydraft.domain.model.valueobjects.AssessmentType;
import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "assessments")
public class AssessmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "therapist_id", nullable = false)
    private Long therapistId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "type_id", nullable = false)
    private AssessmentTypeEntity type;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "status_id", nullable = false)
    private AssessmentStatusEntity status;

    @Column(name = "scheduled_at", updatable = false, nullable = false)
    private ZonedDateTime scheduledAt;

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

    protected AssessmentEntity() {}

    public AssessmentEntity(Long patientId, Long therapistId, AssessmentTypeEntity type, AssessmentStatusEntity status,  ZonedDateTime scheduledAt) {
        this.patientId = patientId;
        this.therapistId = therapistId;
        this.type = type;
        this.status = status;
        this.scheduledAt = scheduledAt;
    }

    public Long getId() { return this.id; }
    public Long getPatientId() {  return this.patientId; }
    public Long getTherapistId() { return this.therapistId; }
    public AssessmentTypeEntity getType() { return this.type; }
    public AssessmentStatusEntity getStatus() { return this.status; }
    public ZonedDateTime getScheduledAt() { return this.scheduledAt; }
    public void setId(Long id) { this.id = id; }
    public void setTherapistId(Long id) { this.therapistId = id; }
    public void setStatus(AssessmentStatusEntity status) { this.status = status; }
}
