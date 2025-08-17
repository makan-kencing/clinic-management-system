package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Patient;

import java.io.IOException;

public class MedicalController {

    public void createConsultationRecord(Patient patient) throws IOException {
        if (patient==null){
            throw new IllegalArgumentException("patient is null");
        }
        Database.patientsList.add(patient);

    }
    public void createDiagnosisForConsultation() throws IOException {

    }
}
