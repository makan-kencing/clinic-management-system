package edu.dsa.ass.clinicmanagementsystem.entity;

import java.util.List;

/**
 * A detailed record for documenting a {@link Patient} symptoms and diagnosis after a {@link Consultation}.
 *
 * @author TODO
 * @see Patient
 * @see Consultation
 */
public class Diagnosis {
    private List<String> symptoms;
    private String diagnosis;
    private String notes;
}
