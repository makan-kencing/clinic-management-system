package edu.dsa.clinic.dto;

import edu.dsa.clinic.entity.Medicine;
import edu.dsa.clinic.entity.MedicineAdministrationType;
import edu.dsa.clinic.entity.Product;

import java.math.BigDecimal;

public record CreateProductDTO(
    String name,
    String brand,
    Medicine medicine,
    MedicineAdministrationType administrationType,
    BigDecimal cost,
    BigDecimal price,
    Integer autoOrderThreshold
) {
    public Product create() {
        return new Product()
                .setName(this.name)
                .setBrand(this.brand)
                .setMedicine(this.medicine)
                .setAdministrationType(this.administrationType)
                .setCost(this.cost)
                .setPrice(this.price)
                .setAutoOrderThreshold(this.autoOrderThreshold);
    }

    public void update(Product product) {
        if (this.name != null)
            product.setName(this.name);

        if (this.brand != null)
            product.setName(this.brand);

        if (this.medicine != null)
            product.setMedicine(this.medicine);

        if (this.administrationType != null)
            product.setAdministrationType(this.administrationType);

        if (this.cost != null)
            product.setCost(this.cost);

        if (this.price != null)
            product.setPrice(this.price);

        if (this.autoOrderThreshold != null)
            product.setAutoOrderThreshold(this.autoOrderThreshold);
    }
}
