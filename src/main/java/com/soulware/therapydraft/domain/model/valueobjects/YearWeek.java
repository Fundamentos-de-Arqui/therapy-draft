package com.soulware.therapydraft.domain.model.valueobjects;

import java.util.Objects;

/**
 * YearWeek value object.
 * Represents a specific week within a year, typically used to identify weekly schedules or agendas.
 * Ensures data integrity for year and week numbers upon creation.
 */
public final class YearWeek {
    private final int year;
    private final int week;

    private static final int MIN_YEAR = 2000;
    private static final int MIN_WEEK = 1;
    private static final int MAX_WEEK = 53;

    /**
     * Constructs the YearWeek value object.
     *
     * @param year The calendar year (must be 2000 or later).
     * @param week The week number (must be between 1 and 53).
     * @throws IllegalArgumentException if the year or week provided are outside the valid range.
     */
    public YearWeek(int year, int week) {
        if (year < MIN_YEAR) {
            throw new IllegalArgumentException("Year must be " + MIN_YEAR + " or later.");
        }
        if (week < MIN_WEEK || week > MAX_WEEK) {
            throw new IllegalArgumentException("Week number must be between " + MIN_WEEK + " and " + MAX_WEEK + ".");
        }

        this.year = year;
        this.week = week;
    }

    /**
     * Provides a standard string representation (YYYY-WXX) of the YearWeek.
     * @return The formatted YearWeek string.
     */
    public String toFormattedString() {
        return String.format("%d-W%02d", this.year, this.week);
    }

    public static YearWeek fromFormattedString(String yearWeekString) {
        if (yearWeekString == null || !yearWeekString.contains("-")) {
            throw new IllegalArgumentException("Invalid YearWeek format. Expected YYYY-XX.");
        }

        String[] parts = yearWeekString.split("-");

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid YearWeek format. Expected YYYY-XX.");
        }

        int year = Integer.parseInt(parts[0]);
        int week = Integer.parseInt(parts[1]);

        return new YearWeek(year, week);
    }

    public int getYear() {
        return this.year;
    }

    public int getWeek() {
        return this.week;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YearWeek yearWeek = (YearWeek) o;
        return year == yearWeek.year && week == yearWeek.week;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, week);
    }

    /**
     * Provides a simple string representation (e.g., 2025-W15).
     */
    @Override
    public String toString() {
        return toFormattedString();
    }
}
