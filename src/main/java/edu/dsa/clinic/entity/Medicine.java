package edu.dsa.clinic.entity;

import edu.dsa.clinic.adt.ListInterface;

import java.math.BigDecimal;

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
    private ListInterface<Medicine> substitutes;
    private ListInterface<Medicine> substitutesFor;

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
    private ListInterface<Stock> stocks;

    public ListInterface<Medicine> getSubstitutes() {
        return substitutes;
    }

    public void setSubstitutes(ListInterface<Medicine> substitutes) {
        this.substitutes = substitutes;
    }

    public ListInterface<Medicine> getSubstitutesFor() {
        return substitutesFor;
    }

    public void setSubstitutesFor(ListInterface<Medicine> substitutesFor) {
        this.substitutesFor = substitutesFor;
    }

    public ListInterface<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(ListInterface<Stock> stocks) {
        this.stocks = stocks;
    }
}
