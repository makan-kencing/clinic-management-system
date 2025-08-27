package edu.dsa.clinic.entity;

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
    private String location;
    @Range(from = 0, to = Integer.MAX_VALUE)
    private int quantityLeft;

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

    public String getLocation() {
        return location;
    }

    public Stock setLocation(String location) {
        this.location = location;
        return this;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getQuantityLeft() {
        return quantityLeft;
    }

    public Stock setQuantityLeft(@Range(from = 0, to = Integer.MAX_VALUE) int quantityLeft) {
        this.quantityLeft = quantityLeft;
        return this;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", medicine=" + product +
                ", stockInQuantity=" + stockInQuantity +
                ", stockInDate=" + stockInDate +
                ", location='" + location + '\'' +
                ", quantityLeft=" + quantityLeft +
                '}';
    }
}
