package com.soulware.therapydraft.infrastructure.persistence.jpa.repositories;

import com.soulware.therapydraft.domain.model.aggregates.Assessment;
import com.soulware.therapydraft.domain.model.valueobjects.ids.AssessmentId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.PatientId;
import com.soulware.therapydraft.domain.model.valueobjects.ids.TherapistId;
import com.soulware.therapydraft.domain.repositories.AssessmentRepository;
import com.soulware.therapydraft.infrastructure.persistence.jpa.entities.AssessmentEntity;
import com.soulware.therapydraft.infrastructure.persistence.jpa.mappers.AssessmentMapper;
import com.soulware.therapydraft.shared.infrastructure.PagedResult;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class JpaAssessmentRepository implements AssessmentRepository {
    @PersistenceContext
    private EntityManager entityManager;

    private final AssessmentMapper mapper;

    @Inject
    public JpaAssessmentRepository(AssessmentMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public Optional<Assessment> findById(AssessmentId id) {
        if (id == null) return Optional.empty();

        AssessmentEntity entity = entityManager.find(AssessmentEntity.class, id.value());
        return Optional.ofNullable(entity)
                .map(mapper::toDomain);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void save(Assessment assessment) {
        if (assessment == null) return;

        AssessmentEntity entity = mapper.toEntity(assessment);
        entityManager.persist(entity);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void update(Assessment assessment) {
        if (assessment == null) return;

        AssessmentEntity entity = entityManager.find(
                AssessmentEntity.class,
                assessment.getId().value()
        );

        if (entity == null) {
            throw new EntityNotFoundException("Assessment not found");
        }

        mapper.updateEntity(entity, assessment);

        entityManager.merge(entity);
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Assessment> findByPatientId(PatientId patientId) {
        if (patientId == null) return List.of();

        List<AssessmentEntity> entities = entityManager.createQuery(
                        "SELECT a FROM AssessmentEntity a WHERE a.patientId = :pid", AssessmentEntity.class)
                .setParameter("pid", patientId.value())
                .getResultList();

        return entities.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Assessment> findByTherapistId(TherapistId therapistId) {
        if (therapistId == null) return List.of();

        List<AssessmentEntity> entities = entityManager.createQuery(
                        "SELECT a FROM AssessmentEntity a WHERE a.therapistId = :tid", AssessmentEntity.class)
                .setParameter("tid", therapistId.value())
                .getResultList();

        return entities.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    @Transactional(Transactional.TxType.SUPPORTS)
    public PagedResult<Assessment> findByFilters(
            Long patientId,
            Long therapistId,
            String status,
            String scheduledAt,
            int page,
            int size
    ) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<AssessmentEntity> cq = cb.createQuery(AssessmentEntity.class);
        Root<AssessmentEntity> root = cq.from(AssessmentEntity.class);

        List<Predicate> predicates = new ArrayList<>();

        // patientId
        if (patientId != null)
            predicates.add(cb.equal(root.get("patientId"), patientId));

        // therapistId
        if (therapistId != null)
            predicates.add(cb.equal(root.get("therapistId"), therapistId));

        // status.name
        if (status != null)
            predicates.add(cb.equal(root.get("status").get("name"), status));

        // scheduledAt
        if (scheduledAt != null)
            predicates.add(cb.equal(
                    root.get("scheduledAt"),
                    ZonedDateTime.parse(scheduledAt)
            ));

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("scheduledAt")));

        List<AssessmentEntity> entities = entityManager.createQuery(cq)
                .setFirstResult(page * size)
                .setMaxResults(size)
                .getResultList();

        // --- COUNT QUERY ---
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<AssessmentEntity> countRoot = countQuery.from(AssessmentEntity.class);

        List<Predicate> countPredicates = new ArrayList<>();

        if (patientId != null)
            countPredicates.add(cb.equal(countRoot.get("patientId"), patientId));

        if (therapistId != null)
            countPredicates.add(cb.equal(countRoot.get("therapistId"), therapistId));

        if (status != null)
            countPredicates.add(cb.equal(countRoot.get("status").get("name"), status));

        if (scheduledAt != null)
            countPredicates.add(cb.equal(
                    countRoot.get("scheduledAt"),
                    ZonedDateTime.parse(scheduledAt)
            ));

        countQuery.select(cb.count(countRoot));
        countQuery.where(countPredicates.toArray(new Predicate[0]));

        Long totalItems = entityManager.createQuery(countQuery).getSingleResult();

        List<Assessment> items = entities.stream()
                .map(mapper::toDomain)
                .toList();

        return new PagedResult<>(items, totalItems, page, size);
    }

}
