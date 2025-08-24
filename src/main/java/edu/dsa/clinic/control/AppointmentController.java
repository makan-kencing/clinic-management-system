package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.dto.Range;
import edu.dsa.clinic.entity.Appointment;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.lambda.Filter;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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

    public static Filter<Doctor> getNotOccupiedFilter(LocalDate date, Range<LocalTime> datetimeRange) {
        return d -> {
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
}
