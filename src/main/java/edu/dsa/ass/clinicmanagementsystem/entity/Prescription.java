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

    public Treatment getTreatment() {
        return treatment;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                "treatment=" + treatment +
                ", medicine=" + medicine +
                ", quantity=" + quantity +
                ", notes='" + notes + '\'' +
                '}';
    }

    public void setTreatment(Treatment treatment) {
        this.treatment = treatment;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public @Nullable String getNotes() {
        return notes;
    }

    public void setNotes(@Nullable String notes) {
        this.notes = notes;
    }
}
