package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Treatment;

import java.io.IOException;

public class MedicalController {

    public void createConsultationRecord(Consultation consultation) throws IOException {
        Database.consultationsList.add(consultation);
    }

    public void createDiagnosisRecord(Diagnosis diagnosis) throws IOException {
        Database.diagnosisList.add(diagnosis);
    }

    public void createTreatmentRecord(Treatment treatment, Prescription prescription) throws IOException {
        Database.prescriptionsList.add(prescription);
    }

    public Consultation listConsultations(Patient patient) throws IOException {
        for (Consultation consultation : Database.consultationsList) {
            if (consultation.getPatient().equals(patient)) {
                return consultation;
            }
        }
        return null;
    }

}
