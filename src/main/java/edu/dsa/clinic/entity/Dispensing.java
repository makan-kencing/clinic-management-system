package edu.dsa.clinic.entity;

import org.jetbrains.annotations.Nullable;

public class Dispensing extends IdentifiableEntity {
    public record Dispense(Stock stock, int quantity, String notes) {
    }

    private Prescription prescription;
    private @Nullable Dispense dispense;

    public Prescription getPrescription() {
        return prescription;
    }

    public void setPrescription(Prescription prescription) {
        this.prescription = prescription;
    }

    public @Nullable Dispense getDispense() {
        return dispense;
    }

    public void setDispense(@Nullable Dispense dispense) {
        this.dispense = dispense;
    }
}
