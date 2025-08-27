package edu.dsa.clinic.dto;

import edu.dsa.clinic.entity.Product;

public class ProductCounter extends Counter<Product> {
    public ProductCounter(Product key) {
        super(key);
    }
}
