package edu.dsa.clinic.dto;

import edu.dsa.clinic.entity.Consultation;
import edu.dsa.clinic.entity.Diagnosis;
import edu.dsa.clinic.entity.Treatment;
import edu.dsa.clinic.entity.Prescription;

/**
 * A flattened row representation of Consultation → Diagnosis → Treatment → Prescription,
 * used for displaying in a single summarized table.
 */
public final class PatientDetail {
    private final Consultation consultation;
    private final Diagnosis diagnosis;
    private final Treatment treatment;
    private final Prescription prescription;

    public PatientDetail(
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

    @Override
    public String toString() {
        return "Consultation: " +
                (consultation != null ? consultation.getId() : "N/A") +
                " | Diagnosis: " +
                (diagnosis != null ? diagnosis.getDescription() : "N/A") +
                " | Treatment: " +
                (treatment != null ? treatment.getSymptom() : "N/A") +
                " | Prescription: " +
                (prescription != null ? prescription.getProduct().getName() : "N/A");
    }
}
