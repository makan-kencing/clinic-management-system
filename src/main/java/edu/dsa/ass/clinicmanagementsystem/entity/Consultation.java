package edu.dsa.ass.clinicmanagementsystem.entity;

import java.util.List;

/**
 * The meeting session between a patient and a doctor.
 *
 * @author TODO
 * @see Doctor
 * @see Patient
 */
public class Consultation {
    private Patient patient;
    private Doctor doctor;
    private Diagnosis diagnosis;
    private List<Treatment> treatments;
    private String notes;
}
