package edu.dsa.clinic.entity;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;

import java.math.BigDecimal;

/**
 * The information of a {@link Medicine} product.
 *
 * @author makan-kencing
 */
public class Medicine extends IdentifiableEntity {
    private String name;
    private MedicineType type;
    private String brand;
    private BigDecimal cost;
    private BigDecimal price;
    private final Inventory inventory = new Inventory();
    private final ListInterface<Medicine> substitutes = new DoubleLinkedList<>();
    private final ListInterface<Medicine> substitutesFor = new DoubleLinkedList<>();

    public String getName() {
        return name;
    }

    public Medicine setName(String name) {
        this.name = name;
        return this;
    }

    public MedicineType getType() {
        return type;
    }

    public Medicine setType(MedicineType type) {
        this.type = type;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public Medicine setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public Medicine setCost(BigDecimal cost) {
        this.cost = cost;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Medicine setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ListInterface<Medicine> getSubstitutes() {
        return substitutes;
    }

    public ListInterface<Medicine> getSubstitutesFor() {
        return substitutesFor;
    }

    @Override
    public String toString() {
        return "Medicine{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", brand='" + brand + '\'' +
                ", cost=" + cost +
                ", price=" + price +
                ", inventory=" + inventory +
                ", substitutes=" + substitutes +
                ", substitutesFor=" + substitutesFor +
                '}';
    }
}
