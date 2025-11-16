package com.soulware.therapydraft.infrastructure.persistence.jpa.repositories;

import com.soulware.therapydraft.domain.model.aggregates.TherapyPlan;
import com.soulware.therapydraft.domain.model.valueobjects.ids.PatientId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.TherapistId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.TherapyPlanId;
import com.soulware.therapydraft.domain.repositories.TherapyPlanRepository;
import com.soulware.therapydraft.infrastructure.persistence.jpa.entities.TherapyPlanEntity;
import com.soulware.therapydraft.infrastructure.persistence.jpa.mappers.TherapyPlanMapper;
import com.soulware.therapydraft.shared.infrastructure.PagedResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class JpaTherapyPlanRepository implements TherapyPlanRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final TherapyPlanMapper therapyPlanMapper;

    @Inject
    public JpaTherapyPlanRepository(TherapyPlanMapper therapyPlanMapper) {this.therapyPlanMapper = therapyPlanMapper;}

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<TherapyPlan> findById(TherapyPlanId id) {
        if (id == null) {
            return Optional.empty();
        }
        TherapyPlanEntity entity = entityManager.find(TherapyPlanEntity.class, id.value());
        return Optional.ofNullable(entity)
                .map(therapyPlanMapper::toDomain);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void save(TherapyPlan plan) {
        if (plan == null) {
            return;
        }
        TherapyPlanEntity entity = therapyPlanMapper.toEntity(plan);
        entityManager.merge(entity);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<TherapyPlan> findByAssignedTherapistId(TherapistId therapistId) {
        if (therapistId == null) {
            return List.of();
        }

        List<TherapyPlanEntity> entities = entityManager.createQuery(
                        "SELECT tp FROM TherapyPlanEntity tp WHERE tp.assignedTherapistId = :therapistId",
                        TherapyPlanEntity.class)
                .setParameter("therapistId", therapistId.value())
                .getResultList();

        return entities.stream()
                .map(therapyPlanMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public PagedResult<TherapyPlan> findByFilters(
            Long assessmentId,
            Long therapistId,
            Long patientId,
            Long legalResponsibleId,
            int page,
            int size
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<TherapyPlanEntity> cq = cb.createQuery(TherapyPlanEntity.class);
        Root<TherapyPlanEntity> root = cq.from(TherapyPlanEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        //assessmentId
        if (assessmentId != null)
            predicates.add(cb.equal(root.get("assessmentId"), assessmentId));

        //therapistId
        if (therapistId != null)
            predicates.add(cb.equal(root.get("therapistId"), therapistId));

        //patientId
        if (patientId != null)
            predicates.add(cb.equal(root.get("patientId"), patientId));

        //legalResponsibleId
        if (legalResponsibleId != null)
            predicates.add(cb.equal(root.get("legalResponsibleId"), legalResponsibleId));

        cq.where(predicates.toArray(new Predicate[0]));

        List<TherapyPlanEntity> entities = entityManager.createQuery(cq)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();

        CriteriaQuery<Long>  countQuery = cb.createQuery(Long.class);
        Root<TherapyPlanEntity> countRoot = countQuery.from(TherapyPlanEntity.class);

        List<Predicate> countPredicates = new ArrayList<>();

        //assessmentId
        if (assessmentId != null)
            countPredicates.add(cb.equal(countRoot.get("assessmentId"), assessmentId));

        //therapistId
        if (therapistId != null)
            countPredicates.add(cb.equal(countRoot.get("therapistId"), therapistId));

        //patientId
        if (patientId != null)
            countPredicates.add(cb.equal(countRoot.get("patientId"), patientId));

        //legalResponsibleId
        if (legalResponsibleId != null)
            countPredicates.add(cb.equal(countRoot.get("legalResponsibleId"), legalResponsibleId));

        countQuery.select(cb.count(countRoot));
        countQuery.where(countPredicates.toArray(new Predicate[0]));

        Long totalItems = entityManager.createQuery(countQuery).getSingleResult();

        List<TherapyPlan> items = entities.stream()
                .map(therapyPlanMapper::toDomain)
                .toList();

        return new PagedResult<>(items, totalItems, page, size);
    }
}
