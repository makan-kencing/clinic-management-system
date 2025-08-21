package edu.dsa.clinic.entity;

import edu.dsa.clinic.utils.Range;

import java.time.LocalTime;

/**
 * The working hours defined for a {@link Doctor}.
 *
 * @author daren
 * @see Doctor
 */
public class Shift {
    private Doctor doctor;
    private ShiftType type;
    private Range<LocalTime> timeRange;

    public Doctor getDoctor() {
        return doctor;
    }

    public Shift setDoctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

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
                "doctor=" + doctor +
                ", type=" + type +
                ", timeRange=" + timeRange +
                '}';
    }
}
