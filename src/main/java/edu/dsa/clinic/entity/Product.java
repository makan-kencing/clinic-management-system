package edu.dsa.clinic.entity;

import edu.dsa.clinic.adt.DoubleLinkedList;
import edu.dsa.clinic.adt.ListInterface;
import edu.dsa.clinic.adt.SortedDoubleLinkedList;
import edu.dsa.clinic.dto.Inventory;

import java.math.BigDecimal;

/**
 * The {@link Product} a {@link Medicine} is sold under as.
 *
 * @author makan-kencing
 */
public class Product extends IdentifiableEntity {
    private String name;
    private String brand;
    private Medicine medicine;
    private MedicineAdministrationType administrationType;
    private BigDecimal cost;
    private BigDecimal price;
    private final Inventory inventory = new Inventory();
    private final ListInterface<Product> substitutes = new DoubleLinkedList<>();
    private final ListInterface<Product> substitutesFor = new DoubleLinkedList<>();
    private final ListInterface<Stock> stocks = new SortedDoubleLinkedList<>((s1, s2) ->
            s1.getStockInDate().compareTo(s2.getStockInDate())
    );

    public String getName() {
        return name;
    }

    public Product setName(String name) {
        this.name = name;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public Product setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public Product setMedicine(Medicine medicine) {
        this.medicine = medicine;
        return this;
    }

    public MedicineAdministrationType getAdministrationType() {
        return administrationType;
    }

    public Product setAdministrationType(MedicineAdministrationType administrationType) {
        this.administrationType = administrationType;
        return this;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public Product setCost(BigDecimal cost) {
        this.cost = cost;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Product setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public ListInterface<Product> getSubstitutes() {
        return substitutes;
    }

    public Product addSubstitute(Product product) {
        product.substitutesFor.add(this);

        this.substitutes.add(product);
        return this;
    }

    public ListInterface<Product> getSubstitutesFor() {
        return substitutesFor;
    }

    public Product addSubstituteFor(Product product) {
        product.substitutes.add(this);

        this.substitutesFor.add(product);
        return this;
    }

    public ListInterface<Stock> getStocks() {
        return stocks;
    }

    public Product addStock(Stock stock) {
        stock.setProduct(this);

        this.stocks.add(stock);
        return this;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", medicine=" + medicine +
                ", brand='" + brand + '\'' +
                ", cost=" + cost +
                ", price=" + price +
                ", inventory=" + inventory +
                ", substitutes=" + substitutes +
                ", substitutesFor=" + substitutesFor +
                ", stocks=" + stocks +
                '}';
    }
}
