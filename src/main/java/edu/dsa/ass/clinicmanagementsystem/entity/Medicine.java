package edu.dsa.ass.clinicmanagementsystem.entity;

import java.math.BigDecimal;
import java.util.List;

/**
 * The medicine to be prescribed to {@link Patient} for treatments.
 *
 * @author TODO
 */
public class Medicine extends IdentifiableEntity {
    private String name;
    private MedicineType type;
    private String brand;
    private BigDecimal cost;
    private BigDecimal price;
    private List<Medicine> substitutes;
    private List<Medicine> substitutesFor;

    @Override
    public String toString() {
        return "Medicine{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", brand='" + brand + '\'' +
                ", cost=" + cost +
                ", price=" + price +
                ", substitutes=" + substitutes +
                ", substitutesFor=" + substitutesFor +
                ", stocks=" + stocks +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MedicineType getType() {
        return type;
    }

    public void setType(MedicineType type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<Medicine> getSubstitutes() {
        return substitutes;
    }

    public void setSubstitutes(List<Medicine> substitutes) {
        this.substitutes = substitutes;
    }

    public List<Medicine> getSubstitutesFor() {
        return substitutesFor;
    }

    public void setSubstitutesFor(List<Medicine> substitutesFor) {
        this.substitutesFor = substitutesFor;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

    private List<Stock> stocks;
}
