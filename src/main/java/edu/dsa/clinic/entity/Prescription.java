package edu.dsa.clinic.entity;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * The amount of {@link Product} to be prescribed for a {@link Treatment}.
 *
 * @author tan
 * @see Product
 * @see Treatment
 */
public class Prescription extends IdentifiableEntity {
    private Treatment treatment;
    private Product product;
    @Range(from = 1, to = Integer.MAX_VALUE)
    private int quantity;
    private @Nullable String notes;
    private ListInterface<Dispensing> dispensing = new DoubleLinkedList<>();

    public Treatment getTreatment() {
        return treatment;
    }

    public Prescription setTreatment(Treatment treatment) {
        this.treatment = treatment;
        return this;
    }

    public Product getProduct() {
        return product;
    }

    public Prescription setProduct(Product product) {
        this.product = product;
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

    public ListInterface<Dispensing> getDispensing() {
        return dispensing;
    }

    public Prescription setDispensing(ListInterface<Dispensing> dispensing) {
        this.dispensing = dispensing;
        return this;
    }

    public Prescription addDispensing(Dispensing dispensing) {
        dispensing.setPrescription(this);

        this.dispensing.add(dispensing);
        return this;
    }

    @Override
    public String toString() {
        return "Prescription{" +
                ", id=" + id +
                ", treatment=" + treatment +
                ", medicine=" + product +
                ", quantity=" + quantity +
                ", notes='" + notes + '\'' +
                ", dispensedMedications=" + dispensing +
                '}';
    }
}
