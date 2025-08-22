/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.dsa.clinic.control;

/**
 *
 * @author daren
 */
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.SortedDoubleLinkedList;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Shift;
import edu.dsa.clinic.entity.ShiftType;
import edu.dsa.clinic.Database;

public class DoctorController {
    public ListInterface<Doctor> doctorsList;
    public ListInterface<Shift> doctorShift;




    public DoctorController() {

    }
    
    public void addDoctorRecord(){
        
    }
    
    public void updateDoctorInfo(){
        
    }
    
    public void deleteDoctorInfo(){
        
    }

    public static int getDoctorCount(){
        return Database.doctorsList.size();
    }

    public void getDoctorInfo(){
        
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
