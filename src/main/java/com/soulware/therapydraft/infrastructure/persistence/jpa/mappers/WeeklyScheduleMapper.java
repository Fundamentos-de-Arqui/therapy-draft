package com.soulware.therapydraft.infrastructure.persistence.jpa.mappers;

import com.soulware.therapydraft.domain.model.valueobjects.TimeSlot;
import com.soulware.therapydraft.domain.model.valueobjects.WeeklySchedule;
import com.soulware.therapydraft.infrastructure.persistence.jpa.entities.TherapyScheduleEntryEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class WeeklyScheduleMapper{
    @Inject
    public WeeklyScheduleMapper() {}

    public WeeklySchedule toDomain(List<TherapyScheduleEntryEntity> entities) {
        if (entities == null) {
            return null;
        }

        Map<DayOfWeek, TimeSlot> scheduleMap = entities.stream()
                .collect(Collectors.toMap(
                        entity -> DayOfWeek.valueOf(entity.getDay()),
                        entity -> new TimeSlot(entity.getStartTime(), entity.getEndTime())
                ));
                return new WeeklySchedule(scheduleMap);
    }

    public List<TherapyScheduleEntryEntity> toEntity(WeeklySchedule domain, Long planId) {
        if (domain == null || domain.schedule().isEmpty()) {
            return List.of();
        }

        return domain.schedule().entrySet().stream()
                .map(entry -> {
                    TherapyScheduleEntryEntity entity = new TherapyScheduleEntryEntity();
                    entity.setPlanId(planId);
                    entity.setDay(entry.getKey().name());
                    entity.setStartTime(entry.getValue().getStart());
                    entity.setEndTime(entry.getValue().getEnd());

                    return entity;
                })
                .collect(Collectors.toList());
    }
}
