package edu.dsa.ass.clinicmanagementsystem.entity;

import java.time.LocalDateTime;

public class Stock extends IdentifiableEntity {
    private Medicine medicine;
    private int stockInQuantity;
    private LocalDateTime stockInDate;
    private String location;
    private int quantityLeft;

    public Medicine getMedicine() {
        return medicine;
    }

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

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }

    public int getStockInQuantity() {
        return stockInQuantity;
    }

    public void setStockInQuantity(int stockInQuantity) {
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

    public int getQuantityLeft() {
        return quantityLeft;
    }

    public void setQuantityLeft(int quantityLeft) {
        this.quantityLeft = quantityLeft;
    }
}
