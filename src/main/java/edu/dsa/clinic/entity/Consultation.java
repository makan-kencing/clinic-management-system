package edu.dsa.clinic.entity;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDateTime;

/**
 * The meeting session between a {@link Patient} and a {@link Doctor}.
 *
 * @author tan
 */
public class Consultation extends IdentifiableEntity {
    private Patient patient;
    private Doctor doctor;
    private ConsultationType type;
    private LocalDateTime consultedAt;
    private @Nullable String notes;
    private final ListInterface<Diagnosis> diagnoses = new DoubleLinkedList<>();

    public Patient getPatient() {
        return patient;
    }

    public Consultation setPatient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Consultation setDoctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

    public ConsultationType getType() {
        return type;
    }

    public Consultation setType(ConsultationType type) {
        this.type = type;
        return this;
    }

    public LocalDateTime getConsultedAt() {
        return consultedAt;
    }

    public Consultation setConsultedAt(LocalDateTime consultedAt) {
        this.consultedAt = consultedAt;
        return this;
    }

    public @Nullable String getNotes() {
        return notes;
    }

    public Consultation setNotes(@Nullable String notes) {
        this.notes = notes;
        return this;
    }

    public ListInterface<Diagnosis> getDiagnoses() {
        return diagnoses;
    }

    public Consultation addDiagnosis(Diagnosis diagnosis) {
        diagnosis.setConsultation(this);

        this.diagnoses.add(diagnosis);
        return this;
    }

    @Override
    public String toString() {
        return "Consultation{" +
                "id=" + id +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", type=" + type +
                ", consultedAt=" + consultedAt +
                ", notes='" + notes + '\'' +
                ", diagnoses=" + diagnoses +
                '}';
    }
}