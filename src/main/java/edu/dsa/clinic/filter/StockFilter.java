package edu.dsa.clinic.filter;

import edu.dsa.clinic.control.MedicineController;
import edu.dsa.clinic.entity.Product;
import edu.dsa.clinic.entity.Stock;
import edu.dsa.clinic.lambda.Filter;
import edu.dsa.clinic.utils.Ordered;
import org.jetbrains.annotations.Range;

public interface StockFilter {
    static Filter<Stock> byProduct(Product product) {
        return s -> s.getProduct().equals(product);
    }

    static Filter<Stock> byStockBetween(
            @Range(from = 0, to = Integer.MAX_VALUE) int from,
            @Range(from = 1, to = Integer.MAX_VALUE) int to,
            boolean inclusive
    ) {
        return s -> Ordered.isBetween(MedicineController.getStockQuantityLeft(s), from, to);
    }

    static Filter<Stock> hasStock() {
        return s -> MedicineController.getStockQuantityLeft(s) > 0;
    }
}
