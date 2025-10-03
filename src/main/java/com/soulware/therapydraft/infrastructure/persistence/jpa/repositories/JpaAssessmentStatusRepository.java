package com.soulware.therapydraft.infrastructure.persistence.jpa.repositories;

import com.soulware.therapydraft.infrastructure.persistence.jpa.entities.AssessmentStatusEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class JpaAssessmentStatusRepository implements AssessmentStatusRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<AssessmentStatusEntity> findByName(String name) {
        if (name == null || name.isEmpty()) return Optional.empty();

        return entityManager.createQuery(
                        "SELECT s FROM AssessmentStatusEntity s WHERE s.name = :statusName",
                        AssessmentStatusEntity.class)
                .setParameter("statusName", name)
                .getResultStream()
                .findFirst();
    }
}