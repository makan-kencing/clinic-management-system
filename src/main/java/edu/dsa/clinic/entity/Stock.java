package edu.dsa.clinic.entity;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import org.jetbrains.annotations.Range;

import java.time.LocalDateTime;

/**
 * The actual physical instance of a {@link Product} in storage.
 *
 * @author makan-kencing
 */
public class Stock extends IdentifiableEntity {
    private Product product;
    @Range(from = 1, to = Integer.MAX_VALUE)
    private int stockInQuantity;
    private LocalDateTime stockInDate;
    private ListInterface<Dispensing> dispensings = new DoubleLinkedList<>();

    public Product getProduct() {
        return product;
    }

    public Stock setProduct(Product product) {
        this.product = product;
        return this;
    }

    public @Range(from = 1, to = Integer.MAX_VALUE) int getStockInQuantity() {
        return stockInQuantity;
    }

    public Stock setStockInQuantity(@Range(from = 1, to = Integer.MAX_VALUE) int stockInQuantity) {
        this.stockInQuantity = stockInQuantity;
        return this;
    }

    public LocalDateTime getStockInDate() {
        return stockInDate;
    }

    public Stock setStockInDate(LocalDateTime stockInDate) {
        this.stockInDate = stockInDate;
        return this;
    }

    public ListInterface<Dispensing> getDispensings() {
        return dispensings;
    }

    public Stock setDispensings(ListInterface<Dispensing> dispensings) {
        this.dispensings = dispensings;
        return this;
    }

    public Stock addDispensing(Dispensing dispensing) {
        dispensing.setStock(this);
        this.dispensings.add(dispensing);

        return this;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", medicine=" + product +
                ", stockInQuantity=" + stockInQuantity +
                ", stockInDate=" + stockInDate +
                '}';
    }
}
