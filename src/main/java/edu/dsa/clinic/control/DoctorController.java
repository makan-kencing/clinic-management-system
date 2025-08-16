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

public class DoctorController {
    
    private ListInterface<Doctor> DoctorList;
    private SortedDoubleLinkedList<Shift> ShiftList;
    
    public DoctorController() {
        this.DoctorList = new DoubleLinkedList<>();
        this.ShiftList = new SortedDoubleLinkedList<>(
            (s1, s2) -> s1.getShiftTime().compareTo(s2.getShiftTime())
        );
    }
    
    public void addDoctorRecord(){
        
    }
    
    public void editDoctorInfo(){
        
    }
    
    public void removeDoctorInfo(){
        
    }
    
    public void viewDoctorInfo(){
        
    }
    
    public ListInterface<Doctor> doctorsList(){
        return DoctorList;
    }
    
    public void addShift(){
        
    }
    
    public void editShift(){
        
    }
    
    public void removeShift(){
        
    }
    
    public void viewShift(){
        
    }
    
    public ListInterface<Shift> listShiftsForDoctor(){
        return ShiftList;
    }
    
    public ListInterface<Shift> allShifts(){
        return ShiftList;
    }
}
