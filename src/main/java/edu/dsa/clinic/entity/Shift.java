package edu.dsa.clinic.entity;

import edu.dsa.clinic.utils.Range;

import java.time.LocalTime;

/**
 * The working hours defined for a {@link Doctor}.
 *
 * @author TODO
 * @see Doctor
 */
public class Shift extends IdentifiableEntity {
    private Doctor doctor;
    private ShiftType type;
    private Range<LocalTime> timeRange;

    @Override
    public String toString() {
        return "Shift{" +
                "doctor=" + doctor +
                ", type=" + type +
                ", timeRange=" + timeRange +
                '}';
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public ShiftType getType() {
        return type;
    }

    public void setType(ShiftType type) {
        this.type = type;
    }

    public Range<LocalTime> getTimeRange() {
        return timeRange;
    }

    public void setTimeRange(Range<LocalTime> timeRange) {
        this.timeRange = timeRange;
    }
}
