package edu.dsa.clinic.control;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Patient;

import java.io.IOException;

public class MedicalController {
    private final ListInterface<Patient> patientList =new DoubleLinkedList<>();
    private final ListInterface<Diagnosis> diagnosisList =new DoubleLinkedList<>();



    public void createConsultationRecord(Patient patient) throws IOException {
        if (patient==null){
            throw new IllegalArgumentException("patient is null");
        }
        patientList.add(patient);

    }
    public void createDiagnosisForConsultation() throws IOException {

    }
}
