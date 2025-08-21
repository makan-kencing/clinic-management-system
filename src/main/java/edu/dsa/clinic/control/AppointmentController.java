package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.SortedDoubleLinkedList;
import edu.dsa.clinic.boundary.AppointmentUI;
import edu.dsa.clinic.entity.Appointment;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.entity.Doctor;

public class AppointmentController {

    public void createAppointment(Appointment appointment, Doctor doctor) {

//        if(hasOverLappingAppointments(appointment, doctor)){
//            findClosestAvailableTime();
//        }else if(schedule is occupied but doctor is free the next time){
//
//        }else{
//            Database.appointmentList.add(appointment);
//        }

    }


    public void searchAppointment(int id){
        //sortViewAppointment();
    }

    public void cancelAppointment(int id){

    }

    public void sortViewAppointment(){

    }

    public void findClosestAvailableTime(){

    }

    public boolean hasOverLappingAppointments(Appointment appointment, Doctor doctor){
        return true;
    }

}
