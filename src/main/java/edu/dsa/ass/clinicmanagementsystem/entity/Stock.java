package edu.dsa.ass.clinicmanagementsystem.entity;

import org.jetbrains.annotations.Range;

import java.time.LocalDateTime;

public class Stock extends IdentifiableEntity {
    private Medicine medicine;
    @Range(from = 1, to = Integer.MAX_VALUE)
    private int stockInQuantity;
    private LocalDateTime stockInDate;
    private String location;
    @Range(from = 0, to = Integer.MAX_VALUE)
    private int quantityLeft;

    @Override
    public String toString() {
        return "Stock{" +
                "medicine=" + medicine +
                ", stockInQuantity=" + stockInQuantity +
                ", stockInDate=" + stockInDate +
                ", location='" + location + '\'' +
                ", quantityLeft=" + quantityLeft +
                '}';
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public @Range(from = 1, to = Integer.MAX_VALUE) int getStockInQuantity() {
        return stockInQuantity;
    }

    public void setStockInQuantity(@Range(from = 1, to = Integer.MAX_VALUE) int stockInQuantity) {
        this.stockInQuantity = stockInQuantity;
    }


    public LocalDateTime getStockInDate() {
        return stockInDate;
    }

    public void setStockInDate(LocalDateTime stockInDate) {
        this.stockInDate = stockInDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int getQuantityLeft() {
        return quantityLeft;
    }

    public void setQuantityLeft(@Range(from = 0, to = Integer.MAX_VALUE) int quantityLeft) {
        this.quantityLeft = quantityLeft;
    }
}
