package com.soulware.therapydraft.interfaces.rest.assemblers;

import com.soulware.therapydraft.domain.model.valueobjects.TimeSlot;
import com.soulware.therapydraft.domain.model.valueobjects.WeeklySchedule;
import com.soulware.therapydraft.interfaces.rest.resources.ScheduleEntryResource;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScheduleEntryResourceFromEntityAssembler {

    public static List<ScheduleEntryResource> toResourceList(WeeklySchedule weeklySchedule) {
        if (weeklySchedule == null || weeklySchedule.schedule().isEmpty()) {
            return List.of();
        }

        return weeklySchedule.schedule().entrySet().stream()
                .map(ScheduleEntryResourceFromEntityAssembler::toResource)
                .collect(Collectors.toList());
    }

    private static ScheduleEntryResource toResource(Map.Entry<DayOfWeek, TimeSlot> entry) {
        return new ScheduleEntryResource(
                null,
                entry.getKey().name(),
                entry.getValue().getStart(),
                entry.getValue().getEnd()
        );
    }
}
