package edu.dsa.clinic;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Treatment;
import edu.dsa.clinic.entity.Doctor;

public class Database {
    public final static ListInterface<Patient> patientsList = new DoubleLinkedList<>();
    public final static ListInterface<Diagnosis> diagnosisList = new DoubleLinkedList<>();
    public final static ListInterface<Consultation> consultationsList = new DoubleLinkedList<>();
    public final static ListInterface<Medicine> medicineList = new DoubleLinkedList<>();
    public final static ListInterface<Prescription> prescriptionsList = new DoubleLinkedList<>();
    public final static ListInterface<Treatment> treatmentsList = new DoubleLinkedList<>();
    public final static ListInterface<Doctor> doctorsList = new DoubleLinkedList<>();

    private Database() {
    }
}
