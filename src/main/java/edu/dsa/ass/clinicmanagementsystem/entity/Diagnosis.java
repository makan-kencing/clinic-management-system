package edu.dsa.ass.clinicmanagementsystem.entity;

import org.jetbrains.annotations.Nullable;

/**
 * A detailed record for documenting a {@link Patient} symptoms and diagnosis after a {@link Consultation}.
 *
 * @author TODO
 * @see Patient
 * @see Consultation
 */
public class Diagnosis extends IdentifiableEntity {
    private Consultation consultation;
    private String description;
    private @Nullable String notes;
}
