package edu.dsa.clinic.control;

import edu.dsa.clinic.Database;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Patient;

public class MedicalController {

    public void saveConsultationRecord(Consultation consultation) {
        Database.consultationsList.add(consultation);
    }

    public Consultation listConsultations(Patient patient) {
        for (Consultation consultation : Database.consultationsList) {
            if (consultation.getPatient().equals(patient)) {
                return consultation;
            }
        }
        return null;
    }

    public ListInterface<Consultation> getConsultationList() {
        return Database.consultationsList;
    }

}
