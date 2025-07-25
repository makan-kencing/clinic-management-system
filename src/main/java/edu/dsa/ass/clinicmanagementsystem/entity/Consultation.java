package edu.dsa.ass.clinicmanagementsystem.entity;

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
    private Diagnosis diagnosis;
    private List<Treatment> treatments;
    private @Nullable String notes;
}
