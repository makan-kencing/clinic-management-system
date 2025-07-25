package edu.dsa.ass.clinicmanagementsystem.entity;

import org.jetbrains.annotations.Nullable;

/**
 * The amount of {@link Medicine} to be prescribed for a {@link Treatment}.
 *
 * @author TODO
 * @see Medicine
 * @see Treatment
 */
public class Prescription extends IdentifiableEntity {
    private Treatment treatment;
    private Medicine medicine;
    private int quantity;
    private @Nullable String notes;
}
