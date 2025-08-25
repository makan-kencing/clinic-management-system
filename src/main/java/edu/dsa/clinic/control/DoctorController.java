/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.dto.Schedule;
import edu.dsa.clinic.dto.Shift;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.filter.DoctorFilter;

import java.time.format.DateTimeFormatter;

public class DoctorController {
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm"); //For Formatting Shift Time

    public static void addDoctorRecord(Doctor doctor){
        Database.doctorList.add(doctor);
    }

    public static Doctor deleteDoctorByID(int id){
        return  Database.doctorList.removeFirst(DoctorFilter.byId(id));
    }

    public static Doctor selectDoctorByID(int id) {
        return Database.doctorList.findFirst(DoctorFilter.byId(id));
    }

    public static int getDoctorCount(){
        return Database.doctorList.size();
    }

    public static ListInterface<Doctor> getDoctors(){
        return Database.doctorList;
    }

    public static void addShift(ListInterface<Shift> currentShifts, Shift shift) {
        // Not sure whether this is how you add shift
        currentShifts.add(shift);
    }

    public static Schedule getOverallSchedule() {
        return null;
    }

    public static void editShift(){
        
    }

    public static void removeShift(){
        
    }
}
