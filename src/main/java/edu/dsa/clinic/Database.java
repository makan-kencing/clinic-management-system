package edu.dsa.clinic;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.Patient;

public class Database {
    public final static ListInterface<Patient> patientsList = new DoubleLinkedList<>();
    public final static ListInterface<Diagnosis> diagnosisList = new DoubleLinkedList<>();
    public final static ListInterface<Consultation> consultationList = new DoubleLinkedList<>();
    public final static ListInterface<Medicine> medicineList = new DoubleLinkedList<>();

    private Database() {
    }
}
