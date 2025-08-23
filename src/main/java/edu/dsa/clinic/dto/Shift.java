package edu.dsa.clinic.dto;

import edu.dsa.clinic.entity.Doctor;

import java.time.LocalTime;

/**
 * The working hours defined for a {@link Doctor}.
 *
 * @author daren
 * @see Doctor
 */
public class Shift {
    private ShiftType type;
    private Range<LocalTime> timeRange;

    public ShiftType getType() {
        return type;
    }

    public Shift setType(ShiftType type) {
        this.type = type;
        return this;
    }

    public Range<LocalTime> getTimeRange() {
        return timeRange;
    }

    public Shift setTimeRange(Range<LocalTime> timeRange) {
        this.timeRange = timeRange;
        return this;
    }

    @Override
    public String toString() {
        return "Shift{" +
                "type=" + type +
                ", timeRange=" + timeRange +
                '}';
    }
}
