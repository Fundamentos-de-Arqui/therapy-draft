package com.soulware.therapydraft.interfaces.rest.resources;

import java.time.ZonedDateTime;

public record ScheduleEntryResource(
        Long id,
        String dayOfWeek,
        ZonedDateTime startTime,
        ZonedDateTime endTime
) {
}
