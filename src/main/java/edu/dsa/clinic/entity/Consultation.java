package edu.dsa.clinic.entity;

import edu.dsa.clinic.adt.ListInterface;
import org.jetbrains.annotations.Nullable;

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
    private ListInterface<Diagnosis> diagnoses;
    private ListInterface<Treatment> treatments;
    private ConsultationType consultationType;
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

    public ListInterface<Diagnosis> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(ListInterface<Diagnosis> diagnoses) {
        this.diagnoses = diagnoses;
    }

    public ListInterface<Treatment> getTreatments() {
        return treatments;
    }

    public void setTreatments(ListInterface<Treatment> treatments) {
        this.treatments = treatments;
    }

    public @Nullable String getNotes() {
        return notes;
    }

    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }

    public ConsultationType getConsultationType() {
        return consultationType;
    }

    public void setConsultationType(ConsultationType consultationType) {
        this.consultationType = consultationType;
    }
}
