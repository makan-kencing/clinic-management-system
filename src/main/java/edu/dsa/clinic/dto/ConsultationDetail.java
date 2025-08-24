package edu.dsa.clinic.dto;

import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Prescription;
import edu.dsa.clinic.entity.Treatment;

public final class ConsultationDetail {
    private final Consultation consultation;
    private final Diagnosis diagnosis;
    private final Treatment treatment;
    private final Prescription prescription;

    public ConsultationDetail(
            Consultation consultation,
            Diagnosis diagnosis,
            Treatment treatment,
            Prescription prescription
    ) {
        this.consultation = consultation;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.prescription = prescription;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public Treatment getTreatment() {
        return treatment;
    }

    public Prescription getPrescription() {
        return prescription;
    }


}
