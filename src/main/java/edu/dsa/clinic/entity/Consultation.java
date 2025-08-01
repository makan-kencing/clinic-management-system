package edu.dsa.clinic.entity;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The meeting session between a patient and a doctor.
 *
 * @author TODO
 * @see Doctor
 * @see Patient
 */
public class Consultation extends IdentifiableEntity {
    private Patient patient;
    private Doctor doctor;
    private List<Diagnosis> diagnoses;
    private List<Treatment> treatments;
    private @Nullable String notes;

    @Override
    public String toString() {
        return "Consultation{" +
                "patient=" + patient +
                ", doctor=" + doctor +
                ", diagnoses=" + diagnoses +
                ", treatments=" + treatments +
                ", notes='" + notes + '\'' +
                '}';
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public List<Diagnosis> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(List<Diagnosis> diagnoses) {
        this.diagnoses = diagnoses;
    }

    public List<Treatment> getTreatments() {
        return treatments;
    }

    public void setTreatments(List<Treatment> treatments) {
        this.treatments = treatments;
    }

    public @Nullable String getNotes() {
        return notes;
    }

    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }
}
