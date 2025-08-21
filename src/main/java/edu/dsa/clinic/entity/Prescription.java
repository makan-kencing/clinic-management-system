package edu.dsa.clinic.entity;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * The amount of {@link Medicine} to be prescribed for a {@link Treatment}.
 *
 * @author tan
 * @see Medicine
 * @see Treatment
 */
public class Prescription extends IdentifiableEntity {
    private Treatment treatment;
    private Medicine medicine;
    @Range(from = 1, to = Integer.MAX_VALUE)
    private int quantity;
    private @Nullable String notes;
    private final ListInterface<Dispensing> dispensedMedications = new DoubleLinkedList<>();

    public Treatment getTreatment() {
        return treatment;
    }

    public Prescription setTreatment(Treatment treatment) {
        this.treatment = treatment;
        return this;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public Prescription setMedicine(Medicine medicine) {
        this.medicine = medicine;
        return this;
    }

    public @Range(from = 1, to = Integer.MAX_VALUE) int getQuantity() {
        return quantity;
    }

    public Prescription setQuantity(@Range(from = 1, to = Integer.MAX_VALUE) int quantity) {
        this.quantity = quantity;
        return this;
    }

    public @Nullable String getNotes() {
        return notes;
    }

    public Prescription setNotes(@Nullable String notes) {
        this.notes = notes;
        return this;
    }

    public ListInterface<Dispensing> getDispensedMedications() {
        return dispensedMedications;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                ", id=" + id +
                ", treatment=" + treatment +
                ", medicine=" + medicine +
                ", quantity=" + quantity +
                ", notes='" + notes + '\'' +
                ", dispensedMedications=" + dispensedMedications +
                '}';
    }
}
