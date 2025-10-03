package com.soulware.therapydraft.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "sessions")
public class SessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_time",  nullable = false)
    private ZonedDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private ZonedDateTime endTime;

    @Column(name = "therapist_id", nullable = false)
    private Long therapistId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "modified_at", nullable = false)
    private ZonedDateTime modifiedAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = ZonedDateTime.now();
        this.modifiedAt = ZonedDateTime.now();
    }

    @PreUpdate
    protected void  onUpdate(){
        this.modifiedAt = ZonedDateTime.now();
    }

    protected SessionEntity() {}

    public SessionEntity(ZonedDateTime startTime, ZonedDateTime endTime, Long therapistId) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.therapistId = therapistId;
    }

    public Long getId() { return this.id; }
    public ZonedDateTime getStartTime() { return this.startTime; }
    public ZonedDateTime getEndTime() { return this.endTime; }
    public Long getTherapistId() { return this.therapistId; }
}
