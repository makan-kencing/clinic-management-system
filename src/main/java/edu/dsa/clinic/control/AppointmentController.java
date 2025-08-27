package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Appointment;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.filter.AppointmentFilter;

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
