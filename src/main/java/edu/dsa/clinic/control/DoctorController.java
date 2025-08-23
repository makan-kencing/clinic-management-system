/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Patient;

public class DoctorController {
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


    public void addShift(){
        
    }
    
    public void editShift(){
        
    }
    
    public void removeShift(){
        
    }
    
    public void viewShift(){
        
    }
}
