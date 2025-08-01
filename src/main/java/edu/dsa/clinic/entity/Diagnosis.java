package edu.dsa.clinic.entity;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A detailed record for documenting a {@link Patient} symptoms and diagnosis after a {@link Consultation}.
 *
 * @author TODO
 * @see Patient
 * @see Consultation
 */
public class Diagnosis extends IdentifiableEntity {
    private Consultation consultation;
    private List<Prescription> prescriptions;
    private String description;
    private @Nullable String notes;

    @Override
    public String toString() {
        return "Diagnosis{" +
                "consultation=" + consultation +
                ", prescriptions=" + prescriptions +
                ", description='" + description + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public @Nullable String getNotes() {
        return notes;
    }

    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }
}
