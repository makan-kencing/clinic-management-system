package edu.dsa.clinic.entity;

import org.jetbrains.annotations.Nullable;

/**
 * The actual {@link Medicine} dispensed for a written {@link Prescription}
 *
 * @author makan-kencing
 */
public class Dispensing extends IdentifiableEntity {
    public record Dispense(Stock stock, int quantity, String notes) {
    }

    private Prescription prescription;
    private @Nullable Dispense dispense;

    public Prescription getPrescription() {
        return prescription;
    }

    public Dispensing setPrescription(Prescription prescription) {
        this.prescription = prescription;
        return this;
    }

    public @Nullable Dispense getDispense() {
        return dispense;
    }

    public Dispensing setDispense(@Nullable Dispense dispense) {
        this.dispense = dispense;
        return this;
    }

    @Override
    public String toString() {
        return "Dispensing{" +
                "id=" + id +
                ", prescription=" + prescription +
                ", dispense=" + dispense +
                '}';
    }
}
