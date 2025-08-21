package edu.dsa.clinic.entity;

import java.time.LocalDateTime;

/**
 * An appointment between a {@link Patient} and {@link Doctor} for a {@link Consultation} in the future.
 * The appointment is scheduled by a staff.
 *
 * @author wen han
 * @see Consultation
 */
public class Appointment extends IdentifiableEntity {
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime createdAt;
    private LocalDateTime expectedStartAt;
    private LocalDateTime expectedEndAt;

    public Patient getPatient() {
        return patient;
    }

    public Appointment setPatient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Appointment setDoctor(Doctor doctor) {
        this.doctor = doctor;
        return this;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Appointment setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public LocalDateTime getExpectedStartAt() {
        return expectedStartAt;
    }

    public Appointment setExpectedStartAt(LocalDateTime expectedStartAt) {
        this.expectedStartAt = expectedStartAt;
        return this;
    }

    public LocalDateTime getExpectedEndAt() {
        return expectedEndAt;
    }

    public Appointment setExpectedEndAt(LocalDateTime expectedEndAt) {
        this.expectedEndAt = expectedEndAt;
        return this;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", createdAt=" + createdAt +
                ", expectedStartAt=" + expectedStartAt +
                ", expectedEndAt=" + expectedEndAt +
                '}';
    }
}
