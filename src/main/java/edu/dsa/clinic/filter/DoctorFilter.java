package edu.dsa.clinic.filter;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.dto.Range;
import edu.dsa.clinic.dto.ShiftType;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Gender;
import edu.dsa.clinic.entity.Specialization;
import edu.dsa.clinic.lambda.Filter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

public interface DoctorFilter {
    static Filter<Doctor> byId(int id) {
        return d -> d.getId() == id;
    }

    static Filter<Doctor> byNameLike(String name) {
        return d -> d.getName().toLowerCase()
                .contains(name.toLowerCase());
    }

    static Filter<Doctor> byName(String name) {
        return d -> d.getName().equals(name);
    }

    static Filter<Doctor> byGender(Gender gender) {
        return d -> d.getGender() == gender;
    }

    static Filter<Doctor> byContactNumberLike(String contactNumber) {
        return d -> d.getContactNumber().toLowerCase()
                .contains(contactNumber.toLowerCase());
    }

    static Filter<Doctor> bySpecialization(Specialization specialization) {
        return d -> d.getSpecialization() == specialization;
    }

    static Filter<Doctor> isWorkingDuring(DayOfWeek dayOfWeek, Range<LocalTime> timeRange) {
        return d -> {
            var schedule = d.getSchedule();

            var shiftsOfTheDay = schedule.getShifts(dayOfWeek);
            shiftsOfTheDay = shiftsOfTheDay.filtered(s -> s.getType() == ShiftType.WORK);
            if (shiftsOfTheDay.size() == 0)
                return false;

            for (var shift : shiftsOfTheDay) {
                var workingHours = shift.getTimeRange();
                if (!workingHours.contains(timeRange))
                    return false;
            }

            return true;
        };
    }

    static Filter<Doctor> isWorkingDuring(LocalDate date, Range<LocalTime> timeRange) {
        return isWorkingDuring(date.getDayOfWeek(), timeRange);
    }

    static Filter<Doctor> isUnoccupiedDuring(LocalDate date, Range<LocalTime> datetimeRange) {
        return d -> {
            // TODO: use AppointmentController::getAppointments
            var scheduledAppointments = Database.appointmentList.filtered(
                    a -> a.getDoctor() == d && a.getExpectedStartAt().toLocalDate() == date
            );

            for (var appointment : scheduledAppointments) {
                var startingTime = appointment.getExpectedStartAt().toLocalTime();
                var endingTime = appointment.getExpectedEndAt().toLocalTime();

                var appointmentTimeRange = new Range<>(startingTime, endingTime);
                if (appointmentTimeRange.overlapsExclusively(datetimeRange))
                    return false;
            }

            return true;
        };
    }

    static Filter<Doctor> isAvailable(LocalDate date, Range<LocalTime> timeRange) {
        return isWorkingDuring(date, timeRange)
                .and(isUnoccupiedDuring(date, timeRange));
    }
}
