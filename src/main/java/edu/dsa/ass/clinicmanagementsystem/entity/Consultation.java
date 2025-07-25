package edu.dsa.ass.clinicmanagementsystem.entity;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

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
    private Diagnosis diagnosis;

    @Override
    public String toString() {
        return "Consultation{" +
                "patient=" + patient +
                ", doctor=" + doctor +
                ", diagnosis=" + diagnosis +
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

    public Diagnosis getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(Diagnosis diagnosis) {
        this.diagnosis = diagnosis;
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

    private List<Treatment> treatments;
    private @Nullable String notes;
}
