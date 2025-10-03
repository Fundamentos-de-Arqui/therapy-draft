package com.soulware.therapydraft.domain.model.valueobjects;

import java.time.Duration;
import java.time.ZonedDateTime;

/**
 * TimeSlot value object.
 * Represents the start and end of a session, in blocks of 30 minutes (e.g. 11:00 or 11:30)
 */
public class TimeSlot {
    private final ZonedDateTime start;
    private final ZonedDateTime end;

    /**
     * Constructor of the value object.
     * Validates that both the start and end times comply with the 30-minute
     * rigid block business rule by calling the {@code isThirtyMinuteInterval(ZonedDateTime time)} function.
     * @param start The starting point of the time slot, which must fall exactly on
     * the hour (e.g., XX:00) or half-hour (e.g., XX:30).
     * @param end   The ending point of the time slot, which must also fall exactly
     * on the hour (e.g., XX:00) or half-hour (e.g., XX:30).
     * @throws IllegalArgumentException if either start time or end time provided are null, if the start time is in the past, if the start time is after the end time or if either the start time or the end time does not comply with the 30-minute interval rule.
     */
    public TimeSlot(ZonedDateTime start, ZonedDateTime end) {
        if (start == null || end == null) {
            throw new IllegalArgumentException("Start and end time cannot be null.");
        }
        if (start.isBefore(ZonedDateTime.now())) {
            throw new IllegalArgumentException("Start time cannot be in the past.");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start time must be before the end time.");
        }
        if (isNotThirtyMinuteInterval(start) || isNotThirtyMinuteInterval(end)) {
            throw new IllegalArgumentException("Time must start/end on a 00 or 30 minute mark.");
        }

        this.start = start;
        this.end = end;
    }

    /**
     * Checks if this time slot if overlapped with other time slot
     * It does so by checking if this ends before the other and the other starts after this
     * Contiguous time slots such as 10:00-11:00 and 11:00-11:30 are not considered overlapped
     *
     * @param other the time slot we want to know if is overlapped with this
     * @return true if overlaps, false otherwise
     * @throws IllegalArgumentException if other time slot is null
     */
    public boolean overlaps(TimeSlot other) {
        if (other == null) {
            throw new IllegalArgumentException("Other time slot cannot be null when calling this function.");
        }

        boolean thisEndsBeforeOtherStarts = this.end.isBefore(other.start) || this.end.isEqual(other.start);
        boolean otherEndsBeforeThisStarts = other.end.isBefore(this.start) || other.end.isEqual(this.start);

        return !(thisEndsBeforeOtherStarts || otherEndsBeforeThisStarts);
    }

    /**
     * Provides the time span of the time slot
     * @return the duration between the start and the end of this time slot.
     */
    public Duration duration() {
        return Duration.between(this.start, this.end);
    }

    /**
     * Checks if a given ZonedDateTime complies to the 30 minutes block business rule.
     * This rule is applied to start and ending times of therapy sessions, ensuring
     * that the minute component is either 0 or 30.
     * @param time the date time we want to check if complies to the rule
     * @return true if the minute component of the time is exactly 0 or 30,
     * false otherwise.
     */
    private boolean isNotThirtyMinuteInterval(ZonedDateTime time) {
        int minute = time.getMinute();
        return minute != 0 && minute != 30;
    }

    // Getters
    public ZonedDateTime getStart() {
        return this.start;
    }
    public ZonedDateTime getEnd() {
        return this.end;
    }
}
