package edu.dsa.clinic.filter;

import edu.dsa.clinic.entity.Dispensing;
import edu.dsa.clinic.entity.Product;
import edu.dsa.clinic.entity.Stock;
import edu.dsa.clinic.lambda.Filter;

public interface DispensingFilter {
    static Filter<Dispensing> byProduct(Product product) {
        return d -> StockFilter.byProduct(product).filter(d.getStock());
    }

    static Filter<Dispensing> byStock(Stock stock) {
        return d -> d.getStock().equals(stock);
    }
}
