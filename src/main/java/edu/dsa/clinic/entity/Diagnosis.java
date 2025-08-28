package edu.dsa.clinic.entity;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import org.jetbrains.annotations.Nullable;

/**
 * A detailed record for documenting a {@link Patient} disease diagnosis after a {@link Consultation}.
 *
 * @author tan
 * @see Patient
 * @see Consultation
 */
public class Diagnosis extends IdentifiableEntity {
    private Consultation consultation;
    private String diagnosis;
    private String description;
    private @Nullable String notes;
    private ListInterface<Treatment> treatments = new DoubleLinkedList<>();

    public Consultation getConsultation() {
        return consultation;
    }

    public Diagnosis setConsultation(Consultation consultation) {
        this.consultation = consultation;
        return this;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public Diagnosis setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Diagnosis setDescription(String description) {
        this.description = description;
        return this;
    }

    public @Nullable String getNotes() {
        return notes;
    }

    public Diagnosis setNotes(@Nullable String notes) {
        this.notes = notes;
        return this;
    }

    public ListInterface<Treatment> getTreatments() {
        return treatments;
    }

    public Diagnosis setTreatments(ListInterface<Treatment> treatments) {
        this.treatments = treatments;
        return this;
    }

    public Diagnosis addTreatment(Treatment treatment) {
        treatment.setDiagnosis(this);

        this.treatments.add(treatment);
        return this;
    }
}
