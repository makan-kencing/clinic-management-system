package edu.dsa.ass.clinicmanagementsystem.entity;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * An appointment created by the patient or a staff for a {@link Consultation} in the future.
 *
 * @author TODO
 * @see Consultation
 */
public class Appointment extends IdentifiableEntity {
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime createdAt;
    private LocalDateTime expectedStartAt;
    private LocalDateTime expectedEndAt;

    @Override
    public String toString() {
        return "Appointment{" +
                "patient=" + patient +
                ", doctor=" + doctor +
                ", createdAt=" + createdAt +
                ", expectedStartAt=" + expectedStartAt +
                ", expectedEndAt=" + expectedEndAt +
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getExpectedStartAt() {
        return expectedStartAt;
    }

    public void setExpectedStartAt(LocalDateTime expectedStartAt) {
        this.expectedStartAt = expectedStartAt;
    }

    public LocalDateTime getExpectedEndAt() {
        return expectedEndAt;
    }

    public void setExpectedEndAt(LocalDateTime expectedEndAt) {
        this.expectedEndAt = expectedEndAt;
    }
}
