package com.soulware.therapydraft.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;

import java.time.ZonedDateTime;

@Entity
@Table(name = "weekly_schedule_layouts")
public class WeeklyScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "layout_configuration", nullable = false, columnDefinition = "TEXT")
    private String layoutConfiguration;

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

    protected WeeklyScheduleEntity() {}

    public WeeklyScheduleEntity(String layoutConfiguration) {
        this.layoutConfiguration = layoutConfiguration;
    }

    public Long getId() { return this.id; }
    public String getLayoutConfiguration() { return this.layoutConfiguration; }
}
