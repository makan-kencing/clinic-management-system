package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Appointment;
import edu.dsa.clinic.entity.Doctor;

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
}
