package com.soulware.therapydraft.infrastructure.persistence.jpa.repositories;

import com.soulware.therapydraft.infrastructure.persistence.jpa.entities.AssessmentTypeEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.Optional;

@ApplicationScoped
public class JpaAssessmentTypeRepository implements AssessmentTypeRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<AssessmentTypeEntity> findByName(String name){
        if (name == null || name.isEmpty())
            return Optional.empty();

        return entityManager.createQuery(
                "SELECT s FROM AssessmentTypeEntity s WHERE s.name = :typeName", AssessmentTypeEntity.class)
                .setParameter("typeName", name)
                .getResultStream()
                .findFirst();
    }
}
