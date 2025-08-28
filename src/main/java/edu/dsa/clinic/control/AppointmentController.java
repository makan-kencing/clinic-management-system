package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.dto.ShiftType;
import edu.dsa.clinic.dto.Range;
import edu.dsa.clinic.entity.Appointment;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.filter.AppointmentFilter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;

public class AppointmentController {

    public void saveAppointment(Appointment appointment) {

//        if(hasOverLappingAppointments(appointment, appointment.getDoctor())){
//            findClosestAvailableTime();
//        }else if(schedule is occupied but doctor is free the next time){
//
//        }else{
            Database.appointmentList.add(appointment);
//        }

    }

    public Object performSelect(int selectedId, String field) {
        return switch (field) {
            case "appointment" -> Database.appointmentList.findFirst(p -> p.getId() == selectedId);
            default -> null;
        };
    }

    public boolean isWorkingDuring(Doctor doctor, DayOfWeek dayOfWeek, Range<LocalTime> timeRange) {
        var shiftsOfTheDay = doctor.getSchedule().getShifts(dayOfWeek)
                .filtered(s -> s.getType() == ShiftType.WORK);

        if (shiftsOfTheDay.size() == 0)
            return false;

        for (var shift : shiftsOfTheDay) {
            var workingHours = shift.getTimeRange();
            if (workingHours.contains(timeRange)) {
                return true;
            }
        }

        return false;
    }

    public boolean isWorkingDuring(Doctor doctor, LocalDate date, Range<LocalTime> timeRange) {
        return isWorkingDuring(doctor, date.getDayOfWeek(), timeRange);
    }

    public boolean isUnoccupiedDuring(Doctor doctor, LocalDate date, Range<LocalTime> datetimeRange) {
        var scheduledAppointments = Database.appointmentList.filtered(
                a -> a.getDoctor() == doctor && a.getExpectedStartAt().toLocalDate().equals(date)
        );

        for (var appointment : scheduledAppointments) {
            var startingTime = appointment.getExpectedStartAt().toLocalTime();
            var endingTime = appointment.getExpectedEndAt().toLocalTime();

            var appointmentTimeRange = new Range<>(startingTime, endingTime);
            if (appointmentTimeRange.overlapsExclusively(datetimeRange)) {
                return false;
            }
        }

        return true;
    }

    public boolean isAvailable(Doctor doctor, LocalDate date, Range<LocalTime> timeRange) {
        return !isWorkingDuring(doctor, date, timeRange)
                || !isUnoccupiedDuring(doctor, date, timeRange);
    }



    public ListInterface<Appointment> getAppointments() {
        return Database.appointmentList.clone();
    }


    public void cancelAppointment(int index){
        Database.appointmentList.remove(index);
    }

    public void findClosestAvailableTime(){

    }

    public boolean hasOverLappingAppointments(Appointment appointment, Doctor doctor){
        return true;
    }

    public static ListInterface<Appointment> getDoctorAppointmentsForTheWeek(TemporalAccessor hasWeek, Doctor doctor) {
        return Database.appointmentList.filtered(
                AppointmentFilter.isSameWeek(hasWeek)
                        .and(AppointmentFilter.byDoctor(doctor))
        );
    }
}
