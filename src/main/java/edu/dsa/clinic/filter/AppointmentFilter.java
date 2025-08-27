package edu.dsa.clinic.filter;

import edu.dsa.clinic.entity.Appointment;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.lambda.Filter;

import java.time.temporal.TemporalAccessor;
import java.time.temporal.WeekFields;

public interface AppointmentFilter {
    static Filter<Appointment> byDoctor(Doctor doctor) {
        return a -> a.getDoctor().equals(doctor);
    }

    static Filter<Appointment> isSameWeek(TemporalAccessor date) {
        final var weekSelector = WeekFields.ISO.weekOfYear();
        return a -> a.getExpectedStartAt().get(weekSelector) == date.get(weekSelector);
    }

}
