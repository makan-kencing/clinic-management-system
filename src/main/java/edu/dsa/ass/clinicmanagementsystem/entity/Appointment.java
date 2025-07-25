package edu.dsa.ass.clinicmanagementsystem.entity;

import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;

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
}
