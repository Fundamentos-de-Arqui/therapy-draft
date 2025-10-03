package com.soulware.therapydraft.domain.model.valueobjects;

import java.time.DayOfWeek;
import java.util.Map;
/**
 * WeeklySchedule value object.
 * Represents the rigid weekly availability schedule, mapping each day of the week to a specific time slot.
 * Ensures the internal schedule map is immutable upon creation.
 */
public record WeeklySchedule(Map<DayOfWeek, TimeSlot> schedule) {
    /**
     * Constructs a new WeeklySchedule and enforces business constraints.
     *
     * @param schedule The map defining the TimeSlot for each DayOfWeek.
     * @throws IllegalArgumentException if the map is null, or if it contains any null keys (DayOfWeek) or null values (TimeSlot).
     */
    public WeeklySchedule(Map<DayOfWeek, TimeSlot> schedule) {
        if (schedule == null) {
            throw new IllegalArgumentException("Weekly schedule cannot be null.");
        }

        schedule.forEach((day, slot) -> {
            if (day == null) {
                throw new IllegalArgumentException("Weekly schedule cannot contain null days.");
            }
            if (slot == null) {
                throw new IllegalArgumentException("TimeSlot for " + day + " cannot be null.");
            }
        });

        this.schedule = Map.copyOf(schedule);
    }

    /**
     * Retrieves the TimeSlot scheduled for a specific day of the week.
     *
     * @param dayOfWeek The day for which to retrieve the time slot. Cannot be null.
     * @return The TimeSlot defined for that day, or null if no slot is defined.
     * @throws NullPointerException if dayOfWeek is null.
     */
    public TimeSlot getTimeSlotForDay(DayOfWeek dayOfWeek) {
        if (dayOfWeek == null) {
            throw new IllegalArgumentException("Day of week cannot be null.");
        }
        return schedule.get(dayOfWeek);
    }

    /**
     * Checks if the weekly schedule contains any defined time slots.
     *
     * @return true if the schedule map is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.schedule.isEmpty();
    }
}