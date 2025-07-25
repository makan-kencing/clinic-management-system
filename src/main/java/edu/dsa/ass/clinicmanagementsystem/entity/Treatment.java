package edu.dsa.ass.clinicmanagementsystem.entity;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * The prescribed treatment to treat or cure symptom(s) after a {@link Consultation}
 *
 * @author TODO
 * @see Consultation
 */
public class Treatment extends IdentifiableEntity{
    private Diagnosis diagnosis;
    private List<Prescription> prescriptions;
    private String symptom;
    private @Nullable String notes;
}
