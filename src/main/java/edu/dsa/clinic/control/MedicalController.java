package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.entity.Doctor;
import edu.dsa.clinic.entity.Patient;
import edu.dsa.clinic.entity.Consultation;

import java.io.IOException;

public class MedicalController {

    public Consultation createConsultationRecord(Patient patient , Doctor doctor) throws IOException {
        if (patient==null){
            throw new IllegalArgumentException("patient is null");
        }
        Consultation consultation = new Consultation();
        consultation.setPatient(patient);
        consultation.setDoctor(doctor);
        Database.consultationList.add(consultation);

        return consultation;
    }
    public Consultation listConsultations(Patient patient) throws IOException {
        for (Consultation consultation : Database.consultationList) {
            if (consultation.getPatient().equals(patient)) {
                return consultation;
            }
        }
        return null;
    }
    public void createDiagnosisForConsultation() throws IOException {

    }
}
