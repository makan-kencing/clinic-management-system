/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.dto.Range;
import edu.dsa.clinic.dto.Shift;
import edu.dsa.clinic.dto.ShiftType;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.dto.Schedule;
import edu.dsa.clinic.dto.Shift;
import edu.dsa.clinic.lambda.Filter;
import edu.dsa.clinic.utils.Utils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DoctorController {

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm"); //For Formatting Shift Time

    public DoctorController() {

    }
    
    public void addDoctorRecord(){
        
    }
    

    
    public Doctor deleteDoctorbyID(int id){
        return  Database.doctorList.removeFirst(m -> m.getId() == id);
    }

    public Doctor selectDoctorByID(int id) {
        return Database.doctorList.findFirst(d -> d.getId() == id);
    }

    public static int getDoctorCount(){
        return Database.doctorList.size();
    }

    public ListInterface<Doctor> getDoctors(){
        return Database.doctorList;
    }


    public void addShift(ListInterface<Shift> currentShifts, Shift shift) {



    }
    
    public void editShift(){
        
    }
    
    public void removeShift(){
        
    }



}
