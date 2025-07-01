package edu.dsa.ass.clinicmanagementsystem.entity;

import java.util.List;

/**
 * The prescribed treatment to treat or cure symptom(s) after a {@link Consultation}
 *
 * @author TODO
 * @see Consultation
 */
public class Treatment {
    private List<Prescription> prescriptions;
    private String notes;
}
