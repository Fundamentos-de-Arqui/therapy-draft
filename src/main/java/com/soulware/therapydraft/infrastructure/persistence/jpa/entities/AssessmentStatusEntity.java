package com.soulware.therapydraft.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "assessment_statuses")
public class AssessmentStatusEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 20, unique = true)
    private String name;

    // Required by JPA
    protected AssessmentStatusEntity() {}

    public AssessmentStatusEntity(String name) {
        this.name = name;
    }

    public Long getId() { return this.id; }
    public String getName() { return this.name; }
}
