/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.dsa.clinic.control;

/**
 *
 * @author Bincent
 */
import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.SortedDoubleLinkedList;
import edu.dsa.clinic.entity.ConsultationQueue;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.entity.ConsultationType;
import java.util.Comparator;

public class PatientController {

    private ListInterface<Patient> patientList = new DoubleLinkedList<>();
    private ListInterface<ConsultationQueue> queueList = new SortedDoubleLinkedList<>(Comparator.comparingInt(ConsultationQueue::getId));

    public boolean createPatientRecord(Patient patient){
        return true;
    }

    public void editPatientRecord(){

    }

    public void removePatientRecord(){

    }

    public void viewPatientList(){

    }

    public void viewPatientDetail(){

    }

    public void createQueueNumber() {

    }

    public void removeQueueNumber() {

    }

    public void viewConnsultationQueue() {

    }

    public void viewSummaryReport() {

    }
}
