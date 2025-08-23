package edu.dsa.clinic.entity;

import org.jetbrains.annotations.Range;

/**
 * The actual {@link Medicine} dispensed for a written {@link Prescription}
 *
 * @author makan-kencing
 */
public class Dispensing extends IdentifiableEntity {
    private Prescription prescription;
    private Stock stock;
    private @Range(from = 1, to = Integer.MAX_VALUE) int quantity;

    public Prescription getPrescription() {
        return prescription;
    }

    public Dispensing setPrescription(Prescription prescription) {
        this.prescription = prescription;
        return this;
    }

    public Stock getStock() {
        return stock;
    }

    public Dispensing setStock(Stock stock) {
        this.stock = stock;
        return this;
    }

    public @Range(from = 1, to = Integer.MAX_VALUE) int getQuantity() {
        return quantity;
    }

    public Dispensing setQuantity(@Range(from = 1, to = Integer.MAX_VALUE) int quantity) {
        this.quantity = quantity;
        return this;
    }

    @Override
    public String toString() {
        return "Dispensing{" +
                "id=" + id +
                ", prescription=" + prescription +
                ", stock=" + stock +
                ", quantity=" + quantity +
                '}';
    }
}
